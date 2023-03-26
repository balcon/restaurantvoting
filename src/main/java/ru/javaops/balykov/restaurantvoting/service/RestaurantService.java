package ru.javaops.balykov.restaurantvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;

    public Restaurant get(int id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Restaurant> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    public List<Restaurant> getAllWithDishesByDate(LocalDate date) {
        return repository.findAllWithDishesAndVotesByDate(date);
    }

    public Restaurant getWithDishes(int id) {
        return repository.findByIdWithDishes(id);
    }

    public Restaurant save(Restaurant restaurant) {
        if (!restaurant.isNew()) {
            return null; // todo exception?
        }
        return repository.save(restaurant);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }
}