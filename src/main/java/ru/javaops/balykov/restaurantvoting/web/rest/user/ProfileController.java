package ru.javaops.balykov.restaurantvoting.web.rest.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.AuthUser;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(ProfileController.BASE_URL)
@Slf4j
public class ProfileController extends BaseController<User> {
    protected static final String BASE_URL = API_URL + "/user/profile";

    public ProfileController(UserRepository repository) {
        super(repository, log);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        return super.create(user);
    }

    @GetMapping
    public User getAuth(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody User user,
                                    @AuthenticationPrincipal AuthUser authUser) { // TODO: 09.04.2023 Password change?
        // TODO: 13.04.2023 Roles?
        return super.update(authUser.id(), user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }
}