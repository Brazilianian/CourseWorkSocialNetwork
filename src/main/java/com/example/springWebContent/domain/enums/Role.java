package com.example.springWebContent.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    OWNER ,ADMIN, USER, NONE;

    @Override
    public String getAuthority() {
        return name();
    }
}
