package ru.javaops.balykov.restaurantvoting;

import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DemoData {
    public static final int DISH_1_ID = 3;
    public static final Dish DISH_1 =
            new Dish(DISH_1_ID, "Duck roasted in sweet soy sauce",
                    "Duck breast fillet, sweet soy sauce. Weight 300g", 50800);
    public static final Dish DISH_2 =
            new Dish(4, "Soup with tomatoes and eggs",
                    "Tomato, egg, cucumber, salt, starch, sesame oil", 15800);
    public static final List<Dish> REST_1_DISHES = List.of(DISH_1, DISH_2);
    public static final List<Dish> REST_2_DISHES = List.of(
            new Dish(5, "Sanders Basket Lite", "3 strips, 3 hot wings, 6 bites", 19900),
            new Dish(6, "Nuggets Box", 10400));

    public static final int REST_1_ID = 1;

    public static final Restaurant REST_1 =
            new Restaurant(REST_1_ID, "Tang-Zhen", "Chinese cuisine", "Nevsky Prospect 74", REST_1_DISHES);
    public static final Restaurant REST_2 =
            new Restaurant(2, "KFC", "Fast food", "Sredniy Prospect, Vasilievsky island 38/40", REST_2_DISHES);

    public static final Dish YESTERDAYS_DISH =
            new Dish(90, "Yesterday's dish", null, 100, LocalDate.now().minus(1, ChronoUnit.DAYS));
}
