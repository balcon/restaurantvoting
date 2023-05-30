package com.github.balcon.restaurantvoting.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    public static final Set<Role> DEFAULT_ROLES = Set.of(USER);

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}