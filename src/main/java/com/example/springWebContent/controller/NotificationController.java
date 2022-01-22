package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Notification;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.repos.NotificationRepo;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class NotificationController {

    @Autowired
    private UserService userService;

    @GetMapping("/notifications")
    public String notifications (Model model,
                                @AuthenticationPrincipal User user) {

        user = userService.authorizeUser(user);
        model.addAttribute(user);

        Set<Notification> notifications = user.getNotifications();
        model.addAttribute("notifications", notifications);

        return "notifications";
    }

    @PostMapping("/delete-notification/{id}")
    public String deleteNotification (Model model,
                                     @AuthenticationPrincipal User user,
                                     @PathVariable String id) {
        user = userService.authorizeUser(user);

        userService.deleteNotification(user, id);
        return "redirect:/notifications";
    }
}
// FIXME: 03.11.2021 link on post notify new post of site