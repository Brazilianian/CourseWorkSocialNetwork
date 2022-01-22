package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Message;
import com.example.springWebContent.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiver(User sender, User receiver);
}
