package com.fivesysdev.Fiveogram.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER(1L, "user"),
    ADMIN(2L, "admin");

    private final long code;
    private final String authority;

    Role(long code, String authority) {
        this.code = code;
        this.authority = authority;
    }

    public long getCode() {
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
