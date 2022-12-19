package operator.controllers;

import operator.models.*;
import operator.repositories.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('CLIENT')")
@Controller
public class ClientPageController {

    private final UserRepository userRepository;

    private final TariffRepository tariffRepository;

    private final PhoneNumberRepository phoneNumberRepository;

    private final ProductRepository productRepository;

    private final BookingRepository bookingRepository;

    public ClientPageController(UserRepository userRepository,
                                TariffRepository tariffRepository,
                                PhoneNumberRepository phoneNumberRepository,
                                ProductRepository productRepository,
                                BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.productRepository = productRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/client/tariffIndex")
    public String clientTariffIndex(Model model) {
        model.addAttribute("tariffs", tariffRepository.findAll());
        return "clientPage/TariffIndex";
    }

    @GetMapping("/client/phoneNumberIndex")
    public String clientPhoneNumberIndex(@RequestParam long idTariff, Model model) {
        Tariff tariff = tariffRepository.findById(idTariff).get();
        model.addAttribute("phoneNumbers", phoneNumberRepository.findByTariff(tariff));
        model.addAttribute("tariff", tariff);
        return "clientPage/PhoneNumberIndex";
    }

    @GetMapping("/client/productIndex")
    public String clientProductIndex(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "clientPage/ProductIndex";
    }

    @GetMapping("/client/bookingIndex")
    public String clientBookingIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("bookings", bookingRepository.findByUserUsername(authentication.getName()));
        return "clientPage/BookingIndex";
    }

    @GetMapping("/client/phoneNumberFilter")
    public String clientPhoneNumberFilter(@RequestParam(required = false) String number, @RequestParam String tariffName, Model model) {
        Iterable<PhoneNumber> phoneNumbers;
        Tariff tariff = tariffRepository.findTariffByName(tariffName);
        if (number != null && !number.equals(""))
            phoneNumbers = phoneNumberRepository.findByTariffAndNumberContains(tariff, number);
        else
            phoneNumbers = phoneNumberRepository.findByTariff(tariff);
        model.addAttribute("phoneNumbers", phoneNumbers);
        model.addAttribute("tariff", tariff);
        return "clientPage/PhoneNumberIndex";
    }

    @GetMapping("/client/productFilter")
    public String clientProductFilter(@RequestParam(required = false) String name, Model model) {
        Iterable<Product> products;
        if (name != null && !name.equals(""))
            products = productRepository.findByNameContains(name);
        else
            products = productRepository.findAll();
        model.addAttribute("products", products);
        return "clientPage/ProductIndex";
    }

    @PostMapping("/client/bookingCreateProduct")
    public String clientBookingCreateProduct(@RequestParam long idProduct) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Product product = productRepository.findById(idProduct).get();
        User user = userRepository.findUserByUsername(authentication.getName());
        Booking booking = new Booking(user, product);
        bookingRepository.save(booking);
        return "redirect:/client/bookingIndex";
    }

    @PostMapping("/client/bookingCreatePhoneNumber")
    public String clientBookingCreatePhoneNumber(@RequestParam long idPhoneNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PhoneNumber phoneNumber = phoneNumberRepository.findById(idPhoneNumber).get();
        User user = userRepository.findUserByUsername(authentication.getName());
        Booking booking = new Booking(user, phoneNumber);
        bookingRepository.save(booking);
        return "redirect:/client/bookingIndex";
    }

    @GetMapping("/client/tariffSort")
    public String clientTariffIFilter(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Tariff> tariffs;
        switch (sortProperty) {
            case "Гигабайты" -> sortProperty = "GB";
            case "Минуты" -> sortProperty = "minutes";
            case "Сообщения" -> sortProperty = "SMS";
            case "Цена" -> sortProperty = "priceMount";
        }
        if (sortType)
            tariffs = tariffRepository.findAll(Sort.by(sortProperty).ascending());
        else
            tariffs = tariffRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("tariffs", tariffs);
        return "clientPage/TariffIndex";
    }

}