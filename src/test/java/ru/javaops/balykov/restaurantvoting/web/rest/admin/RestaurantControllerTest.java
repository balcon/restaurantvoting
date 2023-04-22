package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.RestaurantController.BASE_URL;

@WithUserDetails(ADMIN_EMAIL)
class RestaurantControllerTest extends BaseMvcTest {
    @Autowired
    private RestaurantRepository repository;

    @Test
    void create() throws Exception {
        Restaurant restaurant = new Restaurant(null, "New restaurant", "New Address");
        long count = repository.count();

        post(BASE_URL, restaurant)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect(match(restaurant));
        repository.flush();
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void createNotNew() throws Exception {
        long count = repository.count();

        post(BASE_URL, REST_1)
                .andExpect(status().isBadRequest());
        repository.flush();
        assertThat(repository.count()).isEqualTo(count);
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(REST_1));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content").isArray());
//                .andExpect(match(List.of(REST_2, REST_1, REST_3)));
    }

    @Test
    void getNotExists() throws Exception {
        get(BASE_URL + "/0")
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        String newName = "New name";
        Restaurant restaurant = new Restaurant(REST_1_ID, newName, REST_1.getAddress());

        put(BASE_URL + "/" + REST_1_ID, restaurant)
                .andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(REST_1_ID).orElseThrow().getName())
                .isEqualTo(newName);
    }

    @Test
    void updateDifferentId() throws Exception {
        put(BASE_URL + "/" + REST_1_ID, REST_2)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(REST_1_ID)).isFalse();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void nonAdminAccess() throws Exception {
        get(BASE_URL)
                .andExpect(status().isForbidden());
    }
}