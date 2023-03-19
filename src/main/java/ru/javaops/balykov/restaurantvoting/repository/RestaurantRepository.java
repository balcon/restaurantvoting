package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.offerDate = CURRENT_DATE")
    List<Restaurant> findAllWithTodayDishes();
}
