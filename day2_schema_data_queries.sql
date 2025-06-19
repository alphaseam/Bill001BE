-- Dump File for Hotel Billing System Integration


DROP DATABASE IF EXISTS hotel_billing;
CREATE DATABASE hotel_billing;
USE hotel_billing;

-- Table Definitions
CREATE TABLE hotels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    hotel_id BIGINT,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    hotel_id BIGINT,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE bills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME NOT NULL,
    hotel_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE bill_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    bill_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (bill_id) REFERENCES bills(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Indexes
CREATE INDEX idx_user_email ON users(username);
CREATE INDEX idx_user_hotel ON users(hotel_id);
CREATE INDEX idx_user_role ON users(role);
CREATE INDEX idx_bill_created_at ON bills(created_at);

-- Sample Data: Hotels
INSERT INTO hotels (name, address) VALUES 
('Taj Palace', 'Delhi'),
('The Oberoi', 'Mumbai'),
('Leela Palace', 'Bangalore');

-- Sample Data: Users
INSERT INTO users (username, password, role, hotel_id) VALUES 
('admin1', 'adminpass1', 'ADMIN', 1),
('staff1', 'staffpass1', 'STAFF', 1),
('admin2', 'adminpass2', 'ADMIN', 2),
('staff2', 'staffpass2', 'STAFF', 2);

-- Sample Data: Products
INSERT INTO products (name, price, hotel_id) VALUES 
('Paneer Butter Masala', 250.00, 1),
('Veg Biryani', 180.00, 1),
('Chicken Curry', 300.00, 2),
('Naan', 40.00, 2);

-- Sample Data: Bills
INSERT INTO bills (created_at, hotel_id, user_id) VALUES 
('2025-06-01 12:00:00', 1, 1),
('2025-06-02 18:30:00', 1, 2),
('2025-06-03 13:15:00', 2, 3);

-- Sample Data: Bill Items
INSERT INTO bill_items (quantity, bill_id, product_id) VALUES 
(2, 1, 1),
(1, 1, 2),
(3, 2, 1),
(2, 3, 3),
(4, 3, 4);

