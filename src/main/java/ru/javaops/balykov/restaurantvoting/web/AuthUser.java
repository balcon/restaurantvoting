package ru.javaops.balykov.restaurantvoting.web;


import lombok.Getter;
import ru.javaops.balykov.restaurantvoting.model.User;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    @Getter
    private final User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public Integer id() {
        return user.getId();
    }
}