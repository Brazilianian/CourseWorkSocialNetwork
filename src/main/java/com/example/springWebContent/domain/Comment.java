package com.example.springWebContent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    private User sender;

    private LocalDateTime dateOfSending;

    private String text;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> likes = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<MessageFile> messageFiles = new ArrayList<>();

    public Comment(){
        dateOfSending = LocalDateTime.now();
    }

    public boolean wasLikedByUser(User user){
        for (User like :
                likes) {
            if (Objects.equals(user.getId(), like.getId())) {
                return true;
            }
        }
        return false;
    }
}
