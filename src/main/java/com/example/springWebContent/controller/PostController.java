package com.example.springWebContent.controller;

import com.example.springWebContent.domain.Comment;
import com.example.springWebContent.domain.Post;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.enums.TypeOfNotification;
import com.example.springWebContent.domain.exceptions.CommentDoesNotExistException;
import com.example.springWebContent.domain.exceptions.PostDoesNotExistException;
import com.example.springWebContent.service.PostService;
import com.example.springWebContent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/loadpost")
    public String uploadImage(@AuthenticationPrincipal User user,
                              @RequestParam String name,
                              @RequestParam String text,
                              @RequestParam(required = false) String isNotifyUsers,
                              @RequestParam(required = false) MultipartFile[] images) throws IOException {

        user = userService.authorizeUser(user);
        Post post = postService.createAndSavePost(user, name, text, images);
        if (isNotifyUsers != null) {
            userService.sendNotification(TypeOfNotification.SITE, text, user, null, post);
        }

        return "redirect:/";
    }

    @GetMapping("/posting")
    public String posting(@AuthenticationPrincipal User user,
                          Model model) {

        user = userService.authorizeUser(user);
        model.addAttribute("user", user);

        return "posting";
    }

    @GetMapping("/post/{id}")
    public String post(Model model,
                       @AuthenticationPrincipal User user,
                       @PathVariable String id){
        user = userService.authorizeUser(user);

        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e){
            model.addAttribute("user", user);
            model.addAttribute("message", "Post doesn't exist");
            return "error";
        }

        model.addAttribute("post", post);
        model.addAttribute("user", user);
        return "post";
    }

    @PostMapping("/post/{id}/send-comment")
    public String sendComment(Model model,
                              @AuthenticationPrincipal User user,
                              @PathVariable String id,
                              @RequestParam(required = false) String text,
                              @RequestParam(required = false) MultipartFile[] images,
                              @RequestParam(required = false) MultipartFile[] videos,
                              @RequestParam(required = false) MultipartFile[] audios) throws IOException {
        user = userService.authorizeUser(user);

        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e){
            model.addAttribute("user", user);
            model.addAttribute("message", "Post doesn't exist");
            return "error";
        }

        postService.sendComment(post, text, images, videos, audios, user);

        model.addAttribute("user", user);
        return "redirect:/post/" + id;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/post/{id}/like")
    public String like(Model model,
                       @AuthenticationPrincipal User user,
                       @PathVariable String id){

        user = userService.authorizeUser(user);
        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("user", user);
            return "error";
        }
        postService.like(user, post);

        model.addAttribute("user", user);
        return "redirect:/post/" + id;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/post/{postId}/comment/{commentId}/like")
    public String like(Model model,
                       @AuthenticationPrincipal User user,
                       @PathVariable String postId,
                       @PathVariable String commentId){

        user = userService.authorizeUser(user);
        Post post;
        try {
            post = postService.getPostById(postId);
        } catch (PostDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("user", user);
            return "error";
        }

        Comment comment;
        try {
            comment = postService.getCommentById(commentId);
        } catch (CommentDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("user", user);
            return "error";
        }
        postService.like(user, comment);

        model.addAttribute("user", user);
        return "redirect:/post/" + postId;
    }

    @ResponseStatus (HttpStatus.NO_CONTENT)
    @PostMapping ("/post/{id}/like-main")
    public String likeMain(Model model,
                       @AuthenticationPrincipal User user,
                       @PathVariable String id){

        user = userService.authorizeUser(user);
        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        postService.like(user, post);

        model.addAttribute("user", user);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/post/{id}/hide-main")
    public String hide (Model model,
                        @PathVariable String id) {
        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        postService.hidePost(post);
        return "redirect:/post/" + id;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/delete-post/{id}")
    public String deletePost(Model model,
                             @PathVariable String id){
        Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostDoesNotExistException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        postService.deletePost(post);
        return "redirect:/post/" + id;
    }
}
