package operator.controllers;

import operator.models.*;
import operator.repositories.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class ClientNumberController {

    private final ClientRepository clientRepository;

    private final ClientNumberRepository clientNumberRepository;

    private final PassportDataRepository passportDataRepository;

    private final PhoneNumberRepository phoneNumberRepository;

    private final TariffRepository tariffRepository;

    public ClientNumberController(ClientRepository clientRepository,
                                  ClientNumberRepository clientNumberRepository,
                                  PassportDataRepository passportDataRepository,
                                  PhoneNumberRepository phoneNumberRepository,
                                  TariffRepository tariffRepository) {
        this.clientRepository = clientRepository;
        this.clientNumberRepository = clientNumberRepository;
        this.passportDataRepository = passportDataRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.tariffRepository = tariffRepository;
    }

    @GetMapping("/clientNumber/index")
    public String clientNumberIndex(Model model) {
        model.addAttribute("clientNumbers", clientNumberRepository.findAll());
        return "clientNumber/Index";
    }

    @GetMapping("/clientNumber/filter")
    public String clientNumberFilter(@RequestParam(required = false) String number, Model model) {
        Iterable<ClientNumber> clientNumbers;
        if (number != null && !number.equals(""))
            clientNumbers = clientNumberRepository.findByPhoneNumberNumberContains(number);
        else
            clientNumbers = clientNumberRepository.findAll();
        model.addAttribute("clientNumbers", clientNumbers);
        return "clientNumber/Index";
    }

    @GetMapping("/clientNumber/chart")
    public String clientNumberChart(Model model) {
        model.addAttribute("clientNumbers",
                getClientNumberList(((List) clientNumberRepository.findAll()), tariffRepository.findAll()));
        model.addAttribute("tariffs", getTariffList(tariffRepository.findAll()));
        return "clientNumber/Chart";
    }

    @PreAuthorize("hasAnyAuthority('SELLER')")
    @PostMapping("/clientNumber/create")
    public String clientNumberCreate(@RequestParam String number,
                                     @ModelAttribute("clientNumber") @Valid ClientNumber clientNumber, BindingResult bindingResultClientNumber,
                                     @ModelAttribute("client") @Valid Client client, BindingResult bindingResultClient,
                                     @ModelAttribute("passportData") @Valid PassportData passportData, BindingResult bindingResultPassport,
                                     Model model) {
        if (number == null)
            return "clientNumber/Create";
        PhoneNumber phoneNumber = phoneNumberRepository.findPhoneNumberByNumber(number);
        if (passportData.getIssued() != null && passportData.getDateBirth() != null) {
            long milliseconds = passportData.getIssueDate().getTime() - passportData.getDateBirth().getTime();
            int years = (int) (milliseconds / (24 * 60 * 60 * 1000 * 365.25));
            milliseconds = new Date().getTime() - passportData.getDateBirth().getTime();
            int ages = (int) (milliseconds / (24 * 60 * 60 * 1000 * 365.25));
            if (passportData.getDateBirth().after(passportData.getIssueDate())
                    || ages < 18 || years < 14 || (years > 14 && years < 20) || (years > 20 && years < 45)) {
                bindingResultPassport.addError(new ObjectError("issueDate", "Неправильно введена дата выдачи паспорта"));
                model.addAttribute("errorMessageIssueDate", "Неправильно введена дата выдачи паспорта");
            }
        }
        if (bindingResultPassport.hasErrors() || bindingResultClientNumber.hasErrors() || bindingResultClient.hasErrors()) {
            model.addAttribute("phoneNumbers", phoneNumberRepository.findAll());
            return "clientNumber/Create";
        }
        passportDataRepository.save(passportData);
        client.setPassportData(passportData);
        clientRepository.save(client);
        clientNumber.setPhoneNumber(phoneNumber);
        clientNumber.setClient(client);
        clientNumberRepository.save(clientNumber);
        phoneNumber.setStatus(false);
        phoneNumberRepository.save(phoneNumber);
        return "redirect:/cart/index";
    }

    @PreAuthorize("hasAnyAuthority('SELLER')")
    @GetMapping("/clientNumber/create")
    public String clientNumberCreate(@ModelAttribute("clientNumber") ClientNumber clientNumber,
                                     @ModelAttribute("client") Client client,
                                     @ModelAttribute("passportData") PassportData passportData, Model model) {
        model.addAttribute("phoneNumbers", phoneNumberRepository.findAll());
        return "clientNumber/Create";
    }

    @PostMapping("/clientNumber/details")
    public String clientNumberDetails(@RequestParam long idClientNumber, Model model) {
        model.addAttribute("clientNumber", clientNumberRepository.findById(idClientNumber).get());
        return "clientNumber/Details";
    }

    private static List<String> getTariffList(List<Tariff> tariffs) {
        List<String> tariffNames = new ArrayList<>();
        for (var tariff : tariffs) {
            tariffNames.add(tariff.getName());
        }
        return tariffNames;
    }

    private static List<Double> getClientNumberList(List<ClientNumber> clientNumbers, List<Tariff> tariffs) {
        List<Double> clientNumbersNames = new ArrayList<>();
        for (var tariff : tariffs) {
            double allSales = 0;
            for (var clientNumber : clientNumbers) {
                if (tariff.getName().equals(clientNumber.getPhoneNumber().getTariff().getName()))
                    allSales += (double) clientNumber.getPhoneNumber().getTariff().getPriceMount() + 50;
            }
            clientNumbersNames.add(allSales);
        }
        return clientNumbersNames;
    }

}