package com.example.springWebContent.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class Invite {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @OneToOne(cascade = CascadeType.MERGE)
    private User from;

    @Getter
    @OneToOne(cascade = CascadeType.MERGE)
    private User to;

    public Invite(User from, User to){
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }

        if(!(obj instanceof Invite)){
            return false;
        }

        Invite invite = (Invite) obj;

        return Objects.equals(from.getId(), invite.getFrom().getId())
                && Objects.equals(to.getId(), invite.getTo().getId());
    }
}