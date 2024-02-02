insert into account(first_name, last_name, email, hash_password, phone_number, role, is_blocked, is_checked)
values
('James', 'Smith', 'james-smith@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491757778899', 'ADMIN', false, true),
('John', 'Doe', 'john-doe@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, true),
('Alice', 'Johnson', 'alice-johnson@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491112223334', 'CLIENT', false, true),
('Bob', 'Miller', 'bob-miller@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+497654321098', 'STOREKEEPER', false, true),
('Eva', 'Williams', 'eva-williams@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+490987654321', 'CLIENT', false, true),
('Charlie', 'Brown', 'charlie-brown@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, true),
('Emma', 'Garcia', 'emma-garcia@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, true),
('Daniel', 'Lee', 'daniel-lee@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, false),
('Sophia', 'Clark', 'sophia-clark@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, false),
('Oliver', 'Hall', 'oliver-hall@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, true);

INSERT INTO
    product (name)
VALUES
    ('Vintage Watch'),
    ('Antique Vase');

INSERT INTO
    auction (
    start_price,
    start_at,
    planned_end_at,
    actual_end_at,
    state,
    product_id,
    winner_id,
    updated_at,
    created_at
)
VALUES
    (
        500,
        '2024-01-01T10:00:00',
        '2024-01-10T22:00:00',
        NULL,
        'PENDING',
        1,
        null,
        '2024-01-01T10:00:00',
        '2024-01-01T10:00:00'
    ),
    (
        800,
        '2024-01-02T11:00:00',
        '2024-01-12T23:00:00',
        NULL,
        'PENDING',
        2,
        null,
        '2024-01-02T11:00:00',
        '2024-01-02T11:00:00'
    );

INSERT INTO
    bet (user_id, auction_id, bet_amount, created_at)
VALUES
    (1, 1, 550, '2024-01-03T12:00:00'),
    (2, 1, 600, '2024-01-04T13:00:00');