package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> getByReceiverId(Long id);
}
