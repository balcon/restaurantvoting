package ru.javaops.balykov.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.javaops.balykov.restaurantvoting.model.User;

import java.util.Set;

import static ru.javaops.balykov.restaurantvoting.model.Role.ROLE_USER;

@UtilityClass
public class AuthenticationUtil {

    public User getAuthUser() {
        return new User(20, "User", "user@mail.ru", "password", Set.of(ROLE_USER));
    }
}