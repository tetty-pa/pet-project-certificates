INSERT INTO tags(id, name)
VALUES (1, '1'),
       (2, '2'),
       (3, '3');

INSERT INTO gift_certificates(id, name, description, price, duration, create_date, last_updated_date)
VALUES (1, '1', '1', 1, 1, '2023-01-04 12:07:19', '2023-01-04 12:07:19'),
       (2, '2', '2', 2, 2, '2023-01-04 12:07:19', '2023-01-04 12:07:19'),
       (3, '3', '3', 3, 3, '2023-01-04 12:07:19', '2023-01-04 12:07:19');

INSERT INTO gift_certificate_has_tag(gift_certificate_id, tag_id)
VALUES (1, 2),
       (1, 1);

INSERT INTO users(id, name)
VALUES (1, 'user1'),
       (2, 'user2'),
       (3, 'user3');


INSERT INTO orders(id, user_id, gift_certificate_id, order_date, cost)
VALUES (1, 1, 1, '2023-01-04 12:07:19', 1),
       (2, 1, 2, '2023-01-04 12:07:19', 2),
       (3, 2, 2, '2023-01-04 12:07:19', 2)
