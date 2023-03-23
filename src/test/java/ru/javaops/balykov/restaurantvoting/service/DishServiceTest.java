package ru.javaops.balykov.restaurantvoting.service;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.balykov.restaurantvoting.DemoData.*;

@SpringBootTest
@Transactional
class DishServiceTest {
    public static final RecursiveComparisonConfiguration REST_IGNORE =
            RecursiveComparisonConfiguration.builder().withIgnoredFields("restaurant").build();

    @Autowired
    DishService service;

    @Test
    void get() {
        Dish dish = service.get(DISH_1_ID);

        assertEquals(dish, DISH_1);
    }

    @Test
    void save() {
        Dish dish = new Dish("New dish", 100);
        Integer id = service.save(dish, REST_1_ID).getId();
        dish.setId(id);

        assertEquals(service.get(id), dish);
    }

    @Test
    void getAll() {
        List<Dish> dishes = service.getAllByDate(REST_1_ID, LocalDate.now());

        assertEquals(dishes, REST_1_DISHES);
    }


    @Test
    void delete() {
        service.delete(DISH_1_ID);
        ArrayList<Dish> dishes = new ArrayList<>(REST_1_DISHES);
        dishes.remove(DISH_1);

        assertEquals(service.getAllByDate(REST_1_ID, LocalDate.now()), dishes);
    }

    @Test
    void update() {
        Dish dish = new Dish(DISH_1);
        dish.setName("New name");
        service.update(dish);

        assertEquals(service.get(dish.getId()), dish);
    }

    private static void assertEquals(Dish actual, Dish expected) {
        assertThat(actual)
                .usingRecursiveComparison(REST_IGNORE)
                .isEqualTo(expected);
    }

    private static void assertEquals(List<Dish> actual, List<Dish> expected) {
        assertThat(actual)
                .usingRecursiveComparison(REST_IGNORE)
                .isEqualTo(expected);
    }
}