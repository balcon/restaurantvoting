package ru.javaops.balykov.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

@UtilityClass
public class UserUtil {
    public User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        user.setRoles(Role.DEFAULT_ROLES);
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public User prepareToUpdate(User user, int id, PasswordEncoder encoder, UserRepository repository) {
        User oldUser = repository.findById(id).orElseThrow(RuntimeException::new); // TODO: 25.04.2023 throw legal excepltion
        return prepareToUpdate(user, oldUser, encoder);
    }

    public User prepareToUpdate(User user, User oldUser, PasswordEncoder encoder) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(oldUser.getPassword());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}