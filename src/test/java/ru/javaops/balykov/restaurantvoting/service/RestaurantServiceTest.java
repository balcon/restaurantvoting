package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.javaops.balykov.restaurantvoting.TestUtil;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RestaurantServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void save() {
        service.save(TestUtil.restaurant1);

        List<Restaurant> restaurants = service.getAll();
        assertEquals(3, restaurants.size());
    }
}