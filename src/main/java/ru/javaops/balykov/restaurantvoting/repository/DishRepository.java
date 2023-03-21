package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findAllByRestaurantIdAndOfferDate(int restaurantId, LocalDate date);
}