-- use your database (create if needed)
CREATE DATABASE IF NOT EXISTS spring_api
    /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE spring_api;

-- 1) users must come first, since profiles & addresses reference it
CREATE TABLE users (
   id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
   name     VARCHAR(255)     NOT NULL,
   email    VARCHAR(255)     NOT NULL,
   password VARCHAR(255)     NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- 2) profiles uses the same id as users (one-to-one)
CREATE TABLE profiles (
    id             BIGINT            NOT NULL PRIMARY KEY,
    bio            LONGTEXT               NULL,
    phone_number   VARCHAR(20)           NULL,
    date_of_birth  DATE                  NULL,
    loyalty_points INT UNSIGNED  DEFAULT 0 NULL,
    CONSTRAINT fk_profile_user
      FOREIGN KEY (id) REFERENCES users(id)
          ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- 3) addresses also references users
CREATE TABLE addresses (
    id       BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    street   VARCHAR(255)     NOT NULL,
    city     VARCHAR(255)     NOT NULL,
    state    VARCHAR(255)     NOT NULL,
    zip      VARCHAR(255)     NOT NULL,
    user_id  BIGINT           NOT NULL,
    CONSTRAINT fk_address_user
       FOREIGN KEY (user_id) REFERENCES users(id)
           ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- 4) categories stands alone
CREATE TABLE categories (
    id   TINYINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255)            NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- 5) products references categories
CREATE TABLE products (
    id           BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name         VARCHAR(255)       NOT NULL,
    price        DECIMAL(10,2)      NOT NULL,
    description  LONGTEXT           NOT NULL,
    category_id  TINYINT UNSIGNED   NULL,
    CONSTRAINT fk_product_category
      FOREIGN KEY (category_id) REFERENCES categories(id)
          ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

-- 6) wishlist references both products and users
CREATE TABLE wishlist (
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    PRIMARY KEY (product_id, user_id),
    CONSTRAINT fk_wishlist_product
      FOREIGN KEY (product_id) REFERENCES products(id)
          ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_user
      FOREIGN KEY (user_id) REFERENCES users(id)
          ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;
