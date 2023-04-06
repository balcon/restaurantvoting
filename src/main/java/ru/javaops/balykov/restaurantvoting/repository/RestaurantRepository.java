package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.offerDate = :date ORDER BY r.name")
    List<Restaurant> findAllWithDishesByDate(LocalDate date); // TODO: 06.04.2023 order -> sort.of

    @Query("FROM Restaurant r LEFT JOIN FETCH r.dishes d " +
            "WHERE r.id=:id AND d.offerDate=:offerDate ORDER BY d.offerDate DESC")
    Optional<Restaurant> findByIdWithDishesByDate(int id, LocalDate offerDate); // TODO: 06.04.2023 order -> sort.of1
}