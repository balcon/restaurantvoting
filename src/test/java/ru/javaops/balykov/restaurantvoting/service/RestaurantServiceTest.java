package ru.javaops.balykov.restaurantvoting.service;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.balykov.restaurantvoting.DemoData.*;

@SpringBootTest
@Transactional
class RestaurantServiceTest {
    public static final RecursiveComparisonConfiguration DISHES_IGNORE =
            RecursiveComparisonConfiguration.builder().withIgnoredFields("dishes").build();
    public static final RecursiveComparisonConfiguration DISH_REST_IGNORE =
            RecursiveComparisonConfiguration.builder().withIgnoredFields("dishes.restaurant").build();

    @Autowired
    RestaurantService service;

    @Test
    void get() {
        Restaurant restaurant = service.get(REST_1_ID);

        assertEquals(restaurant, REST_1, DISHES_IGNORE);
    }

    @Test
    void getAll() {
        List<Restaurant> restaurants = service.getAll();

        assertEquals(restaurants, List.of(REST_2, REST_1), DISHES_IGNORE);
    }

    @Test
    void getAllWithTodaysDishes() {
        List<Restaurant> restaurants = service.getAllWithDishesByDate(LocalDate.now());

        assertEquals(restaurants, List.of(REST_2, REST_1), DISH_REST_IGNORE);
    }

    @Test
    void getWithDishes() {
        Restaurant restaurant = service.getWithDishes(REST_1_ID);
        Restaurant expected = new Restaurant(REST_1);
        expected.setDishes(List.of(DISH_1, DISH_2, YESTERDAYS_DISH));

        assertEquals(restaurant, expected, DISH_REST_IGNORE);
    }

    @Test
    void save() {
        Restaurant restaurant = new Restaurant("New Rest", "New Desc", "New Address");
        Integer id = service.save(restaurant).getId();
        restaurant.setId(id);

        assertEquals(service.get(id), restaurant, DISHES_IGNORE);
    }

    @Test
    void delete() {
        service.delete(REST_1_ID);

        assertEquals(service.getAll(), List.of(REST_2), DISHES_IGNORE);
    }

    @Test
    void update() {
        Restaurant restaurant = new Restaurant(REST_1);
        restaurant.setName("New Name");
        service.update(restaurant);

        assertEquals(service.get(restaurant.getId()), restaurant, DISHES_IGNORE);
    }

    public static void assertEquals(Restaurant actual, Restaurant expected, RecursiveComparisonConfiguration configuration) {
        assertThat(actual)
                .usingRecursiveComparison(configuration)
                .isEqualTo(expected);
    }

    public static void assertEquals(List<Restaurant> actual, List<Restaurant> expected, RecursiveComparisonConfiguration configuration) {
        assertThat(actual)
                .usingRecursiveComparison(configuration)
                .isEqualTo(expected);
    }
}