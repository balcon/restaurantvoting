package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DishController {
    protected static final String BASE_URL = "/api/admin/dishes";
    protected static final String RESTAURANT_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(RESTAURANT_URL)
    public ResponseEntity<Dish> create(@PathVariable int restaurantId, @RequestBody Dish dish) {
        if (!dish.isNew()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Restaurant proxy = restaurantRepository.getReferenceById(restaurantId); // todo check if not exists
        dish.setRestaurant(proxy);
        return new ResponseEntity<>(repository.save(dish), HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL + "/{id}")
    public ResponseEntity<Dish> getById(@PathVariable int id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping(BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> getAll() {
        return repository.findAll(); //todo sort
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
    @Transactional
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Dish dish) {
        if (dish.isNew()) {
            dish.setId(id);
        } else if (!Objects.equals(dish.getId(), id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.save(dish);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }
}
