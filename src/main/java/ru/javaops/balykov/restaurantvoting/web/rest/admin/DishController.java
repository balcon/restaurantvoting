package ru.javaops.balykov.restaurantvoting.web.rest.admin;

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
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.dto.assembler.DishAssembler;
import ru.javaops.balykov.restaurantvoting.exception.NotFoundException;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.validation.ValidationUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;
import ru.javaops.balykov.restaurantvoting.web.rest.HalLinkMethods;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@Slf4j
@Tag(name = "Dish controller", description = "CRUD operations for dishes")
public class DishController extends BaseController<Dish> implements HalLinkMethods {
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

    @PostMapping(value = RESTAURANT_URL, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new dish for restaurant with current offer date")
    public DishDto create(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        Restaurant proxy = restaurantRepository.getReferenceById(restaurantId); // todo check if not exists
        dish.setRestaurant(proxy);
        return assembler.toModelWithCollection(super.doCreate(dish));
    }

    @GetMapping(value = BASE_URL + "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public DishDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(super.doGetById(id));
    }

    @GetMapping(value = BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<DishDto> getAll(@SortDefault(sort = "offerDate", direction = Sort.Direction.DESC)
                                           @SortDefault("name") @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(super.doGetAll(pageable));
    }

    @GetMapping(value = RESTAURANT_URL, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get all dishes of restaurant")
    public List<Dish> getAllOfRestaurant(@PathVariable int restaurantId,
                                         @RequestParam(required = false) LocalDate offerDate,
                                         @SortDefault("name") Sort sort) {
        log.info("Get all of restaurant with id [{}], by date [{}] with sort [{}]", restaurantId, offerDate, sort);
        ValidationUtil.checkIfExists(restaurantRepository, restaurantId);
        offerDate = offerDate == null ? DateTimeUtil.currentDate() : offerDate;
        return repository.findAllByRestaurantIdAndOfferDate(restaurantId, offerDate, sort);
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
}