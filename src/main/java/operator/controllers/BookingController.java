package operator.controllers;

import operator.models.*;
import operator.repositories.BookingRepository;
import operator.repositories.CartRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('SELLER')")
@Controller
public class BookingController {

    private final BookingRepository bookingRepository;

    private final CartRepository cartRepository;

    public BookingController(BookingRepository bookingRepository,
                             CartRepository cartRepository) {
        this.bookingRepository = bookingRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/booking/index")
    public String bookingIndex(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        return "booking/Index";
    }

    @PostMapping("/booking/filter")
    public String bookingFilter(@RequestParam(required = false) String username, Model model) {
        Iterable<Booking> bookings;
        if (username != null && !username.equals(""))
            bookings = bookingRepository.findByUserUsername(username);
        else
            bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "booking/Index";
    }

    @PostMapping("/booking/addToCart")
    public String bookingAddToCart(@RequestParam long idBooking) {
        Booking booking = bookingRepository.findById(idBooking).get();
        Product product = booking.getProduct();
        Cart cart = new Cart(product, 1);
        cartRepository.save(cart);
        bookingRepository.delete(booking);
        return "redirect:/cart/index";
    }

    @PostMapping("/booking/registrationClientNumber")
    public String bookingRegistrationClientNumber(@RequestParam long idBooking, Model model) {
        Booking booking = bookingRepository.findById(idBooking).get();
        PhoneNumber phoneNumber = booking.getPhoneNumber();
        model.addAttribute("phoneNumberNumber", phoneNumber.getNumber());
        model.addAttribute("passportData", new PassportData());
        model.addAttribute("client", new Client());
        model.addAttribute("clientNumber", new ClientNumber());
        return "clientNumber/Create";
    }

    @PostMapping("/booking/delete")
    public String bookingDelete(@RequestParam long idBooking) {
        Booking booking = bookingRepository.findById(idBooking).get();
        bookingRepository.delete(booking);
        return "redirect:/booking/index";
    }

}