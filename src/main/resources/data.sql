INSERT INTO products (sku, name, price, created_at, is_deleted)
VALUES
('1111111', 'Banana', 1.99, {ts '2020-01-01'}, false),
('2222222', 'Apple', 1.29, {ts '2020-01-01'}, false),
('3333333', 'Kiwi', 0.69, {ts '2020-01-01'}, false);

INSERT INTO orders (order_id, buyer_email, created_at)
VALUES
(1, 'tom@decoded.de', {ts '2020-01-01 12:00:00.00'}),
(2, 'tom@decoded.de', {ts '2020-01-01 13:00:00.00'}),
(3, 'tom@decoded.de', {ts '2020-02-01 12:00:00.00'}),
(4, 'tom@decoded.de', {ts '2020-03-01 12:00:00.00'}),
(5, 'tom@decoded.de', {ts '2020-03-01 13:04:00.00'});

INSERT INTO order_items (order_item_id, fk_order, fk_product, amount)
VALUES
(1, 1, '1111111', 3),
(2, 2, '2222222', 5),
(3, 2, '3333333', 9),
(4, 3, '1111111', 7),
(5, 3, '3333333', 2),
(6, 4, '2222222', 11),
(7, 5, '1111111', 100),
(8, 5, '2222222', 100),
(9, 5, '3333333', 100);
