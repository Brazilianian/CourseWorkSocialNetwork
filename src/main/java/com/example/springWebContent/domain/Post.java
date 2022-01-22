package com.example.springWebContent.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MyImage> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<MyImage> textFile = new ArrayList<>();

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> likes = new HashSet<>();

    @Lob
    private String text;

    private boolean isHiddenFromUsers;

    @OneToOne(cascade = CascadeType.MERGE)
    private User user;

    private Calendar dateOfPosting;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public boolean wasLikedByUser(User user){
        for (User like :
                likes) {
            if(Objects.equals(like.getId(), user.getId())){
                return true;
            }
        }
        return false;
    }

    public Post(){}

    @Transient
    public boolean getHiddenFromUsers() {
        return isHiddenFromUsers;
    }
}
