package ru.javaops.balykov.restaurantvoting.service;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.TestUtil;
import ru.javaops.balykov.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javaops.balykov.restaurantvoting.TestData.*;
import static ru.javaops.balykov.restaurantvoting.TestUtil.assertRecursiveEquals;

@SpringBootTest
@Transactional
class DishServiceTest {
    public static final RecursiveComparisonConfiguration RESTAURANT_IGNORE =
            RecursiveComparisonConfiguration.builder().withIgnoredFields("restaurant").build();

    @Autowired
    DishService service;

    @Test
    void get() {
        Dish dish = service.get(DISH_1_ID);

        TestUtil.assertRecursiveEquals(dish, DISH_1, RESTAURANT_IGNORE);
    }

    @Test
    void save() {
        Dish dish = new Dish("New dish", 100);
        Integer id = service.save(dish, REST_1_ID).getId();
        dish.setId(id);

        TestUtil.assertRecursiveEquals(service.get(id), dish, RESTAURANT_IGNORE);
    }

    @Test
    void getAll() {
        List<Dish> dishes = service.getAllByDate(REST_1_ID, LocalDate.now());

        assertRecursiveEquals(dishes, REST_1_DISHES, RESTAURANT_IGNORE);
    }


    @Test
    void delete() {
        service.delete(DISH_1_ID);
        ArrayList<Dish> dishes = new ArrayList<>(REST_1_DISHES);
        dishes.remove(DISH_1);

        assertRecursiveEquals(service.getAllByDate(REST_1_ID, LocalDate.now()), dishes, RESTAURANT_IGNORE);
    }

    @Test
    void update() {
        Dish dish = new Dish(DISH_1);
        dish.setName("New name");
        service.update(dish);

        TestUtil.assertRecursiveEquals(service.get(dish.getId()), dish, RESTAURANT_IGNORE);
    }
}