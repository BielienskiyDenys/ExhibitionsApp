package org.example.exhibitionsapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exhibitionsapp.entity.Role;
import org.example.exhibitionsapp.entity.User;
import org.example.exhibitionsapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;
@Slf4j
@Controller
public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model, @RequestParam String username, @RequestParam String password, @RequestParam String nameNative, @RequestParam String email, @RequestParam String role, @RequestParam String lang) {
        user.setEmail(email);
        user.setNameNative(nameNative);

        user.setRoles(Collections.singleton(Role.valueOf(role)));
        if(userService.addNewUser(user)) {
            logger.info("New user registered: " + user);
            return "redirect:/login?lang="+lang;
        }
        model.put("registrationMessage", "Данный email уже зарегистрирован.");
        logger.debug("Attemp to register user with existing email:" + email);
        return "registration";

    }

}
