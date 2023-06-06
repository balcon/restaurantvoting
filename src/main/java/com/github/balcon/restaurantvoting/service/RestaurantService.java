package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
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

    public List<Restaurant> getAllWithDishes(Sort sort) {
        log.info("Get all restaurant with current dishes with sort [{}]", sort);
        return repository.findAllWithDishesByDate(DateTimeUtil.currentDate(), sort);
    }

    @Transactional
    public void update(int id, Restaurant restaurant) {
        log.info("Update restaurant with id [{}] new values [{}]", id, restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        checkIfExists(id);
        repository.save(restaurant);
    }

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