CREATE TABLE favorites (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_id INT,
    FOREIGN KEY (user_id) REFERENCES accounts(USERID),
    FOREIGN KEY (product_id) REFERENCES sanpham(MASANPHAM)
);