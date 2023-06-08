package com.github.balcon.restaurantvoting.repository;

import com.github.balcon.restaurantvoting.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Optional<Restaurant> findByNameIgnoreCaseAndAddressIgnoreCase(String name, String address);

    @Query("FROM Restaurant r LEFT JOIN FETCH r.dishes dish WHERE dish.offerDate = :date")
    List<Restaurant> findAllWithDishesByDate(LocalDate date);

    @Query("FROM Restaurant r LEFT JOIN FETCH r.dishes dish WHERE r.id=:id AND dish.offerDate=:offerDate")
    Optional<Restaurant> findByIdWithDishesByDate(int id, LocalDate offerDate, Sort sort);
}