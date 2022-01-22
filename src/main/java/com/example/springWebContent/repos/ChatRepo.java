package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Chat;
import com.example.springWebContent.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findByFirstUserAndSecondUser(User firstUser, User secondUser);
    List<Chat> findByFirstUserOrSecondUser(User firstUser, User SecondUser);
}
