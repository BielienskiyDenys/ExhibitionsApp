package org.example.exhibitionsapp.controller;

import org.example.exhibitionsapp.entity.Role;
import org.example.exhibitionsapp.entity.User;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class PagesController {
    @RequestMapping("/")
    public String mainPage() {return redirectToBaseIfAutorizedElseDirectTo("main");}

    @RequestMapping("/main")
    public String mainPage2() {
        return redirectToBaseIfAutorizedElseDirectTo("main");
    }

    @RequestMapping("/user_base")
    public String userBasePage() {return "user_base";}

    @RequestMapping("/admin_base")
    public String adminBasePage() {
        return "admin_base";
    }

    @RequestMapping("/login")
    public String loginPage(Model model) {return redirectToBaseIfAutorizedElseDirectTo("login"); }

    @RequestMapping("/registration")
    public String registrationPage(Locale locale) {return redirectToBaseIfAutorizedElseDirectTo("registration");}

    private String redirectToBaseIfAutorizedElseDirectTo(String direction) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Locale locale = LocaleContextHolder.getLocale();
        if (principal == null) {
            return direction;
        }
        try {
            User user = (User) principal;
            if (user.getRoles().contains(Role.ADMIN)) {


                return "redirect:admin_base?lang="+locale;
            }
            return "redirect:user_base?lang="+locale;
        } catch (ClassCastException ex) {
            return direction;
        }
    }
}
