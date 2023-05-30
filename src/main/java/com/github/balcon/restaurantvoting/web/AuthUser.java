package com.github.balcon.restaurantvoting.web;


import com.github.balcon.restaurantvoting.model.User;
import lombok.Getter;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    @Getter
    private final User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public Integer id() {
        return user.id();
    }
}