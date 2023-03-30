package ru.javaops.balykov.restaurantvoting.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping(UsersRestController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class UsersRestController {
    public static final String BASE_URL = "/api/admin/users";

    private final UserRepository repository;

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("Get user with id: [{}]", id);
        return repository.findById(id).orElseThrow(); // todo exception
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setRoles(Role.DEFAUT_ROLES);
        log.info("Create user: [{}]", user);
        return repository.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete user with id: [{}]", id);
        repository.deleteById(id);
    }
}
