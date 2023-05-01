package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController;

import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NoUserDtoAssembler implements RepresentationModelAssembler<User, UserDto> {

    @Override
    public UserDto toModel(User user) {
        UserDto model = toDto(user);
        model.add(linkTo(methodOn(UserController.class).getAll(Pageable.unpaged())).withRel("collection"));
        return model;
    }

    @Override
    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends User> entities) {
        ArrayList<UserDto> userDtos = new ArrayList<>();
        entities.forEach(e -> userDtos.add(toDto(e)));
        CollectionModel<UserDto> models = CollectionModel.of(userDtos);
        models.add(linkTo(methodOn(UserController.class).getAll(Pageable.unpaged())).withSelfRel());
        return models;
    }

    private UserDto toDto(User user) {
        UserDto model = new UserDto(user.getName(), user.getEmail(), user.getRoles());
        model.add(linkTo(methodOn(UserController.class).getById(Objects.requireNonNull(user.getId()))).withSelfRel());
        return model;
    }
}