package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.github.balcon.restaurantvoting.dto.RestaurantWithDishesDto;
import com.github.balcon.restaurantvoting.dto.assembler.RestaurantWithDishes;
import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.util.List;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(RestaurantUserController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Restaurant and dish controller", description = "Get restaurants with dishes and voting")
public class RestaurantUserController {
    protected static final String BASE_URL = API_URL + "/user/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantWithDishes assembler;
    private final VoteService voteService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get restaurant with today's dishes and votes")
    public RepresentationModel<RestaurantWithDishesDto> getById(@PathVariable int id,
                                                                @SortDefault("dish.name") @ParameterObject Sort sort) {
        log.info("Get restaurant with dishes id: [{}], sort [{}]", id, sort);
        LocalDate voteDate = DateTimeUtil.currentDate();
        Restaurant restaurant = repository.findByIdWithDishesByDate(id, voteDate, sort)
                .orElseThrow(() -> new NotFoundException(id));

        return assembler.toModelWithCollection(restaurant, voteDate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Get restaurants with today's dishes and votes")
    public CollectionModel<RestaurantWithDishesDto> getAll(@SortDefault(sort = {"name", "dish.name"})
                                                           @ParameterObject Sort sort) {
        log.info("Get all restaurant with dishes, sort [{}]", sort);
        LocalDate voteDate = DateTimeUtil.currentDate();
        List<Restaurant> restaurants = repository.findAllWithDishesByDate(voteDate, sort);

        return assembler.toCollectionModel(restaurants, voteDate);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Vote for restaurant")
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("User [{}] vote for restaurant with id [{}]", authUser.getUser(), id);
        voteService.vote(repository.getReferenceById(id), authUser.getUser());
    }
}