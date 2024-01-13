INSERT INTO account(first_name, last_name, email, hash_password)
VALUES ('alex', 'schmidt', 'alex-schmidt@mail.com', 'qwerty007'),
       ('ben', 'fischer', 'ben-fischer@mail.com', 'qwerty007');

INSERT INTO product (name)
VALUES ('Vintage Watch'),
       ('Antique Vase');

INSERT INTO auction (number, start_price, start_at, planned_end_at, actual_end_at, state, product_id, winner_id,
                     update_at, created_at)
VALUES ('AU1001', 500, '2024-01-01T10:00:00', '2024-01-10T22:00:00', NULL, 'PENDING', 1, null, '2024-01-01T10:00:00',
        '2024-01-01T10:00:00'),
       ('AU1002', 800, '2024-01-02T11:00:00', '2024-01-12T23:00:00', NULL, 'PENDING', 2, null, '2024-01-02T11:00:00',
        '2024-01-02T11:00:00');

INSERT INTO bet (user_id, auction_id, bet_amount, created_at)
VALUES (1, 1, 550, '2024-01-03T12:00:00'),
       (2, 1, 600, '2024-01-04T13:00:00');

