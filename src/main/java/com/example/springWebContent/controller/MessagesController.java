package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Chat;
import com.example.springWebContent.domain.comparators.ChatByLastMessageComparator;
import com.example.springWebContent.domain.Message;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.enums.TypeOfNotification;
import com.example.springWebContent.repos.UserRepo;
import com.example.springWebContent.service.MessageService;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class MessagesController{

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    MessageService messageService;

    @GetMapping("messages")
    public String messages(Model model,
                           @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);

        List<Chat> chats = messageService.getChats(user);

        chats.sort(new ChatByLastMessageComparator<Chat>());
        Collections.reverse(chats);

        model.addAttribute("chats", chats);
        model.addAttribute("user", user);
        return "messages";
    }

    @PostMapping("messages/{username}")
    public String chatPost(Model model,
                       @PathVariable String username,
                       @AuthenticationPrincipal User user) {

        user = userService.authorizeUser(user);
        model.addAttribute("user", user);

        return chatGet(model,username, user);
    }

    @GetMapping("messages/{username}")
    public String chatGet(Model model,
                       @PathVariable String username,
                       @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);

        User person = userRepo.findByUsername(username);
        if (person == null) {
            model.addAttribute(username);
            return "error";
        }
        if (Objects.equals(person.getUsername(), user.getUsername())) {
            return "redirect:";
        }

        List<Message> messages = messageService.getMessages(user, person);
        userService.removeNotifications(user, person);

        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        model.addAttribute("person", person);
        return "chat";
    }

    @PostMapping("/send-message/{username}")
    public String sendMessage(Model model,
                              @AuthenticationPrincipal User user,
                              @RequestParam(required = false) MultipartFile[] audios,
                              @RequestParam(required = false) MultipartFile[] videos,
                              @RequestParam(required = false) MultipartFile[] images,
                              @RequestParam(required = false) String text,
                              @PathVariable String username) throws IOException {
        user = userService.authorizeUser(user);

        User person = userRepo.findByUsername(username);
        if (person == null) {
            model.addAttribute(username);
            return "error";
        }
        if (Objects.equals(person.getUsername(), user.getUsername())) {
            return "redirect:";
        }

        messageService.sendMessage(user, person, text, audios, videos, images);
        userService.sendNotification(TypeOfNotification.NEW_MESSAGE, text, user, person, null);

        model.addAttribute("user", user);
        return "redirect:/messages/" + username;
    }

    @PostMapping("/messages")
    public String searchUser (Model model,
                              @AuthenticationPrincipal User user,
                              @RequestParam String search) {
        user = userService.authorizeUser(user);
        List<User> users = userService.getUsersByUsernameLike(search);
        model.addAttribute("user", users);
        return "messages";
    }
}