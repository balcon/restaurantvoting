package ru.javaops.balykov.restaurantvoting;

import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.util.List;

public class DemoData {
    public static final List<Dish> DISHES_1 = List.of(
            new Dish(3, "Duck roasted in sweet soy sauce", "Duck breast fillet, sweet soy sauce. Weight 300g", 50800),
            new Dish(4, "Soup with tomatoes and eggs", "Tomato, egg, cucumber, salt, starch, sesame oil", 15800));
    public static final List<Dish> DISHES_2 = List.of(
            new Dish(5, "Sanders Basket Lite", "3 strips, 3 hot wings, 6 bites", 19900),
            new Dish(6, "Nuggets Box", 10400));

    public static final Restaurant REST_1 = new Restaurant(1, "Tang-Zhen", "Chinese cuisine", "Nevsky Prospect 74", DISHES_1);
    public static final Restaurant REST_2 = new Restaurant(2, "KFC", "Fast food", "Sredniy Prospect, Vasilievsky island 38/40", DISHES_2);
}
