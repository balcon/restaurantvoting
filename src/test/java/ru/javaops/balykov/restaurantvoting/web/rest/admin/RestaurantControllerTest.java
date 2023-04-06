package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseControllerTest;

import java.util.List;
import java.util.Objects;

import static ru.javaops.balykov.restaurantvoting.util.TestData.*;

class RestaurantControllerTest extends BaseControllerTest {

    RestaurantRepository repository;

    public RestaurantControllerTest(@Autowired RestaurantRepository repository) {
        super(repository, RestaurantController.BASE_URL);
        this.repository = repository;
    }

    @Test
    void create() throws Exception {
        super.create(NEW_REST);
    }

    @Test
    void createNotNew() throws Exception {
        super.createNotNew(REST_1);
    }

    @Test
    void getById() throws Exception {
        super.getById(REST_1_ID, REST_1);
    }

    @Test
    void getAll() throws Exception {
        super.getAll(List.of(REST_2, REST_1, REST_3));
    }

    @Test
    void getNotExists() throws Exception {
        super.getNotExists(-1);
    }

    @Test
    void deleteById() throws Exception {
        super.deleteById(REST_1_ID);
    }

    @Test
    void update() throws Exception {
        Restaurant restaurant = new Restaurant(REST_1);
        int id = Objects.requireNonNull(restaurant.getId());
        restaurant.setName("New name");
        super.update(id, restaurant);

        Assertions.assertThat(repository.findById(id).orElseThrow().getName())
                .isEqualTo("New name");
    }

    @Test
    void updateNotExists() throws Exception {
        Restaurant restaurant = new Restaurant(REST_1);
        restaurant.setId(-1);
        super.updateNotExists(restaurant);
    }

    @Test
    void updateDifferentId() throws Exception {
        super.updateDifferentId(REST_1_ID, REST_2);
    }
}