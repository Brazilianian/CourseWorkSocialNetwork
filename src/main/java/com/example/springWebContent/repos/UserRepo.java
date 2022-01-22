package com.example.springWebContent.repos;

import com.example.springWebContent.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);

    @Query("select u from User u where u.username like %:username%")
    List<User> getUsersByUsernameLike(@Param("username") String username);
}
