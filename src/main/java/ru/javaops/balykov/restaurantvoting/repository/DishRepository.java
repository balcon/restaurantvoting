package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findAllByRestaurantIdAndOfferDate(int restaurantId, LocalDate date);
}