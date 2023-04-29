package ru.javaops.balykov.restaurantvoting.web.rest.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.user.ProfileController.BASE_URL;

class ProfileControllerTest extends BaseMvcTest {
    @Autowired
    private UserRepository repository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAuthUser() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(USER));
    }

    @Test
    @WithAnonymousUser
    void registration() throws Exception {
        String email = "new_mail@mail.ru";
        User user = new User("New name", email, "password");
        post(BASE_URL, asJsonWithPassword(user))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertThat(repository.findByEmailIgnoreCase(email)).isPresent();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void update() throws Exception {
        User user = new User(USER);
        user.setName("New name");
        user.setPassword(null);

        put(BASE_URL, user).andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(USER_ID).orElseThrow().getName()).isEqualTo("New name");
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void checkRolesUpdateRestriction() throws Exception {
        User user = new User(USER);
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        put(BASE_URL, asJsonWithPassword(user)).andExpect(status().isNoContent());

        assertThat(repository.findById(USER_ID).orElseThrow().getRoles()).isEqualTo(Set.of(Role.USER));
    }

    @Test
    @WithAnonymousUser
    void duplicateEmailWhenRegistered() throws Exception {
        post(BASE_URL, asJsonWithPassword(new User("User", USER_EMAIL, "password")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void duplicateEmailWhenUpdate() throws Exception {
        put(BASE_URL, asJsonWithPassword(new User("User", ADMIN_EMAIL, "password")))
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