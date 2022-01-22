package com.example.springWebContent.repos;

import com.example.springWebContent.domain.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepo extends JpaRepository<Settings, Long> {
}
