package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.repository.DishRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.balcon.restaurantvoting.service.RestaurantService.RESTAURANTS_WITH_DISHES_CACHE;
import static java.util.Objects.requireNonNullElse;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository repository;
    private final RestaurantService restaurantService;

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public Dish create(int restaurantId, Dish dish) {
        ValidationUtil.checkNew(dish);
        dish.setRestaurant(restaurantService.get(restaurantId));
        return repository.save(dish);
    }

    public Dish get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public List<Dish> getAll(int restaurantId, LocalDate offerDate, Sort sort) {
        restaurantService.checkIfExists(restaurantId);
        return repository.findAllByRestaurantIdAndOfferDate(
                restaurantId, requireNonNullElse(offerDate, DateTimeUtil.currentDate()), sort);
    }

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public void update(int id, Dish dish) {
        ValidationUtil.assureIdConsistent(dish, id);
        ValidationUtil.checkIfExists(repository, id);
        dish.setRestaurant(this.get(id).getRestaurant());
        repository.save(dish);
    }

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public void delete(int id) {
        ValidationUtil.checkIfExists(repository, id);
        repository.deleteById(id);
    }
}