INSERT INTO RESTAURANT (NAME, DESCRIPTION, ADDRESS)
VALUES ('Tang-Zhen', 'Chinese cuisine', 'Nevsky Prospect 74'),
       ('KFC', 'Fast food', 'Sredniy Prospect, Vasilievsky island 38/40');

INSERT INTO DISH (NAME, DESCRIPTION, PRICE, RESTAURANT_ID)
VALUES ('Duck roasted in sweet soy sauce', 'Duck breast fillet, sweet soy sauce. Weight 300g', 50800, 1),
       ('Sanders Basket Lite', '3 strips, 3 hot wings, 6 bites', 19900, 2),
       ('Nuggets Box', null, 10400, 2)