package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(UserController.BASE_URL)
@Slf4j
public class UserController extends BaseController<User> {
    protected static final String BASE_URL = API_URL + "/admin/users";

    public UserController(UserRepository repository) {
        super(repository, log);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        return super.create(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        return super.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<User> getAll(@SortDefault(sort = {"name", "email"}) Pageable pageable) {
        return super.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody User entity) {
        return super.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}