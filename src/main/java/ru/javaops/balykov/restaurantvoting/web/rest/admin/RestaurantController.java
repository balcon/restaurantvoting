package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

@RestController
@RequestMapping(RestaurantController.BASE_URL)
@Slf4j
public class RestaurantController extends BaseController<Restaurant> {
    protected static final String BASE_URL = "/api/v1/admin/restaurants";

    public RestaurantController(RestaurantRepository repository) {
        super(repository, log);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> getAll() {
        return super.getAll(Sort.by("name", "id"));
    }
}