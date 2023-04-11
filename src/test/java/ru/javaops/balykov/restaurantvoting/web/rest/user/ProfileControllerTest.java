package ru.javaops.balykov.restaurantvoting.web.rest.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.util.AuthenticationUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.web.rest.user.ProfileController.BASE_URL;

class ProfileControllerTest extends BaseMvcTest {
    @Test
    void getAuthUser() throws Exception {
        User authUser = AuthenticationUtil.getAuthUser();
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(authUser));
    }
}