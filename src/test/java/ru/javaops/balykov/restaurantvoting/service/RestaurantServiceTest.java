package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.javaops.balykov.restaurantvoting.TestUtil;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RestaurantServiceTest {

    @Autowired
    RestaurantService service;

    @Autowired
    DishService dishService;

    @Test
    void get() {
        List<Dish> dishes = dishService.saveAll(TestUtil.dishes);

        Restaurant rest1 = service.save(TestUtil.restaurant1);

        System.out.println(rest1);
    }
}