package com.github.balcon.restaurantvoting.util;

import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import lombok.experimental.UtilityClass;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.model.Restaurant;

import java.util.List;
import java.util.Set;

@UtilityClass
public class TestData {
    // Dishes
    public final int DISH_1_ID = 3;
    public final int DISH_3_ID = 5;
    public final Dish DISH_1 =
            new Dish(DISH_1_ID, "Duck roasted in sweet soy sauce", 50800);
    public final Dish DISH_2 =
            new Dish(4, "Soup with tomatoes and eggs", 15800);
    public final List<Dish> REST_1_DISHES = List.of(DISH_1, DISH_2);
    public final List<Dish> REST_2_DISHES = List.of(
            new Dish(DISH_3_ID, "Sanders Basket Lite", 19900),
            new Dish(6, "Nuggets Box", 10400));

    // Restaurants
    public final int REST_1_ID = 1;
    public final int REST_2_ID = 2;

    public final Restaurant REST_1 =
            new Restaurant(REST_1_ID, "Tang-Zhen", "Nevsky Prospect 74", REST_1_DISHES);
    public final Restaurant REST_2 =
            new Restaurant(REST_2_ID, "KFC", "Sredniy Prospect, Vasilievsky island 38/40", REST_2_DISHES);

    // Users
    public final int USER_ID = 20;
    public final int ADMIN_ID = 21;
    public final String USER_EMAIL = "user@mail.ru";
    public final String ADMIN_EMAIL = "admin@mail.ru";
    public final User USER =
            new User(USER_ID, "User", USER_EMAIL, "password", Set.of(Role.USER));
    public final User ADMIN =
            new User(ADMIN_ID, "Admin", ADMIN_EMAIL, "password", Set.of(Role.USER, Role.ADMIN));

    // New entities
    public final Dish NEW_DISH = new Dish(100, "New dish", 100);
    public final Restaurant NEW_RESTAURANT = new Restaurant(100, "New Rest", "New Address");
    public final User NEW_USER = new User(100, "New admin", "new_mail@mail.ru", "newpassword", Role.DEFAULT_ROLES);
}