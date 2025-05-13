USE spring_api;

-- 1) Define carts.id as a 36‐char UUID string
CREATE TABLE carts (
    id           CHAR(36)    NOT NULL,
    date_created DATE         NOT NULL DEFAULT CURRENT_DATE(),
    PRIMARY KEY (id)
);

-- INSERT INTO carts (id)
-- VALUES (UUID());

-- 2) cart_items referencing the CHAR(36) carts.id
CREATE TABLE cart_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id    CHAR(36)         NOT NULL,
    product_id BIGINT           NOT NULL,
    quantity   INT              NOT NULL DEFAULT 1,
    UNIQUE KEY cart_items_cart_product_unique (cart_id, product_id),
    CONSTRAINT cart_items_carts_id_fk    FOREIGN KEY (cart_id)    REFERENCES carts(id)    ON DELETE CASCADE,
    CONSTRAINT cart_items_products_id_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
