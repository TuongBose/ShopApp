CREATE TABLE favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    FOREIGN KEY (user_id) REFERENCES accounts(USERID),
    FOREIGN KEY (product_id) REFERENCES sanpham(MASANPHAM)
);