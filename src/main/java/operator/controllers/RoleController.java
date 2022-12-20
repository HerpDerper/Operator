package operator.controllers;

import operator.services.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleController {

    @PostMapping("/main")
    public String getMainPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (new RoleService().adminAccess(auth)) return "redirect:/employee/index";
        else if (new RoleService().sellerAccess(auth)) return "redirect:/clientNumber/create";
        else return "redirect:/clientPage/tariffIndex";
    }

}