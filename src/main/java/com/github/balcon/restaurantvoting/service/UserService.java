package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.UserModificationRestrictor;
import com.github.balcon.restaurantvoting.util.validation.UserValidator;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import static com.github.balcon.restaurantvoting.service.VoteService.VOTES_CACHE;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USERS_CACHE = "users";

    private final UserRepository repository;
    private final UserValidator validator;
    private final UserModificationRestrictor restrictor;
    private final PasswordEncoder encoder;

    public User create(User user) {
        ValidationUtil.checkNew(user);
        return repository.save(prepareToCreate(user));
    }

    public User get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Cacheable(cacheNames = USERS_CACHE, key = "#email")
    public User get(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @CacheEvict(cacheNames = USERS_CACHE, allEntries = true)
    @Transactional
    public void update(int id, User user) throws BindException {
        restrictor.check(id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(prepareToUpdate(user, id));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = USERS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = VOTES_CACHE, allEntries = true)})
    @Transactional
    public void delete(int id) {
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