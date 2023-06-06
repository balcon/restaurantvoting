package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.UserModificationRestrictor;
import com.github.balcon.restaurantvoting.util.UserPreparator;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final UserPreparator preparator;
    private final UserModificationRestrictor restrictor;

    public User create(User user) {
        log.info("Create new user [{}]", user);
        ValidationUtil.checkNew(user);
        return repository.save(preparator.prepareToCreate(user));
    }

    public User get(int id) {
        log.info("Get user by id [{}]", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // TODO: 06.06.2023 cache
    public User get(String email) {
        log.info("Get user with email [{}]", email);
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    public Page<User> getAll(Pageable pageable) {
        log.info("Get all users with pagination [{}]", pageable);
        return repository.findAll(pageable);
    }

    // TODO: 06.06.2023 cache
    @Transactional
    public void update(int id, User user) throws BindException {
        log.info("Update user with id [{}] new values [{}]", id, user);
        restrictor.check(id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(preparator.prepareToUpdate(user, id));
    }

    // TODO: 06.06.2023 cache
    @Transactional
    public void delete(int id) {
        log.info("Delete user with id [{}]", id);
        restrictor.check(id);
        ValidationUtil.checkIfExists(repository, id);
        repository.deleteById(id);
    }
}