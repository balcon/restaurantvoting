package ru.javaops.balykov.restaurantvoting.web.rest.user;

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
import ru.javaops.balykov.restaurantvoting.dto.RestaurantWithDishesDto;
import ru.javaops.balykov.restaurantvoting.dto.assembler.RestaurantWithDishes;
import ru.javaops.balykov.restaurantvoting.exception.NotFoundException;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.service.VoteService;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

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