package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(UserController.BASE_URL)
@Slf4j
public class UserController extends BaseController<User> {
    protected static final String BASE_URL = "/api/admin/users";

    public UserController(UserRepository repository) {
        super(repository, log);
    }

    @Override
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        return super.create(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return super.getAll(Sort.by("name", "email"));
    }
}