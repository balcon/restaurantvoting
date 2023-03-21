INSERT INTO RESTAURANT (ID, NAME, DESCRIPTION, ADDRESS)
VALUES (1, 'Tang-Zhen', 'Chinese cuisine', 'Nevsky Prospect 74'),
       (2, 'KFC', 'Fast food', 'Sredniy Prospect, Vasilievsky island 38/40');

INSERT INTO DISH (ID, NAME, DESCRIPTION, PRICE, RESTAURANT_ID)
VALUES (3, 'Duck roasted in sweet soy sauce', 'Duck breast fillet, sweet soy sauce. Weight 300g', 50800, 1),
       (4, 'Soup with tomatoes and eggs', 'Tomato, egg, cucumber, salt, starch, sesame oil', 15800, 1),
       (5, 'Sanders Basket Lite', '3 strips, 3 hot wings, 6 bites', 19900, 2),
       (6, 'Nuggets Box', NULL, 10400, 2);

-- Yesterday's dishes
INSERT INTO DISH (ID, NAME, PRICE, RESTAURANT_ID, OFFER_DATE)
VALUES (90, 'Yesterday''s dish', 100, 1, CURRENT_DATE() - 1),
       (91, 'Yesterday''s dish', 100, 2, CURRENT_DATE() - 1);