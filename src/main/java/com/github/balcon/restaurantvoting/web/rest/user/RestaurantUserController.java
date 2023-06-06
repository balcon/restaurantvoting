package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.dto.RestaurantWithDishesDto;
import com.github.balcon.restaurantvoting.dto.assembler.RestaurantWithDishes;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.service.RestaurantService;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(path = RestaurantUserController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Restaurant and dish controller", description = "Get restaurants with dishes")
public class RestaurantUserController {
    protected static final String BASE_URL = API_URL + "/user/restaurants";

    private final RestaurantService service;
    private final RestaurantWithDishes assembler;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get restaurant with today's dishes and votes")
    public RepresentationModel<RestaurantWithDishesDto> getById(@PathVariable int id,
                                                                @SortDefault("dish.name") @ParameterObject Sort sort) {
        Restaurant restaurant = service.getWithDishes(id, sort);
        return assembler.toModelWithCollection(restaurant, DateTimeUtil.currentDate());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get restaurants with today's dishes and votes")
    public CollectionModel<RestaurantWithDishesDto> getAll(@SortDefault(sort = {"name", "dish.name"})
                                                           @ParameterObject Sort sort) {
        List<Restaurant> restaurants = service.getAllWithDishes(sort);
        return assembler.toCollectionModel(restaurants, DateTimeUtil.currentDate());
    }
}