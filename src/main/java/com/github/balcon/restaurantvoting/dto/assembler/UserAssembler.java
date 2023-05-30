package com.github.balcon.restaurantvoting.dto.assembler;

import com.github.balcon.restaurantvoting.model.User;
import org.springframework.stereotype.Component;
import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.web.rest.admin.UserController;

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