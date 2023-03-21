package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static ru.javaops.balykov.restaurantvoting.DemoData.*;

@SpringBootTest
@Transactional
class DishServiceTest {
    @Autowired
    DishService service;

    @Test
    void get() {
        Dish dish = service.get(DISH_1_ID);

        assertThat(dish)
                .usingRecursiveComparison()
                .ignoringFields("restaurant")
                .isEqualTo(DISH_1);
    }

    @Test
    void save() {
        Dish newDish = new Dish("New dish", 100);
        Integer newId = service.save(newDish, REST_1_ID).getId();
        newDish.setId(newId);

        assertThat(service.get(newId))
                .usingRecursiveComparison()
                .ignoringFields("restaurant")
                .isEqualTo(newDish);
    }

    @Test
    void getAll() {
        List<Dish> dishes = service.getAllByDate(REST_1_ID, LocalDate.now());

        assertThatList(dishes)
                .usingRecursiveComparison()
                .ignoringFields("restaurant")
                .isEqualTo(REST_1_DISHES);
    }
}