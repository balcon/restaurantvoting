package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.TestData;
import com.github.balcon.restaurantvoting.web.rest.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest extends BaseMvcTest {
    @Autowired
    private UserRepository repository;

    @Test
    @WithUserDetails(TestData.USER_EMAIL)
    void getAuthUser() throws Exception {
        get(ProfileController.BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).doesNotExist());
    }

    @Test
    @WithAnonymousUser
    void registration() throws Exception {
        User user = new User(TestData.NEW_USER);
        user.setId(null);
        post(ProfileController.BASE_URL, user)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).doesNotExist());

        assertThat(repository.findByEmailIgnoreCase(user.getEmail())).isPresent();
    }

    @Test
    @WithUserDetails(TestData.USER_EMAIL)
    void update() throws Exception {
        User user = new User(TestData.USER);
        user.setName("New name");
        user.setPassword(null);

        put(ProfileController.BASE_URL, user).andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(TestData.USER_ID).orElseThrow().getName()).isEqualTo("New name");
    }

    @Test
    @WithUserDetails(TestData.USER_EMAIL)
    void checkRolesUpdateRestriction() throws Exception {
        User user = new User(TestData.USER);
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        put(ProfileController.BASE_URL, user).andExpect(status().isNoContent());

        assertThat(repository.findById(TestData.USER_ID).orElseThrow().getRoles()).isEqualTo(Set.of(Role.USER));
    }

    @Test
    @WithAnonymousUser
    void duplicateEmailRegistered() throws Exception {
        post(ProfileController.BASE_URL, new User("User", TestData.USER_EMAIL, "password"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    @WithUserDetails(TestData.USER_EMAIL)
    void duplicateEmailUpdate() throws Exception {
        put(ProfileController.BASE_URL, new User("User", TestData.ADMIN_EMAIL, "password"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @WithUserDetails(TestData.USER_EMAIL)
    @Test
    void updateDifferentId() throws Exception {
        expectIllegalRequest(put(ProfileController.BASE_URL, TestData.ADMIN));
    }
}