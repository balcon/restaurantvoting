package ru.javaops.balykov.restaurantvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    public Dish get(int id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Dish> getAllByDate(int id, LocalDate date) {
        return repository.findAllByRestaurantIdAndOfferDate(id, date);
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew()) {
            return null; // todo exception?!
        }
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return repository.save(dish);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public void update(Dish dish) {
        repository.save(dish);
    }
}