package com.github.balcon.restaurantvoting.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import com.github.balcon.restaurantvoting.dto.RestaurantDto;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.web.rest.BaseMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.github.balcon.restaurantvoting.util.TestData.*;
import static com.github.balcon.restaurantvoting.web.rest.admin.RestaurantController.BASE_URL;

@WithUserDetails(ADMIN_EMAIL)
class RestaurantControllerTest extends BaseMvcTest {
    @Autowired
    private RestaurantRepository repository;

    @Test
    void create() throws Exception {
        Restaurant restaurant = new Restaurant(NEW_RESTAURANT);
        restaurant.setId(null);
        long count = repository.count();

        post(BASE_URL, restaurant)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists())
                .andExpect(jsonPath("$._links.dishes").exists());
        repository.flush();
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists())
                .andExpect(jsonPath("$._links.dishes").exists());
//                .andExpect(match(REST_1));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded." + RestaurantDto.COLLECTION).isArray());
    }

    @Test
    void update() throws Exception {
        String newName = "New name";
        Restaurant restaurant = new Restaurant(REST_1);
        restaurant.setName(newName);

        put(BASE_URL + "/" + REST_1_ID, restaurant)
                .andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(REST_1_ID).orElseThrow().getName())
                .isEqualTo(newName);
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(REST_1_ID)).isFalse();
    }

    @Test
    void nonUniqueCreate() throws Exception {
        Restaurant restaurant = new Restaurant(REST_1);
        restaurant.setId(null);

        expectValidationErrors(post(BASE_URL, restaurant));
    }

    @Test
    void validationError() throws Exception {
        expectValidationErrors(post(BASE_URL, new Restaurant(null, "", "nope")));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void nonAdminAccess() throws Exception {
        get(BASE_URL)
                .andExpect(status().isForbidden());
    }

    @Test
    void createNotNew() throws Exception {
        expectIllegalRequest(post(BASE_URL, NEW_RESTAURANT));
    }

    @Test
    void updateDifferentId() throws Exception {
        expectIllegalRequest(put(BASE_URL + "/0", NEW_RESTAURANT));

    }

    @Test
    void getNotExists() throws Exception {
        expectNotFound(get(BASE_URL + "/0"));
    }

    @Test
    void updateNonExists() throws Exception {
        expectNotFound(put(BASE_URL + "/100", NEW_RESTAURANT));
    }

    @Test
    void deleteNotExists() throws Exception {
        expectNotFound(delete(BASE_URL + "/0"));
    }

    private void expectValidationErrors(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.address").exists());
    }
}