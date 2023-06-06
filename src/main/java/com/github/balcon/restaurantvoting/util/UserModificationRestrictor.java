package com.github.balcon.restaurantvoting.util;

import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserModificationRestrictor {
    private static final String ALLOWED_PROFILE = "test";
    private final Environment environment;

    public void check(int id) {
        List<String> activeProfiles = List.of(environment.getActiveProfiles());
        if (!activeProfiles.contains(ALLOWED_PROFILE) && id < BaseEntity.START_SEQ) {
            throw new IllegalRequestException("Default users can't be modified or delete");
        }
    }
}