package ru.javaops.balykov.restaurantvoting.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.exception.IllegalRequestException;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserUpdateRestrictor {
    private static final String ALLOWED_PROFILE = "test";
    private final Environment environment;

    public void check(int id) {
        List<String> activeProfiles = List.of(environment.getActiveProfiles());
        if (!activeProfiles.contains(ALLOWED_PROFILE) && id < BaseEntity.START_SEQ) {
            throw new IllegalRequestException("Default users can't be modified");
        }
    }
}