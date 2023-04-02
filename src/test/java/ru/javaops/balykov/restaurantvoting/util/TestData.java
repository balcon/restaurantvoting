package ru.javaops.balykov.restaurantvoting.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.model.User;

import java.util.List;
import java.util.Set;

import static ru.javaops.balykov.restaurantvoting.model.Role.ROLE_ADMIN;
import static ru.javaops.balykov.restaurantvoting.model.Role.ROLE_USER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {
    // Dishes
    public static final int DISH_1_ID = 3;
    public static final Dish DISH_1 =
            new Dish(DISH_1_ID, "Duck roasted in sweet soy sauce", 50800);
    public static final Dish DISH_2 =
            new Dish(4, "Soup with tomatoes and eggs", 15800);
    public static final List<Dish> REST_1_DISHES = List.of(DISH_1, DISH_2);
    public static final List<Dish> REST_2_DISHES = List.of(
            new Dish(5, "Sanders Basket Lite", 19900),
            new Dish(6, "Nuggets Box", 10400));

    // Restaurants
    public static final int REST_1_ID = 1;

    public static final Restaurant REST_1 =
            new Restaurant(REST_1_ID, "Tang-Zhen", "Nevsky Prospect 74", REST_1_DISHES);
    public static final Restaurant REST_2 =
            new Restaurant(2, "KFC", "Sredniy Prospect, Vasilievsky island 38/40", REST_2_DISHES);
    public static final Restaurant REST_3 =
            new Restaurant(3, "Without today's dish", "Any address", null);

    public static final Dish YESTERDAYS_DISH =
            new Dish(50, "Yesterday's dish", 100, DateTimeUtil.currentDate().minusDays(1));

    public static final Restaurant NEW_REST =
            new Restaurant("New restaurant", "New Address");

    // Users
    public static final int USER_ID = 20;
    public static final int ADMIN_ID = 21;
    public static final User USER =
            new User(USER_ID, "User", "user@mail.ru", "password", Set.of(ROLE_USER));
    public static final User ADMIN =
            new User(ADMIN_ID, "Admin", "admin@mail.ru", "password", Set.of(ROLE_USER, ROLE_ADMIN));
    public static final List<User> USERS = List.of(ADMIN, USER);

    public static final User NEW_USER = new User("New user", "new_mail@mail.ru", "secret");
    public static final int NON_EXISTENT_ID = 29;
}
