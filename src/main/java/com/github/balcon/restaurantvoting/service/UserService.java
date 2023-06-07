package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.UserModificationRestrictor;
import com.github.balcon.restaurantvoting.util.validation.UserValidator;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public static final String USERS_CACHE = "users";

    private final UserRepository repository;
    private final UserValidator validator;
    private final UserModificationRestrictor restrictor;
    private final PasswordEncoder encoder;

    public User create(User user) {
        log.info("Create new user [{}]", user);
        ValidationUtil.checkNew(user);
        return repository.save(prepareToCreate(user));
    }

    public User get(int id) {
        log.info("Get user by id [{}]", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Cacheable(cacheNames = USERS_CACHE, key = "#email")
    public User get(String email) {
        log.info("Get user with email [{}]", email);
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    public Page<User> getAll(Pageable pageable) {
        log.info("Get all users with pagination [{}]", pageable);
        return repository.findAll(pageable);
    }

    @CacheEvict(cacheNames = USERS_CACHE, allEntries = true)
    @Transactional
    public void update(int id, User user) throws BindException {
        log.info("Update user with id [{}] new values [{}]", id, user);
        restrictor.check(id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(prepareToUpdate(user, id));
    }

    @CacheEvict(cacheNames = USERS_CACHE, allEntries = true)
    @Transactional
    public void delete(int id) {
        log.info("Delete user with id [{}]", id);
        restrictor.check(id);
        ValidationUtil.checkIfExists(repository, id);
        repository.deleteById(id);
    }

    private User prepareToCreate(User user) {
        user.setRoles(Role.DEFAULT_ROLES);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }

    private User prepareToUpdate(User newUser, int id) throws BindException {
        User oldUser = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
            newUser.setPassword(oldUser.getPassword());
            validator.validate(newUser);
        } else {
            validator.validate(newUser);
            newUser.setPassword(encoder.encode(newUser.getPassword()));
        }
        newUser.setEmail(newUser.getEmail().toLowerCase());
        return newUser;
    }
}