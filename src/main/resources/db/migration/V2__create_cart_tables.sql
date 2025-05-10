USE spring_api;

-- 1) carts: id as BINARY(16) PK, date_created defaults to today
CREATE TABLE carts (
    id            BINARY(16) NOT NULL PRIMARY KEY,
    date_created  DATE       NOT NULL DEFAULT (CURDATE())
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;

DELIMITER $$
CREATE TRIGGER trg_carts_before_insert
    BEFORE INSERT ON carts
    FOR EACH ROW
BEGIN
    -- if client didn’t supply an id, generate one
    IF NEW.id IS NULL
        OR NEW.id = x'00000000000000000000000000000000' THEN
        SET NEW.id = UUID_TO_BIN(UUID());
    END IF;
END$$
DELIMITER ;

-- 2) cart_items: FK → carts(id) and products(id)
CREATE TABLE cart_items (
    id         BIGINT       AUTO_INCREMENT PRIMARY KEY,
    cart_id    BINARY(16)   NOT NULL,
    product_id BIGINT       NOT NULL,
    quantity   INT          NOT NULL DEFAULT 1,
    CONSTRAINT uq_cart_product UNIQUE (cart_id, product_id),
    CONSTRAINT fk_ci_cart FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ci_prod FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;
