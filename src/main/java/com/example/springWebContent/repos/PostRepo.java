package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
