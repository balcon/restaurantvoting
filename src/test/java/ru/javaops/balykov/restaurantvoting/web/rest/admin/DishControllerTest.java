package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController.BASE_URL;

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
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists());
        repository.flush();
        assertThat(repository.count()).isEqualTo(dishesCount + 1);
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + DISH_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists());
//                .andExpect(match(assembler.toModel(DISH_1)));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded." + DishDto.COLLECTION).isArray());
    }

    @Test
    void getTodaysOfRestaurant() throws Exception {
        get(RESTAURANT_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded." + DishDto.COLLECTION).isArray());
    }

    @Test
    void update() throws Exception {
        String newName = "New name";
        Dish dish = new Dish(DISH_1);
        dish.setName(newName);
        put(BASE_URL + "/" + DISH_1_ID, dish).andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(DISH_1_ID).orElseThrow().getName()).isEqualTo(newName);
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + DISH_1_ID).andExpect(status().isNoContent());
        assertThat(repository.existsById(DISH_1_ID)).isFalse();
    }

    @Test
    void validationErrors() throws Exception {
        expectValidationErrors(post(RESTAURANT_URL, new Dish(null, "", 3000000)));
    }

    @Test
    void xssValidation() throws Exception{
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
        expectIllegalRequest(put(BASE_URL + "/" + DISH_1_ID, NEW_DISH));

    }

    @Test
    void getNotExists() throws Exception {
        expectNotFound(get(BASE_URL + "/0"));
    }

    @Test
    void updateNonExists() throws Exception {
        expectNotFound(put(BASE_URL + "/100", NEW_DISH));
    }

    @Test
    void deleteNotExists() throws Exception {
        expectNotFound(delete(BASE_URL + "/0"));
    }

    private void expectValidationErrors(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.price").exists());
    }
}