package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController.BASE_URL;

@WithUserDetails(ADMIN_EMAIL)
class UserControllerTest extends BaseMvcTest {
    @Autowired
    private UserRepository repository;

    @Test
    void create() throws Exception {
        String email = "new_mail@mail.ru";
        User user = new User("New admin", email, "secret_pass");
        user.setRoles(Set.of(Role.USER, Role.ADMIN)); // TODO: 09.04.2023 set default roles??

        post(BASE_URL, asJsonWithPassword(user))
                .andExpect(status().isCreated());
        repository.flush();

        assertThat(repository.findByEmailIgnoreCase(email)).isPresent();
    }

    // TODO: 25.04.2023 fix test

    @Test
    @Disabled
    void createNotNew() throws Exception {
        post(BASE_URL, USER)
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + USER_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(USER));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content").isArray());
//                .andExpect(match(List.of(USER, ADMIN)));
    }

    @Test
    void getNotExists() throws Exception {
        get(BASE_URL + "/0")
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        User user = new User(USER);
        user.setName("New name");
        user.setPassword(null);

        put(BASE_URL + "/" + USER_ID, asJsonWithPassword(user))
                .andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(USER_ID).orElseThrow().getName())
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

    @Test
    void createWithValidationErrors() throws Exception {
        post(BASE_URL, asJsonWithPassword(new User("", "not_mail", "short")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void updateWithValidationErrors() throws Exception {
        User user = new User(USER);
        user.setName("");
        user.setEmail("not_mail");
        user.setPassword("short");
        put(BASE_URL + "/" + USER_ID, asJsonWithPassword(user))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void duplicateEmailWhenCreate() throws Exception {
        post(BASE_URL, asJsonWithPassword(new User("User", USER_EMAIL, "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void duplicateEmailWhenUpdate() throws Exception {
        put(BASE_URL + "/" + USER_ID, asJsonWithPassword(new User("User", ADMIN_EMAIL, "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // Writing password to json manually because password is write-only in jackson config
    private String asJsonWithPassword(User user) {
        ObjectNode json = mapper.valueToTree(user);
        json.put("password", user.getPassword());
        return json.toString();
    }
}