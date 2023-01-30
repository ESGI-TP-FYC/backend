INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price)
    VALUES (1, 'Wall Street1', 'New York', '2021-04-07', 'test123@test.com', 'John', 'Doe', '1234567890', 1234567890, 840);

INSERT INTO order_item (id, amount, quantity, product_id) VALUES (1, 384, 2, 33);

INSERT INTO orders_order_items (order_id, order_items_id) VALUES (1, 1);


