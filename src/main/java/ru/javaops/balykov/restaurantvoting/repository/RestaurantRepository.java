package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
