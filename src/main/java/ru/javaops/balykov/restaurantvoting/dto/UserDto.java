package ru.javaops.balykov.restaurantvoting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ru.javaops.balykov.restaurantvoting.model.Role;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@Relation(collectionRelation = UserDto.COLLECTION)
public class UserDto extends RepresentationModel<UserDto> {
    public static final String COLLECTION = "users";

    private final String name;
    private final String email;
    private final Set<Role> roles;
}