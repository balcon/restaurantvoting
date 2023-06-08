package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.balcon.restaurantvoting.service.VoteService.VOTES_CACHE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    public static final String RESTAURANTS_WITH_DISHES_CACHE = "restaurantsWithDishes";

    private final RestaurantRepository repository;

    public Restaurant create(Restaurant restaurant) {
        log.info("Create new restaurant [{}]", restaurant);
        ValidationUtil.checkNew(restaurant);
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        log.info("Get restaurant with id [{}]", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Restaurant getWithDishes(int id, Sort sort) {
        log.info("Get restaurant with id [{}] with current dishes with sort [{}]", id, sort);
        return repository.findByIdWithDishesByDate(id, DateTimeUtil.currentDate(), sort)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Restaurant> getAll(Pageable pageable) {
        log.info("Get all restaurant with pageable [{}]", pageable);
        return repository.findAll(pageable);
    }

    @Cacheable(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, key = "#offerDate")
    public List<Restaurant> getAllWithDishes(LocalDate offerDate) {
        log.info("Get all restaurant with dishes by date [{}]", offerDate);
        return repository.findAllWithDishesByDate(offerDate);
    }

    @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true)
    @Transactional
    public void update(int id, Restaurant restaurant) {
        log.info("Update restaurant with id [{}] new values [{}]", id, restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        checkIfExists(id);
        repository.save(restaurant);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = RESTAURANTS_WITH_DISHES_CACHE, allEntries = true),
            @CacheEvict(cacheNames = VOTES_CACHE, allEntries = true)})
    @Transactional
    public void delete(int id) {
        log.info("Delete restaurant with id [{}]", id);
        checkIfExists(id);
        repository.deleteById(id);
    }

    public void checkIfExists(int id) {
        ValidationUtil.checkIfExists(repository, id);
    }
}