package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
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
        Dish newDish = new Dish(null, "New dish", 10000);
        long dishesCount = repository.count();
        post(RESTAURANT_URL, newDish)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect(match(newDish));
        repository.flush();
        assertThat(repository.count()).isEqualTo(dishesCount + 1);
    }

    @Test
    void createNotNew() throws Exception {
        long count = repository.count();

        post(RESTAURANT_URL, DISH_1)
                .andExpect(status().isBadRequest());
        repository.flush();
        assertThat(repository.count()).isEqualTo(count);
    }

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + DISH_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(DISH_1));
    }

    @Test
    void getAll() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content").isArray());
//                .andExpect(match(repository.findAll()));
    }

    @Test
    void getTodaysOfRestaurant() throws Exception {
        get(RESTAURANT_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(REST_1_DISHES));
    }

    @Test
    void getNotExists() throws Exception {
        get(RESTAURANT_URL + "/0")
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        String newName = "New name";
        int id = Objects.requireNonNull(DISH_1.getId());
        Dish dish = new Dish(id, newName, DISH_1.getPrice(), DISH_1.getOfferDate());

        put(BASE_URL + "/" + id, dish)
                .andExpect(status().isNoContent());
        repository.flush();
        assertThat(repository.findById(id).orElseThrow().getName())
                .isEqualTo(newName);
    }

    @Test
    void updateWithDifferentId() throws Exception {
        put(BASE_URL + "/" + DISH_1_ID, DISH_2)
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById() throws Exception {
        delete(BASE_URL + "/" + DISH_1_ID)
                .andExpect(status().isNoContent());
        assertThat(repository.existsById(DISH_1_ID)).isFalse();
    }
}