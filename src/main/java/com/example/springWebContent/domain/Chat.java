package com.example.springWebContent.domain;

import com.example.springWebContent.domain.comparators.MessageByLastMessageComparator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    private User firstUser;

    @OneToOne(cascade = CascadeType.MERGE)
    private User secondUser;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    public Chat(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        messages = new ArrayList<>();
    }

    public Message getLastMessage(){
        try {
            messages.sort(new MessageByLastMessageComparator<Message>());
            return messages.get(messages.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
