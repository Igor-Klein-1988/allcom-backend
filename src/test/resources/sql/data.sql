insert into account(first_name, last_name, email, hash_password, phone_number, role, is_checked, is_blocked)
values
('James', 'Smith', 'james-smith@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491757778899', 'ADMIN', true, false),
('John', 'Doe', 'john-doe@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, false),
('Alice', 'Johnson', 'alice-johnson@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491112223334', 'CLIENT', true, false),
('Bob', 'Miller', 'bob-miller@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+497654321098', 'STOREKEEPER', true, false),
('Eva', 'Williams', 'eva-williams@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+490987654321', 'CLIENT', true, false),
('Charlie', 'Brown', 'charlie-brown@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, false),
('Emma', 'Garcia', 'emma-garcia@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, false),
('Daniel', 'Lee', 'daniel-lee@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, false),
('Sophia', 'Clark', 'sophia-clark@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', false, false),
('Oliver', 'Hall', 'oliver-hall@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', '+491234567890', 'CLIENT', true, true);