package com.example.springWebContent.service;

import com.example.springWebContent.domain.Chat;
import com.example.springWebContent.domain.Message;
import com.example.springWebContent.domain.MessageFile;
import com.example.springWebContent.domain.User;
import com.example.springWebContent.domain.enums.FileType;
import com.example.springWebContent.repos.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    @Autowired
    ChatRepo chatRepo;

    public List<Message> getMessages(User firstUser, User secondUser){
        Chat chat = chatRepo.findByFirstUserAndSecondUser(firstUser, secondUser);
        if(chat == null){
            chat = chatRepo.findByFirstUserAndSecondUser(secondUser, firstUser);
        }
        if(chat == null){
            chat = new Chat(firstUser, secondUser);
            chatRepo.save(chat);
        }

        return chat.getMessages();
    }

    public void sendMessage(User sender, User receiver, String text, MultipartFile[] audios, MultipartFile[] videos, MultipartFile[] images) throws IOException {
        Chat chat = chatRepo.findByFirstUserAndSecondUser(sender, receiver);
        if(chat == null){
            chat = chatRepo.findByFirstUserAndSecondUser(receiver, sender);
        }
        Message message = new Message(sender, receiver, LocalDateTime.now(), text);
        try{
            Message bdMessage = chat.getMessages().get(chat.getMessages().size() - 1);
            if(bdMessage.getDateOfSending().plusHours(3).isBefore(LocalDateTime.now())){
                message.setDifferenceMoreThanThreeHours(true);
            }
        } catch (Exception e) {
            message.setDifferenceMoreThanThreeHours(true);
        }

        if(audios != null){
            for (MultipartFile audio :
                    audios) {
                if(!Objects.requireNonNull(audio.getOriginalFilename()).isEmpty()) {
                    if(message.getMessageFiles() == null){
                        message.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(audio.getBytes()), audio.getOriginalFilename(), FileType.AUDIO);
                    message.getMessageFiles().add(messageFile);
                }
            }
        }

        if(videos != null) {
            for (MultipartFile video :
                    videos) {
                if (!Objects.requireNonNull(video.getOriginalFilename()).isEmpty()) {
                    if(message.getMessageFiles() == null){
                        message.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(video.getBytes()), video.getOriginalFilename(), FileType.VIDEO);
                    message.getMessageFiles().add(messageFile);
                }
            }
        }

        if(images != null) {
            for (MultipartFile image :
                    images) {
                if (!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
                    if(message.getMessageFiles() == null){
                        message.setMessageFiles(new ArrayList<>());
                    }
                    MessageFile messageFile = new MessageFile(Base64.getEncoder().encodeToString(image.getBytes()), image.getOriginalFilename(), FileType.IMAGE);
                    message.getMessageFiles().add(messageFile);
                }
            }
        }

        chat.getMessages().add(message);
        chatRepo.save(chat);
    }

    public List<Chat> getChats(User user) {
        return chatRepo.findByFirstUserOrSecondUser(user, user);
    }
}
