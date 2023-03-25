INSERT INTO RESTAURANT (ID, NAME, ADDRESS)
VALUES (1, 'Tang-Zhen', 'Nevsky Prospect 74'),
       (2, 'KFC', 'Sredniy Prospect, Vasilievsky island 38/40');

INSERT INTO DISH (ID, NAME, PRICE, RESTAURANT_ID)
VALUES (3, 'Duck roasted in sweet soy sauce', 50800, 1),
       (4, 'Soup with tomatoes and eggs', 15800, 1),
       (5, 'Sanders Basket Lite', 19900, 2),
       (6, 'Nuggets Box', 10400, 2);

-- Yesterday's dishes
INSERT INTO DISH (ID, NAME, PRICE, RESTAURANT_ID, OFFER_DATE)
VALUES (50, 'Yesterday''s dish', 100, 1, CURRENT_DATE() - 1),
       (51, 'Yesterday''s dish', 100, 2, CURRENT_DATE() - 1);

INSERT INTO USERS (ID, NAME, EMAIL, PASSWORD)
VALUES (20, 'User', 'user@mail.ru', 'password'),
       (21, 'Admin', 'admin@mail.ru', 'password');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (20, 'ROLE_USER'),
       (21, 'ROLE_USER'),
       (21, 'ROLE_ADMIN');