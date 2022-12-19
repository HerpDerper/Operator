package operator.controllers;

import operator.models.Employee;
import operator.models.PassportData;
import operator.models.Role;
import operator.models.User;
import operator.repositories.EmployeeRepository;
import operator.repositories.PassportDataRepository;
import operator.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final PassportDataRepository passportDataRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeRepository employeeRepository,
                              UserRepository userRepository,
                              PassportDataRepository passportDataRepository,
                              PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passportDataRepository = passportDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/employee/index")
    public String employeeIndex(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee/Index";
    }

    @GetMapping("/employee/filter")
    public String employeeFilter(@RequestParam(required = false) String email, Model model) {
        Iterable<Employee> employees;
        if (email != null && !email.equals(""))
            employees = employeeRepository.findByEmailContains(email);
        else
            employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee/Index";
    }

    @PostMapping("/employee/editEmployee")
    public String employeeEdit(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResultEmployee,
                               @ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                               @ModelAttribute("passportData") @Valid PassportData passportData, BindingResult bindingResultPassport, Model model) {
        if (userRepository.findUserByUsername(user.getUsername()) != null &&
                !userRepository.findUserByUsername(user.getUsername()).getIdUser().equals(user.getIdUser())) {
            bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
            model.addAttribute("errorMessageUsername", "Данный логин уже занят");
        }
        user.setPassword(user.getPassword());
        return employeeDataManipulation("employee/Edit", employee, bindingResultEmployee, user, bindingResultUser, passportData, bindingResultPassport, model);
    }


    @PostMapping("/employee/edit")
    public String employeeEdit(@RequestParam long idEmployee, Model model) {
        Employee employee = employeeRepository.findById(idEmployee).get();
        User user = userRepository.findById(employee.getUser().getIdUser()).get();
        PassportData passportData = passportDataRepository.findById(employee.getPassportData().getIdPassportData()).get();
        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        model.addAttribute("passportData", passportData);
        return "employee/Edit";
    }

    @PostMapping("/employee/delete")
    public String employeeDelete(@RequestParam long idEmployee) {
        Employee employee = employeeRepository.findById(idEmployee).get();
        User user = userRepository.findById(employee.getUser().getIdUser()).get();
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/employee/index";
    }

    @PostMapping("/employee/create")
    public String employeeCreate(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResultEmployee,
                                 @ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                                 @ModelAttribute("passportData") @Valid PassportData passportData, BindingResult bindingResultPassport, Model model) {
        if (passportDataRepository.findPassportDataByPassportSeriesAndPassportNumber(passportData.getPassportSeries(), passportData.getPassportNumber()) != null)
            passportData = passportDataRepository.findPassportDataByPassportSeriesAndPassportNumber(
                    passportData.getPassportSeries(),
                    passportData.getPassportNumber());
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
            model.addAttribute("errorMessageUsername", "Данный логин уже занят");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return employeeDataManipulation("employee/Create", employee, bindingResultEmployee, user, bindingResultUser, passportData, bindingResultPassport, model);
    }

    @GetMapping("/employee/create")
    public String employeeCreate(@ModelAttribute("employee") Employee employee,
                                 @ModelAttribute("user") User user,
                                 @ModelAttribute("passportData") PassportData passportData) {
        return "employee/Create";
    }

    @PostMapping("/employee/details")
    public String employeeDetails(@RequestParam long idEmployee, Model model) {
        model.addAttribute("employee", employeeRepository.findById(idEmployee).get());
        return "employee/Details";
    }


    private String employeeDataManipulation(String redirectTo,
                                            @Valid Employee employee, BindingResult bindingResultEmployee,
                                            @Valid User user, BindingResult bindingResultUser,
                                            @Valid PassportData passportData, BindingResult bindingResultPassport, Model model) {
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
        if (bindingResultPassport.hasErrors() || bindingResultUser.hasErrors() || bindingResultEmployee.hasErrors())
            return redirectTo;
        user.setRoles(Collections.singleton(Role.SELLER));
        user.setActive(true);
        userRepository.save(user);
        passportDataRepository.save(passportData);
        employee.setUser(user);
        employee.setPassportData(passportData);
        employeeRepository.save(employee);
        return "redirect:/employee/index";
    }

}