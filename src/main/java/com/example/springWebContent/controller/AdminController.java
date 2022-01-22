package com.example.springWebContent.controller;

import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.exceptions.UserDoesNotExistException;
import com.example.springWebContent.repos.UserRepo;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String admin(Model model,
                        @AuthenticationPrincipal User user){
        user = userService.authorizeUser(user);

        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/admin/search")
    public String adminSearch() {
        return "redirect:/admin";
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/admin/setRole/{username}/{role}")
    public String setRole(Model model,
                          @PathVariable String username,
                          @PathVariable String role) {
        try {
            userService.setRole(username, role);
        } catch (UserDoesNotExistException e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin")
    public String search(Model model,
                               @AuthenticationPrincipal User user,
                               @RequestParam String field,
                               @RequestParam(required = false) String order,
                               @RequestParam(required = false) String search){
        user = userService.authorizeUser(user);
        model.addAttribute("user", user);

        List<User> users = userService.getUsersByUsernameLike(search);
        userService.sortUsersByField(users, field, Boolean.parseBoolean(order));

        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin";
    }
}
