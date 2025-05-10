CREATE TABLE addresses (
    id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    street   VARCHAR(255) NOT NULL,
    city     VARCHAR(255) NOT NULL,
    state    VARCHAR(255) NOT NULL,
    zip      VARCHAR(255) NOT NULL,
    user_id  BIGINT NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE categories (
    id   TINYINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    price        DECIMAL(10, 2) NOT NULL,
    description  LONGTEXT NOT NULL,
    category_id  TINYINT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE profiles (
    id             BIGINT NOT NULL PRIMARY KEY,
    bio            LONGTEXT,
    phone_number   VARCHAR(20),
    date_of_birth  DATE,
    loyalty_points INT UNSIGNED DEFAULT 0,
    CONSTRAINT fk_profile_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE wishlist (
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    PRIMARY KEY (product_id, user_id),
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);
