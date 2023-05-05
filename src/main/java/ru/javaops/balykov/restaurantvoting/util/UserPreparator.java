package ru.javaops.balykov.restaurantvoting.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;
import ru.javaops.balykov.restaurantvoting.exception.NotFoundException;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.util.validation.EmailUniqueValidator;

@Component
@RequiredArgsConstructor
public class UserPreparator {

    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final SmartValidator validator;
    private final EmailUniqueValidator emailUniqueValidator;

    public User prepareToSave(User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }

    public User prepareToUpdate(User newUser, int id) throws BindException {
        User oldUser = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return prepareToUpdate(newUser, oldUser);
    }

    public User prepareToUpdate(User newUser, User oldUser) throws BindException {
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
    // If password is null, the User must be validated after the password is set and before it be encrypted.
    private void validate(User user) throws BindException {
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(validator, emailUniqueValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }
}