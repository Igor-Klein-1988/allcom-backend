insert into account(id, first_name, last_name, email, hash_password, is_blocked, phone_number, role)
values
('1','James', 'Smith', 'james-smith@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491757778899', 'ADMIN'),
('2', 'John', 'Doe', 'john-doe@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491234567890', 'CLIENT'),
('3', 'Alice', 'Johnson', 'alice-johnson@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491112223334', 'CLIENT'),
('4', 'Bob', 'Miller', 'bob-miller@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+497654321098', 'STOREKEEPER'),
('5', 'Eva', 'Williams', 'eva-williams@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+490987654321', 'CLIENT'),
('6', 'Charlie', 'Brown', 'charlie-brown@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491234567890', 'CLIENT'),
('7', 'Emma', 'Garcia', 'emma-garcia@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491234567890', 'CLIENT'),
('8', 'Daniel', 'Lee', 'daniel-lee@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491234567890', 'CLIENT'),
('9', 'Sophia', 'Clark', 'sophia-clark@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', false, '+491234567890', 'CLIENT'),
('10', 'Oliver', 'Hall', 'oliver-hall@mail.com', '$2a$10$g0.iMQ0YzlVgxZxKe5RNuOCszxz81LovGoXy7eEvKhLsoQibjcxIe', true, '+491234567890', 'CLIENT');