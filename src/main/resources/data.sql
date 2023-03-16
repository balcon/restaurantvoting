INSERT INTO RESTAURANT (ID, NAME, DESCRIPTION, ADDRESS)
VALUES (1, 'Tang-Zhen', 'Chinese cuisine', 'Nevsky Prospect 74'),
       (2, 'KFC', 'Fast food', 'Sredniy Prospect, Vasilievsky island 38/40');

INSERT INTO DISH (ID, NAME, DESCRIPTION, PRICE, RESTAURANT_ID)
VALUES (3, 'Duck roasted in sweet soy sauce', 'Duck breast fillet, sweet soy sauce. Weight 300g', 50800, 1),
       (4, 'Sanders Basket Lite', '3 strips, 3 hot wings, 6 bites', 19900, 2),
       (5, 'Nuggets Box', NULL, 10400, 2)