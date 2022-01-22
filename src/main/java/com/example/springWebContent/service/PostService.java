package com.example.springWebContent.service;

import com.example.springWebContent.domain.*;
import com.example.springWebContent.domain.enums.FileType;
import com.example.springWebContent.domain.exceptions.CommentDoesNotExistException;
import com.example.springWebContent.domain.exceptions.PostDoesNotExistException;
import com.example.springWebContent.repos.CommentRepo;
import com.example.springWebContent.repos.PostRepo;
import com.example.springWebContent.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Objects;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    public Post createAndSavePost(User user, String name, String text, MultipartFile[] images) throws IOException {
        Post post = new Post();

        if(images != null && !Objects.equals(images[0].getOriginalFilename(), "")) {
            if(post.getImages() == null){
                post.setImages(new ArrayList<>());
            }
            for (MultipartFile image :
                    images) {
                post.getImages().add(new MyImage(post.getImages().size(), Base64.getEncoder().encodeToString(image.getBytes())));
            }
        }

        post.setName(name);
        post.setText(text);
        post.setUser(user);
        post.setDateOfPosting(Calendar.getInstance());

        postRepo.save(post);
        return post;
    }

    public void sendComment(Post post, String text, MultipartFile[] images, MultipartFile[] videos, MultipartFile[] audios, User user) throws IOException {
        post = postRepo.getById(post.getId());
        user = userRepo.getById(user.getId());

        Comment comment = new Comment();
        comment.setText(text);
        comment.setSender(user);

        if(audios != null){
            for (MultipartFile audio :
                    audios) {
                if(!Objects.requireNonNull(audio.getOriginalFilename()).isEmpty()) {
                    if(post.getComments() == null){
                        post.setComments(new ArrayList<>());
                    }
                    if(comment.getMessageFiles() == null){
                        comment.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(audio.getBytes()), audio.getOriginalFilename(), FileType.AUDIO);
                    comment.getMessageFiles().add(messageFile);
                }
            }
        }

        if(videos != null) {
            for (MultipartFile video :
                    videos) {
                if (!Objects.requireNonNull(video.getOriginalFilename()).isEmpty()) {
                    if(post.getComments() == null){
                        post.setComments(new ArrayList<>());
                    }
                    if(comment.getMessageFiles() == null){
                        comment.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(video.getBytes()), video.getOriginalFilename(), FileType.VIDEO);
                    comment.getMessageFiles().add(messageFile);
                }
            }
        }

        if(images != null) {
            for (MultipartFile image :
                    images) {
                if (!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
                    if(post.getComments() == null){
                        post.setComments(new ArrayList<>());
                    }
                    if(comment.getMessageFiles() == null){
                        comment.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(image.getBytes()), image.getOriginalFilename(), FileType.IMAGE);
                    comment.getMessageFiles().add(messageFile);
                }
            }
        }

        post.getComments().add(comment);
        postRepo.save(post);
    }

    public void like(User user, Post post){
        for (User like :
                post.getLikes()) {
            if (Objects.equals(like.getId(), user.getId())) {
                post.getLikes().remove(like);
                postRepo.save(post);
                return;
            }
        }
        post.getLikes().add(user);
        postRepo.save(post);
    }

    public void like(User user, Comment comment){
        for (User like :
                comment.getLikes()) {
            if(Objects.equals(like.getId(), user.getId())){
                comment.getLikes().remove(like);
                commentRepo.save(comment);
                return;
            }
        }
        comment.getLikes().add(user);
        commentRepo.save(comment);
    }

    public Comment getCommentById(String commentId) throws CommentDoesNotExistException {
        Long id;
        try {
            id = Long.parseLong(commentId);
        } catch (NumberFormatException e){
            throw new CommentDoesNotExistException("Id of comment does not contain a parsable long");
        }

        boolean exist = commentRepo.existsById(id);
        if(!exist){
            throw new CommentDoesNotExistException("Comment with id " + commentId + " does not exist");
        }
        return commentRepo.getById(id);
    }

    public Post getPostById(String postId) throws PostDoesNotExistException {
        Long id;
        try {
            id = Long.parseLong(postId);
        } catch (NumberFormatException e){
            throw new PostDoesNotExistException("Id of post does not contain a parsable long");
        }

        boolean exist = postRepo.existsById(id);
        if(!exist){
            throw new PostDoesNotExistException("Post with id " + id + " does not exist");
        }

        return postRepo.getById(id);
    }

    public void hidePost(Post post) {
        post.setHiddenFromUsers(!post.getHiddenFromUsers());
        postRepo.save(post);
    }

    public void deletePost(Post post) {
        postRepo.delete(post);
    }
}
