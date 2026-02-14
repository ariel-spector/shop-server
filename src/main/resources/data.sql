CREATE TABLE users(
first_name VARCHAR(255) NOT NULL,
last_name VARCHAR(255) NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL,
phone VARCHAR(20),
address VARCHAR(255),
username VARCHAR(255),
password VARCHAR(255) NOT NULL,
role VARCHAR(255) DEFAULT 'USER',
PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS items(
 item_id INT AUTO_INCREMENT,
 item_name VARCHAR(255) NOT NULL,
 item_price DOUBLE NOT NULL,
 item_quantity  INT NOT NULL,
 photo_url VARCHAR(500),
 brand_name VARCHAR(50),
 PRIMARY KEY(item_id)
);

CREATE TABLE IF NOT EXISTS  orders(
order_id INT AUTO_INCREMENT,
user_username VARCHAR(255) NOT NULL ,
order_placed DATE NOT NULL DEFAULT CURRENT_DATE,
shipping_address VARCHAR(50) NOT NULL ,
total_price DOUBLE NOT NULL  DEFAULT 0.0,
status VARCHAR(20) DEFAULT 'TEMP',
PRIMARY KEY(order_id),
FOREIGN KEY(user_username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS order_items(
id INT AUTO_INCREMENT PRIMARY KEY,

order_id INT NOT NULL,

item_id  INT NOT NULL,

quantity INT NOT NULL DEFAULT 1,

item_name VARCHAR(255) NOT NULL,

item_price DOUBLE NOT NULL,

photo_url VARCHAR(500),

FOREIGN KEY(order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
FOREIGN KEY(item_id) REFERENCES items(item_id)
);

CREATE TABLE favorites (
user_username VARCHAR(255) NOT NULL,

item_id INT NOT NULL,

PRIMARY KEY(user_username,item_id),
FOREIGN KEY(user_username) REFERENCES users(username) ON DELETE CASCADE,
FOREIGN KEY(item_id) REFERENCES items(item_id) ON DELETE CASCADE
);


INSERT INTO users (first_name, last_name, email, phone, address, username, password, role) VALUES
('ARIEL', 'SPECTOR', 'rhcp5981@gmail.com', '0526766366', 'Tnufa 8', 'Ariel1993', '$2a$10$AjXBjvS58fE1GP.eH1234uy1Yp4vYd0ZcYr2OVsWwczXlu/c2WVdW'	, 'ADMIN');


INSERT INTO items (item_name, item_price, item_quantity, brand_name, photo_url) VALUES
('Sony WH-1000XM5', 299.90, 25, 'Sony', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=1000&auto=format&fit=crop'),

('MacBook Pro M3', 2499.00, 10, 'Apple', 'https://www.zdnet.com/a/img/resize/f7273074a91e6b6e08d3c0280ab5014428ba2447/2023/11/06/ff2ab50d-93b8-4954-96e5-7176557f03b5/dsc02399-enhanced-nr.jpg?auto=webp&fit=crop&height=675&width=1200'),

('Nike Air Jordan', 149.99, 50, 'Nike', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000&auto=format&fit=crop'),

('Leather Jacket', 120.00, 15, 'Zara',' https://static.zara.net/assets/public/5601/6d6a/e8ed458cadee/3cf4160a06c5/05388900700-p/05388900700-p.jpg?ts=1770914452572&w=1024'),

('Apple Watch Series 9', 399.00, 30, 'Apple', 'https://images.unsplash.com/photo-1546868871-7041f2a55e12?q=80&w=1000&auto=format&fit=crop'),

('Gaming Mouse G502', 49.99, 100, 'Logitech', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=1000&auto=format&fit=crop'),

('Canon EOS Camera', 850.00, 8, 'Canon', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=1000&auto=format&fit=crop'),

('Ray-Ban Aviator', 160.00, 40, 'Ray-Ban', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?q=80&w=1000&auto=format&fit=crop');


