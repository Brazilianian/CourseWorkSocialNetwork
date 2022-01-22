package com.example.springWebContent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class Message implements Comparable<Message>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;

    private LocalDateTime dateOfSending;

    @Lob
    private String text;

    private boolean isDifferenceMoreThanThreeHours;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageFile> messageFiles = new ArrayList<>();

    public Message(User sender, User receiver, LocalDateTime dateOfSending, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.dateOfSending = dateOfSending;
        this.text = text;
        this.messageFiles = new ArrayList<>();
    }

    @Override
    public int compareTo(@NotNull Message o) {
        if(o.getDateOfSending().isAfter(dateOfSending)){
            return -1;
        } else if(o.getDateOfSending().isBefore(dateOfSending)){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return isDifferenceMoreThanThreeHours == message.isDifferenceMoreThanThreeHours && Objects.equals(id, message.id) && Objects.equals(sender, message.sender)
                && Objects.equals(receiver, message.receiver) && Objects.equals(dateOfSending, message.dateOfSending) && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, dateOfSending, text, isDifferenceMoreThanThreeHours);
    }
}
