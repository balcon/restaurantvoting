package ru.javaops.balykov.restaurantvoting;

import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

public class TestUtil {
    public static final Restaurant restaurant1 = new Restaurant("Restaurant 1", "Description 1", "Address 1");
    public static final List<Dish> dishes = List.of(
            new Dish("d1", "d1", 100),
            new Dish("d2", "d2", 200),
            new Dish("d3", "d3", 300));
}
