package ru.javaops.balykov.restaurantvoting.web.rest.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.util.TestData.USER;
import static ru.javaops.balykov.restaurantvoting.util.TestData.USER_EMAIL;
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
    void registrationNewUser() throws Exception {
        String email = "new_mail@mail.ru";
        User user = new User("New name", email, "password");
        post(BASE_URL, asJsonWithPassword(user))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertThat(repository.findByEmailIgnoreCase(email)).isPresent();
    }

    private String asJsonWithPassword(User user) {
        ObjectNode json = mapper.valueToTree(user);
        json.put("password", user.getPassword());
        return json.toString();
    }
}