package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Notification;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.exceptions.UserDoesNotExistException;
import com.example.springWebContent.repos.UserRepo;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);
        model.addAttribute(user);

        try {
            if (user.getDateOfBirth() != null) {
                String date = userService.getFormatterDateOfUser(user, "yyyy-MM-dd");
                model.addAttribute("date", date);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "profile";
    }

    @GetMapping("/profile/{username}")
    public String userProfile(Model model,
                              @AuthenticationPrincipal User user,
                              @PathVariable String username){

        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e) {
            model.addAttribute(username);
            return "error";
        }

        if (user != null && Objects.equals(person.getUsername(), user.getUsername())) {
            return "redirect:";
        }
        if (person.getDateOfBirth() != null) {
            String date = userService.getFormatterDateOfUser(person, "dd.MM.yyyy");
            model.addAttribute("date", date);
        }

        if(user != null) {
            model.addAttribute("isFriend", userService.isFriend(user, person));
            model.addAttribute("isInviter", userService.isInviter(user, person));
            model.addAttribute("wasInvited", userService.wasInvited(user, person));
        } else{
            model.addAttribute("isFriend", false);
            model.addAttribute("isInviter", false);
            model.addAttribute("wasInvited", false);
        }

        model.addAttribute("isAvailableInfo", userService.isAvailableInfo(user, person));
        model.addAttribute("isAvailableMessaging", userService.isAvailableMessaging(user, person));
        model.addAttribute("user", user);
        model.addAttribute("person", person);
        return "person";
    }

    @PostMapping("/update-profile")
    public String updateProfile(Model model,
                                @AuthenticationPrincipal User user,
                                @RequestParam String username,
                                @RequestParam String name,
                                @RequestParam String surname,
                                @RequestParam(required = false) String description,
                                @RequestParam("dateOfBirth") String date,
                                @RequestParam(required = false) MultipartFile image) {

        user = userService.authorizeUser(user);
        try {
            LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            userService.updateProfile(user, username, name, surname,
                    description, dateOfBirth, image);
            userRepo.save(user);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "redirect:/profile";
    }
}