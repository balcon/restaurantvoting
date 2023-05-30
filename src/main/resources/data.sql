INSERT INTO RESTAURANT (ID, NAME, ADDRESS)
VALUES (1, 'Tang-Zhen', 'Nevsky Prospect 74'),
       (2, 'KFC', 'Sredniy Prospect, Vasilievsky island 38/40'),
       (3, 'Tang-Zhen', 'Bolshoy Prospect, Vasilievskiy island 19');

INSERT INTO DISH (ID, NAME, PRICE, RESTAURANT_ID)
VALUES (3, 'Duck roasted in sweet soy sauce', 50800, 1),
       (4, 'Soup with tomatoes and eggs', 15800, 1),
       (5, 'Sanders Basket Lite', 19900, 2),
       (6, 'Nuggets Box', 10400, 2);

-- Yesterday's dishes
INSERT INTO DISH (ID, NAME, PRICE, RESTAURANT_ID, OFFER_DATE)
VALUES (50, 'Yesterday''s dish', 100, 1, CURRENT_DATE - 1),
       (51, 'Yesterday''s dish', 100, 3, CURRENT_DATE - 1);

INSERT INTO USERS (ID, NAME, EMAIL, PASSWORD)
VALUES (20, 'User', 'user@mail.ru', '{noop}password'),
       (21, 'Admin', 'admin@mail.ru', '{noop}password');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (20, 'USER'),
       (21, 'USER'),
       (21, 'ADMIN');

INSERT INTO VOTE (ID, RESTAURANT_ID, USER_ID, VOTE_DATE)
VALUES (70, 1, 21, CURRENT_DATE),
       (71, 2, 20, CURRENT_DATE - 1),
       (72, 2, 21, CURRENT_DATE - 1);