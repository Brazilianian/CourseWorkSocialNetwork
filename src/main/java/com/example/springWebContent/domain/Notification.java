package com.example.springWebContent.domain;

import com.example.springWebContent.domain.enums.TypeOfNotification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeOfNotification typeOfNotification;

    private String text;

    @OneToOne(cascade = CascadeType.MERGE)
    private User receiver;

    @OneToOne(cascade = CascadeType.MERGE)
    private User sender;

    private int countOfFiles;

    @OneToOne(cascade = CascadeType.MERGE)
    private Post post;

    public int getCountOfFiles() {
        return countOfFiles;
    }

    public void setCountOfFiles(int countOfFiles) {
        this.countOfFiles = countOfFiles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfNotification getTypeOfNotification() {
        return typeOfNotification;
    }

    public void setTypeOfNotification(TypeOfNotification typeOfNotification) {
        this.typeOfNotification = typeOfNotification;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Notification(TypeOfNotification typeOfNotification, String text, User sender, User receiver){
        this.typeOfNotification = typeOfNotification;
        this.text = text;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Notification(TypeOfNotification typeOfNotification, User sender, User receiver){
        this.typeOfNotification = typeOfNotification;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Notification() {}

    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(!(obj instanceof Notification)){
            return false;
        }

        Notification notification = (Notification) obj;

        return Objects.equals(typeOfNotification, notification.typeOfNotification)
                && Objects.equals(sender, notification.sender)
                && Objects.equals(receiver, notification.receiver);
    }
}
