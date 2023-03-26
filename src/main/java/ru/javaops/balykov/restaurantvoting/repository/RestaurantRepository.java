package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.offerDate = :date ORDER BY r.name")
    List<Restaurant> findAllWithDishesAndVotesByDate(LocalDate date);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE r.id=:id ORDER BY d.offerDate DESC")
    Restaurant findByIdWithDishes(int id);
}