package ru.javaops.balykov.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository repository;

    public Dish save(Dish dish) {
        return repository.save(dish);
    }

    public List<Dish> saveAll(List<Dish> dishes) {
        return repository.saveAll(dishes);
    }
}
