package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
