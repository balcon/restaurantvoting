package com.github.balcon.restaurantvoting.util.validation;

import com.github.balcon.restaurantvoting.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;

/**
 * Manual validation used because the user can be updated without changing his password.
 * In @RequestBody the controller can get json without password.
 * Also, password must be validated before encryption.
 */

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final SmartValidator baseValidator;
    private final EmailUniqueValidator emailUniqueValidator;

    public void validate(User user) throws BindException {
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(baseValidator, emailUniqueValidator);
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }
}