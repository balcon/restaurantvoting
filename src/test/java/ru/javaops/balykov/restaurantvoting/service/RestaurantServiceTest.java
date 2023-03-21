package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static ru.javaops.balykov.restaurantvoting.DemoData.*;

@SpringBootTest
@Transactional
class RestaurantServiceTest {
    @Autowired
    RestaurantService service;

    @Test
    void get() {
        Restaurant restaurant = service.get(REST_1_ID);

        assertThat(restaurant)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(REST_1);
    }

    @Test
    void getAll() {
        List<Restaurant> restaurants = service.getAll();

        assertThatList(restaurants)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(List.of(REST_1, REST_2));
    }

    @Test
    void getAllWithTodaysDishes() {
        List<Restaurant> restaurants = service.getAllWithDishesByDate(LocalDate.now());
        assertThatList(restaurants)
                .usingRecursiveComparison()
                .ignoringFields("dishes.restaurant")
                .isEqualTo(List.of(REST_1, REST_2));
    }

    @Test
    void getWithDishes() {
        Restaurant restaurant = service.getWithDishes(REST_1_ID);
        Restaurant expected = new Restaurant(REST_1);
        expected.setDishes(List.of(DISH_1, DISH_2, YESTERDAYS_DISH));

        assertThat(restaurant)
                .usingRecursiveComparison()
                .ignoringFields("dishes.restaurant")
                .isEqualTo(expected);
    }

    @Test
    void save() {
        new Restaurant("New Rest", "New Desc", "New Address");
    }
}