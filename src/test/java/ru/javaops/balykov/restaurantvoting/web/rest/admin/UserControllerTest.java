package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController.BASE_URL;

@WithUserDetails(ADMIN_EMAIL)
class UserControllerTest extends BaseMvcTest {
    @Autowired
    private UserRepository repository;

    @Test
    void create() throws Exception {
        User user = new User(NEW_USER);
        user.setId(null);

        post(BASE_URL, user)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists());
        repository.flush();
        assertThat(repository.findByEmailIgnoreCase(user.getEmail())).isPresent();
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + USER_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists());
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded." + UserDto.COLLECTION).isArray());
    }

    @Test
    void update() throws Exception {
        User user = new User(USER);
        user.setName("New name");
        user.setPassword(null);

        put(BASE_URL + "/" + USER_ID, user).andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(USER_ID).orElseThrow().getName()).isEqualTo("New name");
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + USER_ID).andExpect(status().isNoContent());
        assertThat(repository.existsById(USER_ID)).isFalse();
    }

    @Test
    void createWithValidationErrors() throws Exception {
        expectValidationErrors(post(BASE_URL, new User("", "not_mail", "short")));
    }

    @Test
    void updateWithValidationErrors() throws Exception {
        User user = new User(USER);
        user.setName("");
        user.setEmail("not_mail");
        user.setPassword("short");
        expectValidationErrors(put(BASE_URL + "/" + USER_ID, user));
    }

    @Test
    void duplicateEmailWhenCreate() throws Exception {
        post(BASE_URL, new User("User", USER_EMAIL, "password"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    void duplicateEmailWhenUpdate() throws Exception {
        put(BASE_URL + "/" + USER_ID, new User("User", ADMIN_EMAIL, "password"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void nonAdminAccess() throws Exception {
        get(RestaurantController.BASE_URL)
                .andExpect(status().isForbidden());
    }

    @Test
    void createNotNew() throws Exception {
        expectIllegalRequest(post(BASE_URL, NEW_USER));
    }

    @Test
    void updateDifferentId() throws Exception {
        expectIllegalRequest(put(BASE_URL + "/0", NEW_USER));

    }

    @Test
    void getNotExists() throws Exception {
        expectNotFound(get(BASE_URL + "/0"));
    }

    @Test
    void updateNonExists() throws Exception {
        expectNotFound(put(BASE_URL + "/100", NEW_USER));
    }

    @Test
    void deleteNotExists() throws Exception {
        expectNotFound(delete(BASE_URL + "/0"));
    }

    private void expectValidationErrors(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.password").exists());
    }
}