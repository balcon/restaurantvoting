package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(RestaurantController.BASE_URL)
@Slf4j
public class RestaurantController extends BaseController<Restaurant> {
    protected static final String BASE_URL = API_URL + "/admin/restaurants";

    public RestaurantController(RestaurantRepository repository) {
        super(repository, log);
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        return super.create(restaurant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable int id) {
        return super.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Restaurant> getAll(@SortDefault(sort = {"name", "address"}) Pageable pageable) {
        return super.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        return super.update(id, restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}