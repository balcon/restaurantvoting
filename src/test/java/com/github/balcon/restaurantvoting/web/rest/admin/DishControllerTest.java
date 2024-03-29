package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.repository.DishRepository;
import com.github.balcon.restaurantvoting.web.rest.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.balcon.restaurantvoting.util.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(ADMIN_EMAIL)
class DishControllerTest extends BaseMvcTest {
    private static final int RESTAURANT_ID = 1;
    private static final String RESTAURANT_URL =
            DishController.RESTAURANT_URL.replace("{restaurantId}", Integer.toString(RESTAURANT_ID));

    @Autowired
    private DishRepository repository;

    @Test
    void create() throws Exception {
        long dishesCount = repository.count();
        Dish dish = new Dish(NEW_DISH);
        dish.setId(null);
        post(RESTAURANT_URL, dish)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists());
        repository.flush();
        assertThat(repository.count()).isEqualTo(dishesCount + 1);
    }

    @Test
    void getById() throws Exception {
        get(DishController.BASE_URL + "/" + DISH_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists());
    }

    @Test
    void getTodaysOfRestaurant() throws Exception {
        get(RESTAURANT_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded." + DishDto.COLLECTION).isArray())
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists());
    }

    @Test
    void update() throws Exception {
        String newName = "New name";
        Dish dish = new Dish(DISH_1);
        dish.setName(newName);
        put(DishController.BASE_URL + "/" + DISH_1_ID, dish).andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(DISH_1_ID).orElseThrow().getName()).isEqualTo(newName);
    }

    @Test
    void deleteById() throws Exception {
        delete(DishController.BASE_URL + "/" + DISH_1_ID).andExpect(status().isNoContent());
        assertThat(repository.existsById(DISH_1_ID)).isFalse();
    }

    @Test
    void validationErrors() throws Exception {
        expectValidationErrors(post(RESTAURANT_URL, new Dish(null, "", -1)));
    }

    @Test
    void xssValidation() throws Exception {
        Dish dish = new Dish(null, "<script>alert('xss')</script>", 10000);
        post(RESTAURANT_URL, dish)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.name").exists());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void nonAdminAccess() throws Exception {
        get(RestaurantController.BASE_URL)
                .andExpect(status().isForbidden());
    }

    @Test
    void createNotNew() throws Exception {
        expectIllegalRequest(post(RESTAURANT_URL, NEW_DISH));
    }

    @Test
    void updateDifferentId() throws Exception {
        expectIllegalRequest(put(DishController.BASE_URL + "/" + DISH_1_ID, NEW_DISH));

    }

    @Test
    void getNotExists() throws Exception {
        expectNotFound(get(DishController.BASE_URL + "/0"));
    }

    @Test
    void updateNonExists() throws Exception {
        expectNotFound(put(DishController.BASE_URL + "/100", NEW_DISH));
    }

    @Test
    void deleteNotExists() throws Exception {
        expectNotFound(delete(DishController.BASE_URL + "/0"));
    }

    private void expectValidationErrors(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.price").exists());
    }
}