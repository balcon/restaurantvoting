package ru.javaops.balykov.restaurantvoting.web.rest.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.service.VoteService;
import ru.javaops.balykov.restaurantvoting.to.RestaurantTo;
import ru.javaops.balykov.restaurantvoting.util.AuthenticationUtil;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.util.RestaurantToMapper;

import java.util.List;
import java.util.Optional;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(RestaurantUserController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class RestaurantUserController {
    protected static final String BASE_URL = API_URL + "/user/restaurants";

    private final RestaurantRepository repository;

    private final VoteService voteService;

    private final RestaurantToMapper toMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTo> getById(@PathVariable int id) {
        log.info("Get by id [{}]", id);
        Optional<Restaurant> restaurantOpt = repository.findByIdWithDishesByDate(id, DateTimeUtil.currentDate());
        if (restaurantOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            RestaurantTo restaurantTo = toMapper.getTo(restaurantOpt.get());
            return new ResponseEntity<>(restaurantTo, HttpStatus.OK);
        }
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("Get all");
        List<Restaurant> restaurants = repository.findAllWithDishesByDate(DateTimeUtil.currentDate());

        return toMapper.getTos(restaurants);
    }

    @PutMapping("/{id}/voted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id) {
        log.info("Vote for restaurant with id [{}]", id);
        User authUser = AuthenticationUtil.getAuthUser();
        voteService.vote(repository.getReferenceById(id), authUser); // TODO: 06.04.2023 Exception if illegal vote
    }
}