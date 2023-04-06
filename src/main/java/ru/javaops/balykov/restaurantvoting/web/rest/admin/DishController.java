package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@Slf4j
public class DishController extends BaseController<Dish> {
    protected static final String BASE_URL = API_URL + "/admin/dishes";
    protected static final String RESTAURANT_URL = API_URL + "/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    public DishController(DishRepository repository, RestaurantRepository restaurantRepository) {
        super(repository, log);
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(RESTAURANT_URL)
    public ResponseEntity<Dish> create(@PathVariable int restaurantId, @RequestBody Dish dish) {
        Restaurant proxy = restaurantRepository.getReferenceById(restaurantId); // todo check if not exists
        dish.setRestaurant(proxy);
        dish.setOfferDate(DateTimeUtil.currentDate()); // TODO: 06.04.2023 Default offer date?
        return super.create(dish);
    }

    @GetMapping(BASE_URL + "/{id}")
    public ResponseEntity<Dish> getById(@PathVariable int id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping(BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> getAll() {
        return super.getAll(Sort.unsorted());
    }

    @GetMapping(RESTAURANT_URL)
    public List<Dish> getAllOfRestaurant(@PathVariable int restaurantId,
                                         @RequestParam(required = false) LocalDate offerDate) {
        if (offerDate == null) {
            return repository.findAllByRestaurantId(
                    restaurantId, Sort.by(Sort.Direction.DESC, "offerDate", "name", "id"));
        } else {
            return repository.findAllByRestaurantIdAndOfferDate(restaurantId, offerDate, Sort.by("name", "id"));
        }
    }

    @PutMapping(BASE_URL + "/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Dish dish) {
        return super.update(id, dish);
    }

    @DeleteMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}
