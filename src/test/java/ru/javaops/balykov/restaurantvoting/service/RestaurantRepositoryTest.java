package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static ru.javaops.balykov.restaurantvoting.DemoData.REST_1;
import static ru.javaops.balykov.restaurantvoting.DemoData.REST_2;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository repository;

    @Test
    void get() {
        Restaurant restaurant = repository.findById(1).orElseThrow();

        assertThat(restaurant)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(REST_1);
    }

    @Test
    void getAll() {
        List<Restaurant> restaurants = repository.findAll();

        assertThatList(restaurants)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(List.of(REST_1, REST_2));
    }

//    @Test
//    void name() {
//        new Restaurant()
//    }
}