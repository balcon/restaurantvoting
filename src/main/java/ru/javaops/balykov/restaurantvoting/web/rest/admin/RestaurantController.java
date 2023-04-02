package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

@RestController
@RequestMapping(RestaurantController.BASE_URL)
@Slf4j
public class RestaurantController extends AbstractController<Restaurant> {
    protected static final String BASE_URL = "/api/admin/restaurants";

    public RestaurantController(RestaurantRepository repository) {
        super(repository, log, Sort.by("name", "id"));
    }
}