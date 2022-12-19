package operator.controllers;

import operator.models.PhoneNumber;
import operator.repositories.PhoneNumberRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class PhoneNumberController {

    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberController(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @GetMapping("/phoneNumber/index")
    public String phoneNumberIndex(Model model) {
        model.addAttribute("phoneNumbers", phoneNumberRepository.findAll());
        return "phoneNumber/Index";
    }

    @GetMapping("/phoneNumber/filter")
    public String phoneNumberFilter(@RequestParam(required = false) String number, Model model) {
        Iterable<PhoneNumber> phoneNumbers;
        if (number != null && !number.equals(""))
            phoneNumbers = phoneNumberRepository.findByNumberContains(number);
        else
            phoneNumbers = phoneNumberRepository.findAll();
        model.addAttribute("phoneNumbers", phoneNumbers);
        return "phoneNumber/Index";
    }

    @PostMapping("/phoneNumber/editStatus")
    public String phoneNumberEditStatus(@RequestParam long idPhoneNumber, Model model) {
        PhoneNumber phoneNumber = phoneNumberRepository.findById(idPhoneNumber).get();
        phoneNumber.setStatus(!phoneNumber.isStatus());
        model.addAttribute("phoneNumbers", phoneNumberRepository.findAll());
        phoneNumberRepository.save(phoneNumber);
        return "phoneNumber/Index";
    }

}