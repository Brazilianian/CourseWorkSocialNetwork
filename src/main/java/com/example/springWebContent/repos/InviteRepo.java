package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepo extends JpaRepository<Invite, Long> {
}
