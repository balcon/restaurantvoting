package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RestaurantService {
    public static final String RESTAURANTS_WITH_DISHES_CACHE = "restaurantsWithDishes";

    private final RestaurantRepository repository;

    public Restaurant create(Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Restaurant getWithDishes(int id) {
        return repository.findByIdWithDishesByDate(id, DateTimeUtil.currentDate())
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Restaurant> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, key = "#offerDate")
    public List<Restaurant> getAllWithDishes(LocalDate offerDate) {
        return repository.findAllWithDishesByDate(offerDate);
    }

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public void update(int id, Restaurant restaurant) {
        ValidationUtil.assureIdConsistent(restaurant, id);
        checkIfExists(id);
        repository.save(restaurant);
    }

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public void delete(int id) {
        checkIfExists(id);
        repository.deleteById(id);
    }

    public void checkIfExists(int id) {
        ValidationUtil.checkIfExists(repository, id);
    }
}