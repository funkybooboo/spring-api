USE spring_api;

-- 1) Users
INSERT INTO users (name, email, password) VALUES
    ('Alice Smith',   'alice@example.com',   'password1'),
    ('Bob Johnson',   'bob@example.com',     'password2'),
    ('Carol Williams','carol@example.com',   'password3'),
    ('Dave Brown',    'dave@example.com',    'password4'),
    ('Eve Davis',     'eve@example.com',     'password5');

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

-- 7) Carts and cart_items
--    Create two carts and capture their IDs
SET @cart1 = UUID();
SET @cart2 = UUID();
INSERT INTO carts (id) VALUES (@cart1), (@cart2);

--    Add items to the carts
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
    (@cart1, 1, 2),  -- cart1: 2× Wireless Mouse
    (@cart1, 3, 1),  -- cart1: 1× The Great Novel
    (@cart2, 2, 1),  -- cart2: 1× USB-C Charger
    (@cart2, 5, 3);  -- cart2: 3× Yoga Pants
