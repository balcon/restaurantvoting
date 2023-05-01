package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController;

@Component
public class UserAssembler extends BaseAssembler<User, UserDto> {

    protected UserAssembler() {
        super(UserController.class);
    }

    @Override
    protected UserDto of(User u) {
        return new UserDto(u.getName(), u.getEmail(), u.getRoles());
    }
}