package ru.javaops.balykov.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;

    public Restaurant get(int id) {
        return repository.findById(id).orElseThrow();
    }

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }
}
