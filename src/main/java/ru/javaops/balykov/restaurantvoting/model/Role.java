package ru.javaops.balykov.restaurantvoting.model;

import java.util.Set;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static final Set<Role> DEFAUT_ROLES = Set.of(ROLE_USER);
}