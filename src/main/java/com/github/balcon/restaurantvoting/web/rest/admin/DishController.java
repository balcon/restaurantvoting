package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.dto.assembler.DishAssembler;
import com.github.balcon.restaurantvoting.dto.assembler.MethodsForAssembler;
import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.DishRepository;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import com.github.balcon.restaurantvoting.web.rest.BaseController;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@Slf4j
@Tag(name = "Dish controller", description = "CRUD operations for dishes")
public class DishController extends BaseController<Dish> implements MethodsForAssembler {
    protected static final String BASE_URL = API_URL + "/admin/dishes";
    protected static final String RESTAURANT_URL = API_URL + "/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final DishAssembler assembler;

    public DishController(DishRepository repository, RestaurantRepository restaurantRepository, DishAssembler assembler) {
        super(repository, log);
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.assembler = assembler;
    }

    @PostMapping(value = RESTAURANT_URL)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new dish for restaurant with current offer date")
    public DishDto create(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        Restaurant restaurant =
                restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException(restaurantId));
        dish.setRestaurant(restaurant);
        return assembler.toModelWithCollection(super.doCreate(dish));
    }

    @GetMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DishDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(super.doGetById(id));
    }

    @GetMapping(BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<DishDto> getAll(@SortDefault(sort = "offerDate", direction = Sort.Direction.DESC)
                                           @SortDefault("name") @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(super.doGetAll(pageable));
    }

    @GetMapping(RESTAURANT_URL)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get all dishes of restaurant")
    public CollectionModel<DishDto> getAllOfRestaurant(@PathVariable int restaurantId,
                                                       @RequestParam(required = false) LocalDate offerDate,
                                                       @SortDefault("name") Sort sort) {
        log.info("Get all of restaurant with id [{}], by date [{}] with sort [{}]", restaurantId, offerDate, sort);
        ValidationUtil.checkIfExists(restaurantRepository, restaurantId);
        offerDate = offerDate == null ? DateTimeUtil.currentDate() : offerDate;
        List<Dish> dishes = repository.findAllByRestaurantIdAndOfferDate(restaurantId, offerDate, sort);
        return assembler.toCollectionModel(dishes, restaurantId);
    }

    @PutMapping(BASE_URL + "/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        dish.setRestaurant(repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)).getRestaurant());
        super.doUpdate(id, dish);
    }

    @DeleteMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.doDelete(id);
    }

    // Only for demonstration
    @Hidden
    @PostMapping(BASE_URL + "/demo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void populateDemoDishes() {
        Random rnd = new Random();
        restaurantRepository.findAll().stream()
                .map(Restaurant::id)
                .forEach(id -> IntStream.range(0, rnd.nextInt(1, 5))
                        .forEach(i -> this.create(
                                id, new Dish(null, generateDish(rnd), rnd.nextInt(200, 600) * 100))));
    }

    private String generateDish(Random random) {
        String[][] food = {
                {"Fried", "Boiled", "Marinated", "Fresh"},
                {"chicken", "duck", "beef", "eggplant"},
                {"with"},
                {"tomatoes", "soy sauce", "french fries"}};
        return Arrays.stream(food)
                .map(a -> a[random.nextInt(a.length)])
                .collect(Collectors.joining(" "));
    }
}