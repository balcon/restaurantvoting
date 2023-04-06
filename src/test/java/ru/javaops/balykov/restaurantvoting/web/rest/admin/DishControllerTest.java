package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController.BASE_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DishControllerTest {
    private static final int RESTAURANT_ID = 1;
    private static final String RESTAURANT_URL =
            DishController.RESTAURANT_URL.replace("{restaurantId}", Integer.toString(RESTAURANT_ID));

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    DishRepository repository;

    @Test
    void create() throws Exception {
        Dish newDish = new Dish("New dish", 10000);
        newDish.setOfferDate(null); // TODO: 06.04.2023 Default offer date
        mockMvc.perform(post(RESTAURANT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newDish)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(newDish)));
    }

    @Test
    void createNotNew() throws Exception {
        mockMvc.perform(post(RESTAURANT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(DISH_1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + DISH_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(DISH_1)));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    void getAllOfRestaurant() throws Exception {
        mockMvc.perform(get(RESTAURANT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        List.of(DISH_1, DISH_2, YESTERDAYS_DISH))));
    }

    @Test
    void getTodaysOfRestaurant() throws Exception {
        mockMvc.perform(get(RESTAURANT_URL)
                        .queryParam("offerDate", DateTimeUtil.currentDate().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(REST_1_DISHES)));
    }

    @Test
    void getNotExists() throws Exception {
        mockMvc.perform(get(RESTAURANT_URL + "/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        Dish dish = new Dish(DISH_1);
        dish.setName("New name");
        int id = Objects.requireNonNull(dish.getId());

        mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dish)))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(id).orElseThrow().getName())
                .isEqualTo("New name");
    }

    @Test
    void updateDifferentId() throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + DISH_1_ID)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(DISH_2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + DISH_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Dish> dishes = repository.findAllByRestaurantId(RESTAURANT_ID, Sort.unsorted());
        assertThat(dishes).hasSize(2);
    }
}