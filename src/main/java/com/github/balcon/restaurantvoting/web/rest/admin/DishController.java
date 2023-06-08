package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.dto.assembler.DishAssembler;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.service.DishService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dish controller", description = "CRUD operations for dishes")
public class DishController {
    protected static final String BASE_URL = API_URL + "/admin/dishes";
    protected static final String RESTAURANT_URL = API_URL + "/admin/restaurants/{restaurantId}/dishes";

    private final DishService service;
    private final DishAssembler assembler;

    @PostMapping(value = RESTAURANT_URL)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new dish for restaurant with current offer date")
    public DishDto create(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("Creating new [{}] in restaurant id: [{}]", dish, restaurantId);
        return assembler.toModel(service.create(restaurantId, dish));
    }

    @GetMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DishDto getById(@PathVariable int id) {
        log.info("Getting dish id: [{}]", id);
        return assembler.toModel(service.get(id));
    }

    @GetMapping(RESTAURANT_URL)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all dishes of restaurant")
    public CollectionModel<DishDto> getAllOfRestaurant(@PathVariable int restaurantId,
                                                       @RequestParam(required = false) LocalDate offerDate,
                                                       @SortDefault("name") Sort sort) {
        log.info("Getting dishes of restaurant id: [{}], by date [{}]", restaurantId, offerDate);
        return assembler.toCollectionModel(
                service.getAll(restaurantId, offerDate, sort), restaurantId);
    }

    @PutMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("Updating [{}] id: [{}]", dish, id);
        service.update(id, dish);
    }

    @DeleteMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Deleting dish id: [{}]", id);
        service.delete(id);
    }

    // Only for demonstration
    @Hidden
    @PostMapping(BASE_URL + "/demo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void populate() {
        service.populateDemoDishes();
    }
}