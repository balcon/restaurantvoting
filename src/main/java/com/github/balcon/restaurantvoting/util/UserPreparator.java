package com.github.balcon.restaurantvoting.util;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.validation.EmailUniqueValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;

/**
 * Use for preparing User before create or update.
 * Set default roles, encode password, validate fields etc.
 */

@Component
@RequiredArgsConstructor
public class UserPreparator {

    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final SmartValidator baseValidator;
    private final EmailUniqueValidator emailUniqueValidator;

    public User prepareToCreate(User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }

    public User prepareToUpdate(User newUser, int id) throws BindException {
        User oldUser = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
            newUser.setPassword(oldUser.getPassword());
            validate(newUser);
        } else {
            validate(newUser);
            newUser.setPassword(encoder.encode(newUser.getPassword()));
        }
        newUser.setEmail(newUser.getEmail().toLowerCase());
        return newUser;
    }

    // This is manual validation, because the user can be updated without changing his password.
    // In @RequestBody controller can get json without password.
    // If password is null, the User must be validated after the password is set and before password be encrypted.
    private void validate(User user) throws BindException {
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(baseValidator, emailUniqueValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }
}