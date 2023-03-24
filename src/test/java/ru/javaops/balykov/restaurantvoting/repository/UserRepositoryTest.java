package ru.javaops.balykov.restaurantvoting.repository;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;

import java.util.List;
import java.util.Set;

import static ru.javaops.balykov.restaurantvoting.TestData.*;
import static ru.javaops.balykov.restaurantvoting.TestUtil.assertRecursiveEquals;

@DataJpaTest
class UserRepositoryTest {
    public static final RecursiveComparisonConfiguration DUMMY =
            RecursiveComparisonConfiguration.builder().build();

    @Autowired
    UserRepository repository;

    @Test
    void get() {
        User user = repository.findById(USER_1_ID).orElseThrow();

        assertRecursiveEquals(user, USER_1, DUMMY);
    }

    @Test
    void getAll() {
        List<User> users = repository.findAll(Sort.by("email"));

        assertRecursiveEquals(users, USERS, DUMMY);
    }

    @Test
    void save() {
        User user = new User("New user", "E-mail", "password", Set.of(Role.ROLE_USER));
        Integer id = repository.save(user).getId();
        user.setId(id);

        assertRecursiveEquals(repository.findById(id).get(), user, DUMMY);
    }

    @Test
    void update() {
        User user = new User(USER_1);
        user.setName("NEW NAME");
        repository.save(user);

        assertRecursiveEquals(repository.findById(user.getId()).get(), user, DUMMY);
    }

    @Test
    void delete() {
        repository.deleteById(USER_1_ID);

        assertRecursiveEquals(repository.findAll(), List.of(USER_2), DUMMY);
    }
}