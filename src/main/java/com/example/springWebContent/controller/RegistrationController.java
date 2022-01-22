package com.example.springWebContent.controller;

import com.example.springWebContent.domain.exceptions.UserDoesNotExistException;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Model model,
                          @RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String password2,
                          @RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam("dateOfBirth") String date) {
        if(!password.equals(password2)){
            model.addAttribute("message", "Паролі мають співпадати");
            return "registration";
        }

        try {
            userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            try {
                LocalDate dateOfBirth = LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                userService.createAndSaveNewUser(username, password, name, surname, dateOfBirth);
            } catch (DateTimeParseException d) {
                System.out.println(d.getMessage());
            }
            return "redirect:/login";
        }

        return "registration";
    }
}
