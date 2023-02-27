package com.fivesysdev.Fiveogram.roles;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER(1L, "user"),
    ADMIN(2L, "admin");

    private final Long code;
    private final String authority;

    Role(Long code, String authority) {
        this.code = code;
        this.authority = authority;
    }

    public Long getCode() {
        return code;
    }
    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
