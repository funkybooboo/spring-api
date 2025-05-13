USE spring_api;

-- 1) Users (with role and bcrypt-hashed passwords)
INSERT INTO users (name, email, password, role) VALUES
    ('Alice Smith',   'alice@example.com',   '$2a$10$hD6sMNkqobtlcMztrDnUReS12YKAzDJPvB1lMQixMy4JqaEyRKKCa', 'ADMIN'),
    ('Bob Johnson',   'bob@example.com',     '$2a$10$fZxsEzWxTh2AxV5UgH7AuOQbl6sK8VOqKoONauZKprrx0HJ58Y5AS', 'USER'),
    ('Carol Williams','carol@example.com',   '$2a$10$UMr1PiOiM0wIuYg95y5JOe3QSRvvCvq3a0XDYMRlo1AMHcuXj2dGu', 'USER'),
    ('Dave Brown',    'dave@example.com',    '$2a$10$9TqzjND3m.cG/zlTgq.GxuDjJvTxfPrQ/P3.SBzF/zkBJkxS9Ajde', 'USER'),
    ('Eve Davis',     'eve@example.com',     '$2a$10$sARh0sXpqFwPaRYRpQ4NR.iRZQROkn7e6qS0Z8P9MN13FZSnAzD.a', 'USER');

-- 2) Addresses (one per user)
INSERT INTO addresses (street, city, state, zip, user_id) VALUES
    ('123 Maple St',     'Springfield', 'IL', '62701',  1),
    ('456 Oak Ave',      'Rivertown',   'CA', '90210',  2),
    ('789 Pine Rd',      'Laketown',    'TX', '73301',  3),
    ('321 Birch Blvd',   'Hillview',    'NY', '10001',  4),
    ('654 Cedar Lane',   'Forest City', 'WA', '98101',  5);

-- 3) Profiles
INSERT INTO profiles (id, bio, phone_number, date_of_birth, loyalty_points) VALUES
    (1, 'Enthusiastic shopper and cat lover.',     '555-1234', '1990-02-15', 120),
    (2, 'Tech geek and coffee addict.',           '555-2345', '1985-07-30',  85),
    (3, 'Bookworm and traveler.',                 '555-3456', '1992-11-05',  45),
    (4, 'Fitness enthusiast and blogger.',        '555-4567', '1988-01-20', 200),
    (5, 'Foodie exploring new cuisines.',         '555-5678', '1995-06-10',  60);

-- 4) Categories
INSERT INTO categories (name) VALUES
    ('Electronics'),
    ('Books'),
    ('Clothing');

-- 5) Products
INSERT INTO products (name, price, description, category_id) VALUES
    ('Wireless Mouse',        25.99,  'Ergonomic wireless mouse',       1),
    ('USB-C Charger',         19.50,  'Fast-charging USB-C adapter',    1),
    ('“The Great Novel”',     12.99,  'A thrilling page-turner',        2),
    ('Noise-Cancel Headphones',89.00, 'Over-ear noise-canceling',       1),
    ('Yoga Pants',            35.00,  'Stretch-fit yoga pants',         3),
    ('Graphic T-Shirt',       20.00,  '100% cotton with cool print',    3);

-- 6) Wishlist entries
INSERT INTO wishlist (product_id, user_id) VALUES
    (2, 1),  -- Alice wants the USB-C Charger
    (3, 1),  -- Alice wants the novel
    (1, 2),  -- Bob wants the Wireless Mouse
    (4, 3),  -- Carol wants Headphones
    (5, 4),  -- Dave wants Yoga Pants
    (6, 5);  -- Eve wants the T-Shirt

-- 7) Carts and cart_items using fixed UUIDs
INSERT INTO carts (id) VALUES
    ('6cc7b1b7-27b2-494a-ae2d-241c69f8cba6'),
    ('6582e10e-3f99-439a-b1f5-b303efb4104d');

INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
    ('6cc7b1b7-27b2-494a-ae2d-241c69f8cba6', 1, 2),  -- cart1: 2× Wireless Mouse
    ('6cc7b1b7-27b2-494a-ae2d-241c69f8cba6', 3, 1),  -- cart1: 1× The Great Novel
    ('6582e10e-3f99-439a-b1f5-b303efb4104d', 2, 1),  -- cart2: 1× USB-C Charger
    ('6582e10e-3f99-439a-b1f5-b303efb4104d', 5, 3);  -- cart2: 3× Yoga Pants

-- 8) Orders
INSERT INTO orders (customer_id, status, total_price) VALUES
    (1, 'PLACED', 64.97),   -- Alice: 2x Mouse + 1x Novel
    (2, 'SHIPPED', 19.50),  -- Bob: 1x USB-C Charger
    (3, 'DELIVERED', 124.00); -- Carol: 1x Headphones + 1x T-Shirt

-- 9) Order Items
-- Alice's order (id = 1)
INSERT INTO order_items (order_id, product_id, unit_price, quantity, total_price) VALUES
    (1, 1, 25.99, 2, 51.98),  -- 2x Wireless Mouse
    (1, 3, 12.99, 1, 12.99);  -- 1x The Great Novel
-- Bob's order (id = 2)
INSERT INTO order_items (order_id, product_id, unit_price, quantity, total_price) VALUES
    (2, 2, 19.50, 1, 19.50);  -- 1x USB-C Charger
-- Carol's order (id = 3)
INSERT INTO order_items (order_id, product_id, unit_price, quantity, total_price) VALUES
    (3, 4, 89.00, 1, 89.00),  -- 1x Headphones
    (3, 6, 35.00, 1, 35.00);  -- 1x T-Shirt
