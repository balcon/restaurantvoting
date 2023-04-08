package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController.BASE_URL;

class UserControllerTest extends BaseMvcTest {
    @Autowired
    private UserRepository repository;

    @Test
    void create() throws Exception {
        User user = new User("New user", "new_mail@mail.ru", "secret");
        user.setRoles(Role.DEFAULT_ROLES); // TODO: 09.04.2023 set default roles??
        long count = repository.count();

        post(BASE_URL, asJsonWithPassword(user))
                .andExpect(status().isCreated());
//                .andExpect(match(user));
        repository.flush();
        assertThat(repository.count()).isEqualTo(count + 1);
    }


    @Test
    void createNotNew() throws Exception {
        post(BASE_URL, USER)
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + USER_ID)
                .andExpect(status().isOk())
                .andExpect(match(USER));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(match(List.of(USER, ADMIN)));
    }

    @Test
    void getNotExists() throws Exception {
        get(BASE_URL + "/0")
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        User user = new User(USER);
        int id = Objects.requireNonNull(user.getId());
        user.setName("New name");

        put(BASE_URL + "/" + id, asJsonWithPassword(user))
                .andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(id).orElseThrow().getName())
                .isEqualTo("New name");
    }

    @Test
    void updateDifferentId() throws Exception {
        put(BASE_URL + "/" + USER_ID, ADMIN)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + USER_ID)
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(USER_ID)).isFalse();
    }

    // Writing password to json manually because password is write-only in jackson config
    private String asJsonWithPassword(User user) {
        ObjectNode json = mapper.valueToTree(user);
        json.put("password", user.getPassword());
        return json.toString();
    }
}