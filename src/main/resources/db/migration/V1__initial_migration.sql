CREATE DATABASE IF NOT EXISTS spring_api;
USE spring_api;

-- 1) Create users first, so later FKs to users(id) are valid
CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 2) Now you can safely create addresses -> users(id)
CREATE TABLE addresses (
    id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    street   VARCHAR(255) NOT NULL,
    city     VARCHAR(255) NOT NULL,
    state    VARCHAR(255) NOT NULL,
    zip      VARCHAR(255) NOT NULL,
    user_id  BIGINT NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- 3) Categories before products (already OK)
CREATE TABLE categories (
    id   TINYINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- 4) Products -> categories(id)
CREATE TABLE products (
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    price        DECIMAL(10, 2) NOT NULL,
    description  LONGTEXT NOT NULL,
    category_id  TINYINT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- 5) Profiles -> users(id)
CREATE TABLE profiles (
    id             BIGINT NOT NULL PRIMARY KEY,
    bio            LONGTEXT,
    phone_number   VARCHAR(20),
    date_of_birth  DATE,
    loyalty_points INT UNSIGNED DEFAULT 0,
    CONSTRAINT fk_profile_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE RESTRICT
);

-- 6) Wishlist -> products(id) and users(id)
CREATE TABLE wishlist (
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    PRIMARY KEY (product_id, user_id),
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE RESTRICT
);
