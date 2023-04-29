package ru.javaops.balykov.restaurantvoting.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailUniqueValidator implements Validator {

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        repository.findByEmailIgnoreCase(user.getEmail())
                .ifPresent(u -> {
                    if (request.getMethod().equals("PUT") &&
                            Objects.equals(u.getId(), user.getId())) {
                        return;
                    }
                    errors.rejectValue("email", "", "Value already exists");
                });
    }
}