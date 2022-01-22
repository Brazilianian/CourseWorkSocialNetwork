package com.example.springWebContent.controller;

import com.example.springWebContent.domain.User;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    @Autowired
    UserService userService;

    @GetMapping("/settings")
    public String settings(Model model,
                           @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);
        model.addAttribute("user", user);

        return "settings";
    }

    @PostMapping("/update-settings")
    public String updateSettings(Model model,
                                 @AuthenticationPrincipal User user,
                                 @RequestParam(required = false) boolean isMessagingOnlyWithFriends,
                                 @RequestParam(required = false) boolean isPrivateProfile,
                                 @RequestParam(required = false) boolean isGettingNotificationsOnlyFromFriends,
                                 @RequestParam(required = false) boolean isGettingNotificationsFromSite) {

        user = userService.authorizeUser(user);
        userService.updateSettings(user, isMessagingOnlyWithFriends, isPrivateProfile, isGettingNotificationsOnlyFromFriends, isGettingNotificationsFromSite);
        return "redirect:settings";
    }
}
