package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Notification;
import com.example.springWebContent.domain.Post;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.comparators.PostByLastMessageComparator;
import com.example.springWebContent.repos.PostRepo;
import com.example.springWebContent.repos.UserRepo;
import com.example.springWebContent.service.PostService;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/", ""})
    public String main(Model model,
                       @AuthenticationPrincipal User user){

        user = userService.authorizeUser(user);

        List<Post> posts = postRepo.findAll();
        posts.sort(new PostByLastMessageComparator());

        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "main";
    }
}