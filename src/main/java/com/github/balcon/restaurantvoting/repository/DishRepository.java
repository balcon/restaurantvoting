package com.github.balcon.restaurantvoting.repository;

import com.github.balcon.restaurantvoting.model.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findAllByRestaurantIdAndOfferDate(int restaurantId, LocalDate date, Sort sort);
}