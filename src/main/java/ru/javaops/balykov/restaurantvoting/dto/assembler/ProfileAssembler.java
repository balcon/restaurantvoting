package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.web.AuthUser;
import ru.javaops.balykov.restaurantvoting.web.rest.user.ProfileController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProfileAssembler implements RepresentationModelAssembler<User, UserDto> {
    @Override
    public UserDto toModel(User u) {
        UserDto dto = new UserDto(u.getName(), u.getEmail(), u.getRoles());
        dto.add(linkTo(methodOn(ProfileController.class).getAuth(new AuthUser(u))).withSelfRel());
        return dto;
    }
}