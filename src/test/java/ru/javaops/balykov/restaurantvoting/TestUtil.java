package ru.javaops.balykov.restaurantvoting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {
    public static final Restaurant restaurant1 = new Restaurant("Restaurant 1", "Address 1");
    public static final List<Dish> dishes = List.of(new Dish("d1", 100), new Dish("d2", 200), new Dish("d3", 300));
    static {
        restaurant1.setDishes(dishes);
    }
}
