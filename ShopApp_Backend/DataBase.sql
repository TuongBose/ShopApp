CREATE DATABASE shopapp;
USE shopapp;

-- TẠO BẢNG
CREATE TABLE loaisanpham 
(
    maloaisanpham INT PRIMARY KEY AUTO_INCREMENT,
    tenloaisanpham VARCHAR(255) NOT NULL
);

CREATE TABLE thuonghieu
(
    mathuonghieu INT PRIMARY KEY AUTO_INCREMENT,
    tenthuonghieu VARCHAR(40)
);

CREATE TABLE sanpham 
(
    masanpham INT PRIMARY KEY AUTO_INCREMENT,
    tensanpham VARCHAR(255) NOT NULL,
    gia INT NOT NULL,
    mathuonghieu INT NOT NULL,
    mota VARCHAR(500) DEFAULT '',
    soluongtonkho INT,
    ngaytao DATETIME,
    chinhsua DATETIME,
    maloaisanpham INT,
    thumbnail VARCHAR(300) DEFAULT '',
    CONSTRAINT fk_loaisanpham FOREIGN KEY (maloaisanpham) REFERENCES loaisanpham(maloaisanpham),
    CONSTRAINT fk_mathuonghieu FOREIGN KEY (mathuonghieu) REFERENCES thuonghieu(mathuonghieu)
);

CREATE TABLE hinhanh
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    masanpham INT,
    maloaisanpham INT,
    tenhinhanh VARCHAR(300),
    CONSTRAINT fk_hinhanh_sanpham FOREIGN KEY(masanpham) REFERENCES sanpham(masanpham) ON DELETE CASCADE,
    CONSTRAINT fk_hinhanh_loaisanpham FOREIGN KEY(maloaisanpham) REFERENCES loaisanpham(maloaisanpham) ON DELETE CASCADE
);

CREATE TABLE accounts
(
    userid INT PRIMARY KEY AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    fullname VARCHAR(255) DEFAULT '',
    diachi VARCHAR(255) DEFAULT '',
    sodienthoai VARCHAR(15) NOT NULL,
    ngaytao DATETIME,
    chinhsua DATETIME,
    ngaysinh DATE,
    is_active BIT DEFAULT 1,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0,
    rolename BIT
);

CREATE TABLE tokens
(
    token_id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(500) UNIQUE NOT NULL,
    token_type VARCHAR(100) NOT NULL,
    expiration_date DATETIME,
    revoked BIT DEFAULT 1,
    expired BIT DEFAULT 1,
    userid INT,
    CONSTRAINT fk_tokens_accounts FOREIGN KEY (userid) REFERENCES accounts(userid)
);

CREATE TABLE social_accounts
(
    social_account_id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'tên nhà social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'email tài khoản',
    name VARCHAR(100) NOT NULL COMMENT 'tên người dùng',
    userid INT,
    CONSTRAINT fk_social_accounts_accounts FOREIGN KEY (userid) REFERENCES accounts(userid)
);

CREATE TABLE donhang (
    madonhang INT PRIMARY KEY AUTO_INCREMENT,
    userid INT,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) DEFAULT '',
    sodienthoai VARCHAR(15) NOT NULL,
    diachi VARCHAR(200) NOT NULL,
    ghichu VARCHAR(100) DEFAULT '',
    trangthai ENUM('chua xu ly', 'dang xu ly', 'dang van chuyen', 'giao hang thanh cong', 'da huy'),
    ngaydathang DATE,
    tongtien INT,
    phuongthucthanhtoan VARCHAR(100),
    is_active BIT DEFAULT 1,
    CONSTRAINT fk_donhang_accounts FOREIGN KEY (userid) REFERENCES accounts(userid)
);

CREATE TABLE chitietdonhang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    madonhang INT NOT NULL,
    masanpham INT NOT NULL,
    soluong INT NOT NULL,
    giaban INT NOT NULL,
    tongtien INT NOT NULL,
    CONSTRAINT fk_donhang FOREIGN KEY (madonhang) REFERENCES donhang(madonhang),
    CONSTRAINT fk_sanpham FOREIGN KEY (masanpham) REFERENCES sanpham(masanpham)
);

CREATE TABLE feedback (
    feedbackid INT PRIMARY KEY AUTO_INCREMENT,
    userid INT,
    noidung VARCHAR(255) NOT NULL,
    sosao INT NOT NULL,
    masanpham INT,
    CONSTRAINT fk_spfb FOREIGN KEY (masanpham) REFERENCES sanpham(masanpham),
    CONSTRAINT fk_feedback_accounts FOREIGN KEY (userid) REFERENCES accounts(userid)
);

-- Example data

INSERT INTO loaisanpham (tenloaisanpham) VALUES
('Điện thoại'),
('Máy tính xách tay'),
('Tai nghe'),
('Đồng hồ thông minh'),
('Phụ kiện'),
('Máy ảnh'),
('Loa Bluetooth');

INSERT INTO thuonghieu (tenthuonghieu) VALUES
('Apple'),
('Samsung'),
('Xiaomi'),
('Sony'),
('Huawei'),
('Dell'),
('JBL');