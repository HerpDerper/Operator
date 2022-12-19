package operator.controllers;

import operator.models.Role;
import operator.models.User;
import operator.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (userRepository.findUserByUsername(user.getUsername()) != null){
            bindingResult.addError(new ObjectError("username", "Данный логин уже занят"));
            model.addAttribute("errorMessage", "Данный логин уже занят");
        }
        if (bindingResult.hasErrors())
            return "user/Create";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "user/Create";
    }

}