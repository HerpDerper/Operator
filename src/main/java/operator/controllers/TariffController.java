package operator.controllers;

import operator.models.PhoneNumber;
import operator.models.Tariff;
import operator.repositories.PhoneNumberRepository;
import operator.repositories.TariffRepository;
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
import java.util.Random;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class TariffController {

    private final TariffRepository tariffRepository;

    private final PhoneNumberRepository phoneNumberRepository;

    public TariffController(TariffRepository tariffRepository,
                            PhoneNumberRepository phoneNumberRepository) {
        this.tariffRepository = tariffRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @GetMapping("/tariff/index")
    public String tariffIndex(Model model) {
        model.addAttribute("tariffs", tariffRepository.findAll());
        return "tariff/Index";
    }

    @GetMapping("/tariff/filter")
    public String tariffFilter(@RequestParam(required = false) String name, Model model) {
        Iterable<Tariff> tariffs;
        if (name != null && !name.equals(""))
            tariffs = tariffRepository.findByNameContains(name);
        else
            tariffs = tariffRepository.findAll();
        model.addAttribute("tariffs", tariffs);
        return "tariff/Index";
    }

    @PostMapping("/tariff/editTariff")
    public String tariffEdit(@ModelAttribute("tariff") @Valid Tariff tariff, BindingResult bindingResult, Model model) {
        if (tariffRepository.findTariffByName(tariff.getName()) != null
                && !tariffRepository.findTariffByName(tariff.getName()).getIdTariff().equals(tariff.getIdTariff())) {
            bindingResult.addError(new ObjectError("name", "Данный тариф уже существует"));
            model.addAttribute("errorMessage", "Данный тариф уже существует");
        }
        if (bindingResult.hasErrors())
            return "tariff/Edit";
        tariffRepository.save(tariff);
        return "redirect:/tariff/index";
    }

    @PostMapping("/tariff/edit")
    public String tariffEdit(@RequestParam long idTariff, Model model) {
        Tariff tariff = tariffRepository.findById(idTariff).get();
        model.addAttribute("tariff", tariff);
        return "tariff/Edit";
    }

    @PostMapping("/tariff/create")
    public String tariffCreate(@ModelAttribute("tariff") @Valid Tariff tariff, BindingResult bindingResult, Model model) {
        if (tariffRepository.findTariffByName(tariff.getName()) != null) {
            bindingResult.addError(new ObjectError("name", "Данный тариф уже существует"));
            model.addAttribute("errorMessage", "Данный тариф уже существует");
        }
        if (bindingResult.hasErrors())
            return "tariff/Create";
        tariffRepository.save(tariff);
        for (int i = 0; i < 19; i++) {
            PhoneNumber phoneNumber = new PhoneNumber();
            Random random = new Random();
            phoneNumber.setTariff(tariff);
            phoneNumber.setPUK(String.valueOf(random.nextInt((99999999 - 10000000) + 1) + 10000000));
            phoneNumber.setICCID(String.valueOf(random.nextInt((999999999 - 100000000) + 1) + 100000000)
                    + String.valueOf(random.nextInt((999999999 - 100000000) + 1) + 100000000));
            phoneNumber.setNumber("+7(" + String.valueOf(random.nextInt((999 - 900) + 1) + 900) + ")"
                    + String.valueOf(random.nextInt((999 - 100) + 1) + 100) + "-"
                    + String.valueOf(random.nextInt((99 - 10) + 1) + 10) + "-"
                    + String.valueOf(random.nextInt((99 - 10) + 1) + 10));
            phoneNumber.setStatus(true);
            phoneNumberRepository.save(phoneNumber);
        }
        return "redirect:/tariff/index";
    }

    @GetMapping("/tariff/create")
    public String tariffCreate(@ModelAttribute("tariff") Tariff tariff) {
        return "tariff/Create";
    }

}