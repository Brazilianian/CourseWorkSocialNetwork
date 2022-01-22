package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Notification;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.enums.TypeOfNotification;
import com.example.springWebContent.domain.exceptions.UserDoesNotExistException;
import com.example.springWebContent.repos.UserRepo;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FriendsController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/send-invite/{username}")
    public String sendInvite(Model model,
                            @PathVariable String username,
                            @AuthenticationPrincipal User user){
        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        userService.sendInvite(user, person);
        userService.sendNotification(TypeOfNotification.FRIEND_REQUEST, "", user, person, null);

        return "redirect:/profile/" + username;
    }

    @PostMapping("/remove-invite/{username}")
    public String removeInvite(Model model,
                               @AuthenticationPrincipal User user,
                               @PathVariable String username){
        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        userService.removeInvite(user, person);
        return "redirect:/profile/" + username;
    }

    @PostMapping("/remove-friend/{username}")
    public String removeFriend(Model model,
                               @AuthenticationPrincipal User user,
                               @PathVariable String username){
        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        userService.removeFriend(user, person);
        return "redirect:/profile/" + username;
    }

    @PostMapping("/confirm-invite/{username}")
    public String confirmInvite(Model model,
                               @AuthenticationPrincipal User user,
                               @PathVariable String username){
        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        userService.confirmInvite(user, person);
        return "redirect:/profile/" + username;
    }

    @PostMapping("/decline-invite/{username}")
    public String declineInvite(Model model,
                               @AuthenticationPrincipal User user,
                               @PathVariable String username){
        user = userService.authorizeUser(user);
        User person;
        try {
            person = userService.getUserByUsername(username);
        } catch (UserDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        userService.declineInvite(user, person);
        return "redirect:/profile/" + username;
    }
}
