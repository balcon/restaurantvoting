package ru.javaops.balykov.restaurantvoting.to;

import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Vote;

import java.util.List;

public record RestaurantTo(Integer id,
                           String name,
                           String address,
                           List<Dish> dishes,
                           List<Vote> votes) {
}