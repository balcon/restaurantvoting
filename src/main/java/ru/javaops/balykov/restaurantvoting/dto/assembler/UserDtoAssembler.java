package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.model.User;

@Component
public class UserDtoAssembler extends BaseDtoAssembler<User, UserDto> {

    @Override
    protected UserDto of(User u) {
        return new UserDto(u.getName(), u.getEmail(), u.getRoles());
    }
}