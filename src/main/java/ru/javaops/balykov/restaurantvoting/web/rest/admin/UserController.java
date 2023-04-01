package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    protected static final String BASE_URL = "/api/admin/users";

    private final UserRepository repository;

    @GetMapping
    public List<User> getAll() { // todo add pagination
        log.info("Get all");
        return repository.findAll(Sort.by("name", "email"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        log.info("Get with id [{}]", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setRoles(Role.DEFAUT_ROLES);
        log.info("Create [{}]", user);
        if (!user.isNew()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete with id [{}]", id);
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        log.info("Update [{}] with id [{}]", user, id);
        if (user.isNew()) {
            user.setId(id);
        }
        if (!Objects.equals(user.getId(), id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}