package ru.javaops.balykov.restaurantvoting.web.rest.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.util.AuthenticationUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import java.util.Objects;

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
    public ResponseEntity<User> create(@RequestBody User user) {
        return super.create(user);
    }

    @GetMapping
    public ResponseEntity<User> getProfile() {
        int id = Objects.requireNonNull(AuthenticationUtil.getAuthUser().getId());
        return super.getById(id);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) { // TODO: 09.04.2023 Password change? 
        int id = Objects.requireNonNull(AuthenticationUtil.getAuthUser().getId());
        return super.update(id, user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        int id = Objects.requireNonNull(AuthenticationUtil.getAuthUser().getId());
        super.delete(id);
    }
}