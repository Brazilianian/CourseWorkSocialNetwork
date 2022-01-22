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

import java.util.List;

@Controller
public class PeopleController {

    @Autowired
    UserService userService;

    @GetMapping("/people")
    public String people(Model model,
                         @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);

        List<User> allUsersExceptCurrentAndFriends = userService.findAllUsersExceptUserAndFriends(user);
        userService.prepareDescriptionOfUsers(allUsersExceptCurrentAndFriends);
        if (user != null) {
            userService.prepareDescriptionOfUsers(user.getFriends());
        }

        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("friends", user.getFriends());
        }
        model.addAttribute("people", allUsersExceptCurrentAndFriends);

        return "people";
    }

    @PostMapping("/people")
    public String searchPeople (Model model,
                               @AuthenticationPrincipal User user,
                               @RequestParam String field,
                               @RequestParam(required = false) String order,
                               @RequestParam(required = false) String search) {
        user = userService.authorizeUser(user);

        List<User> allUsersExceptCurrentAndFriends = userService.findAllUsersExceptUserAndFriends(user);
        userService.prepareDescriptionOfUsers(allUsersExceptCurrentAndFriends);
        if (user != null) {
            userService.prepareDescriptionOfUsers(user.getFriends());
        }

        userService.filterUsersByUsernameLike(allUsersExceptCurrentAndFriends, search, Boolean.parseBoolean(order));
        
        userService.sortUsersByField(allUsersExceptCurrentAndFriends, field, Boolean.parseBoolean(order));
        if (user != null) {
            userService.sortUsersByField(user.getFriends(), field, Boolean.parseBoolean(order));
        }

        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("friends", user.getFriends());
        }
        model.addAttribute("people", allUsersExceptCurrentAndFriends);
        return "people";
    }
}
