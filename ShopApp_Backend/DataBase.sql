CREATE DATABASE SHOPAPP;
USE SHOPAPP;

-- TẠO BẢNG
CREATE TABLE LOAISANPHAM 
(
    MALOAISANPHAM INT PRIMARY KEY AUTO_INCREMENT,
    TENLOAISANPHAM VARCHAR(255) NOT NULL
);

CREATE TABLE THUONGHIEU
(
	MATHUONGHIEU INT PRIMARY KEY AUTO_INCREMENT,
	TENTHUONGHIEU VARCHAR(40)
);

CREATE TABLE SANPHAM 
(
    MASANPHAM INT PRIMARY KEY AUTO_INCREMENT,
    TENSANPHAM VARCHAR(255) NOT NULL,
    GIA INT NOT NULL,
	MATHUONGHIEU INT NOT NULL,
    MOTA VARCHAR(500) DEFAULT '',
    SOLUONGTONKHO INT,
    NGAYTAO DATETIME,
    CHINHSUA DATETIME,
	MALOAISANPHAM INT,
	CONSTRAINT FK_LOAISANPHAM FOREIGN KEY (MALOAISANPHAM) REFERENCES LOAISANPHAM(MALOAISANPHAM),
	CONSTRAINT FK_MATHUONGHIEU FOREIGN KEY (MATHUONGHIEU) REFERENCES THUONGHIEU(MATHUONGHIEU)
);

CREATE TABLE HINHANH
(
	ID INT PRIMARY KEY AUTO_INCREMENT,
    MASANPHAM INT,
    MALOAISANPHAM INT,
    TENHINHANH VARCHAR(300),
    CONSTRAINT FK_HINHANH_SANPHAM FOREIGN KEY(MASANPHAM) REFERENCES SANPHAM(MASANPHAM) ON DELETE CASCADE,
	CONSTRAINT FK_HINHANH_LOAISANPHAM FOREIGN KEY(MALOAISANPHAM) REFERENCES LOAISANPHAM(MALOAISANPHAM) ON DELETE CASCADE
);

CREATE TABLE ACCOUNTS
(
	USERID INT PRIMARY KEY AUTO_INCREMENT,
	PASSWORD VARCHAR(255) NOT NULL,
	EMAIL VARCHAR(100),
	FULLNAME VARCHAR(255) DEFAULT '',
	DIACHI VARCHAR(255) DEFAULT '',
	SODIENTHOAI VARCHAR(15) NOT NULL,
    NGAYTAO DATETIME,
    CHINHSUA DATETIME,
    NGAYSINH DATE,
    IS_ACTIVE BIT DEFAULT 1,
    FACEBOOK_ACCOUNT_ID INT DEFAULT 0,
    GOOGLE_ACCOUNT_ID INT DEFAULT 0,
	ROLENAME BIT
);

CREATE TABLE TOKENS
(
	TOKEN_ID INT PRIMARY KEY AUTO_INCREMENT,
    TOKEN VARCHAR(500) UNIQUE NOT NULL,
    TOKEN_TYPE VARCHAR(100) NOT NULL,
    EXPIRATION_DATE DATETIME,
    REVOKED BIT DEFAULT 1,
    EXPIRED BIT DEFAULT 1,
    USERID INT,
    CONSTRAINT FK_TOKENS_ACCOUNTS FOREIGN KEY (USERID) REFERENCES ACCOUNTS(USERID)
);

CREATE TABLE SOCIAL_ACCOUNTS
(
	SOCIAL_ACCOUNT_ID INT PRIMARY KEY AUTO_INCREMENT,
    PROVIDER VARCHAR(20) NOT NULL COMMENT 'TÊN NHÀ SOCIAL NETWORK',
    PROVIDER_ID VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(150) NOT NULL COMMENT 'EMAIL TÀI KHOẢN',
    NAME VARCHAR(100) NOT NULL COMMENT 'TÊN NGƯỜI DÙNG',
    USERID INT,
	CONSTRAINT FK_SOCIAL_ACCOUNTS_ACCOUNTS FOREIGN KEY (USERID) REFERENCES ACCOUNTS(USERID)
);

CREATE TABLE DONHANG (
    MADONHANG INT PRIMARY KEY AUTO_INCREMENT,
    USERID INT,
    FULLNAME VARCHAR(100) NOT NULL,
    EMAIL VARCHAR(100) DEFAULT '',
    SODIENTHOAI VARCHAR(15) NOT NULL,
    DIACHI VARCHAR(200) NOT NULL,
    GHICHU VARCHAR(100) DEFAULT '',
    TRANGTHAI ENUM('Chua xu ly', 'Dang xu ly', 'Dang van chuyen', 'Giao hang thanh cong', 'Da huy'),
    NGAYDATHANG DATE,
    TONGTIEN INT,
    PHUONGTHUCTHANHTOAN VARCHAR(100),
    IS_ACTIVE BIT DEFAULT 1,
    CONSTRAINT FK_DONHANG_ACCOUNTS FOREIGN KEY (USERID) REFERENCES ACCOUNTS(USERID)
);

CREATE TABLE CHITIETDONHANG (
	ID INT PRIMARY KEY AUTO_INCREMENT,
    MADONHANG INT NOT NULL,
    MASANPHAM INT NOT NULL,
    SOLUONG INT NOT NULL,
    GIABAN INT NOT NULL,
    TONGTIEN INT NOT NULL,
    CONSTRAINT FK_DONHANG FOREIGN KEY (MADONHANG) REFERENCES DONHANG(MADONHANG),
    CONSTRAINT FK_SANPHAM FOREIGN KEY (MASANPHAM) REFERENCES SANPHAM(MASANPHAM)
);

CREATE TABLE FEEDBACK (
    FEEDBACKID INT PRIMARY KEY AUTO_INCREMENT,
	USERID INT,
    NOIDUNG VARCHAR(255) NOT NULL,
    SOSAO INT NOT NULL,
    MASANPHAM INT,
    CONSTRAINT FK_SPFB FOREIGN KEY (MASANPHAM) REFERENCES SANPHAM(MASANPHAM),
    CONSTRAINT FK_FEEDBACK_ACCOUNTS FOREIGN KEY (USERID) REFERENCES ACCOUNTS(USERID)
);

-- NHẬP DỮ LIỆU

-- Nhập dữ liệu vào bảng LOAISANPHAM
INSERT INTO LOAISANPHAM (TENLOAISANPHAM, HINHANH, TenController) 
VALUES 
(N'Điện Thoại', N'Phone.png', N'DienThoai'),
(N'Máy Tính Bảng', N'Tablet.png', N'MayTinhBang'),
(N'Ipad', N'ipad.png',  N'Ipad'),
(N'Phụ Kiện', N'Wire.png',  N'PhuKien'),
(N'Sim, Thẻ Cào', N'sim.png',  N'Sim'),
(N'Laptop', N'Laptop.png',  N'Laptop');

-- Nhập dữ liệu vào bảng THUONGHIEU
INSERT INTO THUONGHIEU (TenTHUONGHIEU)
VALUES
('Apple'),
('Samsung'),
('Xiaomi'),
('Oppo'),
('Vivo'),
('Lenovo'),
('Huawei'),
('Microsoft'),
('Viettel'),
('Mobifone'),
('Vinaphone'),
('Asus'),
('Dell'),
('Acer');

-- Nhập dữ liệu vào bảng SanPham 
INSERT INTO SanPham (TenSanPham, Gia, MaTHUONGHIEU, MoTa, HINHANH, SoLuongTonKho,MALOAISANPHAM) 
VALUES 
-- Dien Thoai
('iPhone 14', 25000000, 1, N'Điện thoại iPhone 14 mới nhất', 'iphone14.jpg', 50,1),
('Samsung Galaxy S23', 20000000, 2, N'Điện thoại Samsung Galaxy S23', 'samsung_s23.jpg', 40,1),
('Xiaomi Mi 12', 15000000, 3, N'Điện thoại Xiaomi Mi 12', 'xiaomi_mi12.jpg', 30,1),
('Oppo Reno6', 12000000, 4, N'Điện thoại Oppo Reno6', 'oppo_reno6.jpg', 20,1),
('Vivo V21', 10000000, 5, N'Điện thoại Vivo V21', 'vivo_v21.jpg', 25,1),

-- May Tinh Bang
('Samsung Galaxy Tab S7', 18000000, 2, N'Máy tính bảng Samsung Galaxy Tab S7', 'tab_s7.jpg', 15,2),
('iPad Pro 12.9', 30000000, 1, N'iPad Pro 12.9 inch mới nhất', 'ipad_pro_129.jpg', 10,2),
('Lenovo Tab P11', 12000000, 6, N'Máy tính bảng Lenovo Tab P11', 'lenovo_tab_p11.jpg', 12,2),
('Huawei MatePad 11', 15000000, 7, N'Máy tính bảng Huawei MatePad 11', 'matepad_11.jpg', 8,2),
('Microsoft Surface Go 3', 20000000, 8, N'Máy tính bảng Microsoft Surface Go 3', 'surface_go_3.jpg', 5,2),

-- Ipad
('iPad Mini 6', 15000000, 1, N'iPad Mini 6', 'ipad_mini_6.jpg', 18,3),
('iPad Air 5', 18000000, 1, N'iPad Air 5', 'ipad_air_5.jpg', 12,3),
('iPad Gen 9', 10000000, 1, N'iPad thế hệ 9', 'ipad_gen_9.jpg', 25,3),
('iPad Pro 11', 25000000, 1, N'iPad Pro 11 inch', 'ipad_pro_11.jpg', 10,3),
('iPad Pro 12.9', 30000000, 1, N'iPad Pro 12.9 inch', 'ipad_pro_129.jpg', 5,3),

-- Phu Kien
(N'Tai nghe AirPods Pro', 5000000, 1, N'Tai nghe không dây AirPods Pro', 'airpods_pro.jpg', 100,4),
(N'Tai nghe Galaxy Buds Pro', 3000000, 2, N'Tai nghe không dây Galaxy Buds Pro', 'galaxy_buds_pro.jpg', 80,4),
(N'Ốp lưng iPhone 14', 500000, 1, N'Ốp lưng cho iPhone 14', 'oplung_iphone14.jpg', 200,4),
(N'Sạc nhanh 20W', 400000, 1, N'Củ sạc nhanh 20W', 'cu_sac_nhanh_20w.jpg', 150,4),
(N'Bàn phím Magic Keyboard', 7000000, 1, N'Bàn phím Magic Keyboard cho iPad', 'magic_keyboard.jpg', 50,4),

-- Sim, The Cao
(N'Sim Viettel 4G', 200000, 9, N'Sim 4G Viettel 3 tháng', 'sim_viettel_4g.jpg', 300,5),
(N'Sim Vinaphone 4G', 180000, 11, N'Sim 4G Vinaphone 3 tháng', 'sim_vinaphone_4g.jpg', 250,5),
(N'Thẻ cào Viettel 100K', 100000, 9, N'Thẻ cào điện thoại 100K', 'thecao_viettel_100k.jpg', 500,5),
(N'Thẻ cào Mobifone 100K', 100000, 10, N'Thẻ cào điện thoại 100K', 'thecao_mobifone_100k.jpg', 400,5),
(N'Thẻ cào Vinaphone 100K', 100000, 11, N'Thẻ cào điện thoại 100K', 'thecao_vinaphone_100k.jpg', 450,5),

-- Laptop
(N'Asus Vivobook Go 15', 5000000, 12, N'Laptop văn phòng', 'Asus_Vivobook.jpg', 555,6),
(N'Dell Inspiron 15', 1900000, 13, N'Laptop doanh nhân', 'Dell_Inspiron.jpg', 100,6),
(N'Lenovo Ideapad Slim 3', 3000000, 6, N'Laptop đồ họa, lập trình', 'Lenovo_Ideapad_Slim_3.jpg', 123,6),
(N'Acer Aspire 3', 150000, 14, N'Laptop Gamming', 'AcerAspire3.jpg', 564,6),
(N'MacBook Air 13 inch M1', 1000000, 1, N'Laptop mỏng nhẹ, đồ họa, lập trình', 'MacBook_Air_13_inch_M1.jpg', 609,6);

-- Nhập dữ liệu bảng ACCOUNTSSSSSSS
INSERT INTO ACCOUNTS (USERNAME, PASS, EMAIL, FULLNAME, DIACHI, SODIENTHOAI, ROLENAME)
VALUES
('hongthanh', 'thanh123', 'hongthanh@gmail.com', N'Trần Hồng Thanh', N'123 Phú Thọ, Tân Phú, TPHCM', '012345678', 1),
('vananh', 'va123', 'vananh204@gmail.com', N'Trần Thị Vân Anh', N'555 Tây Thạnh, Tân Phú, TPHCM', '055566777', 0),
('phuonglinh', 'linhka333', 'phuonglinh.09204@gmail.com', N'Vũ Phương Linh', N'140 Lê Trọng Tấn, Tân Phú, TPHCM', '0980837584', 0),
('tuongvy123', 'dobiet', 'tuongvy2002@gmail.com', N'Nguyễn Tường Vy', N'1A QL1A, Hà Nam, Tây Bắc', '0908765111', 1),
('thanhnhan555', 'thanhnhanxinhdep', 'thanhnhanle8904@gmail.com', N'Thanh Nhàn', N'98 Hàm Nghi, Q1, TPHCM', '0989098989', 1);

-- Nhập dữ liệu vào bảng DonHang
INSERT INTO DonHang (USERID, NgayDatHang, TongTien) 
VALUES 
(1, '2024-10-01', 5000000),
(2, '2024-10-02', 10000000),
(3, '2024-10-03', 15000000),
(4, '2024-10-04', 20000000),
(5, '2024-10-05', 25000000);

-- Nhập dữ liệu vào bảng ChiTietDonHang
INSERT INTO ChiTietDonHang (MADONHANG, MASANPHAM, SoLuong, GiaBan) 
VALUES 
(1, 1, 1, 25000000),
(1, 16, 1, 5000000),
(2, 2, 1, 20000000),
(2, 17, 2, 3000000),
(3, 3, 1, 15000000),
(3, 18, 3, 500000),
(4, 4, 1, 12000000),
(4, 19, 4, 400000),
(5, 5, 1, 10000000),
(5, 20, 2, 7000000);

INSERT INTO feedback (USERID, NoiDung, SoSao, MASANPHAM)
VALUES 
(1, N'Sản phẩm rất tốt', 5, 1),
(2, N'Giá cả hợp lý, đáng mua', 4, 2),
(3, N'Chất lượng bình thường', 3, 3),
(4, N'Giao hàng nhanh, sản phẩm ổn', 4, 4),
(5, N'Thiết kế đẹp, tính năng ổn', 5, 5);

-- select các bảng
USE SHOPAPP;
SELECT * FROM ACCOUNTS;
SELECT * FROM CHITIETDONHANG;
SELECT * FROM DONHANG;
SELECT * FROM FEEDBACK;
SELECT * FROM HINHANH;
SELECT * FROM LOAISANPHAM;
SELECT * FROM SANPHAM;
SELECT * FROM SOCIAL_ACCOUNTS;
SELECT * FROM THUONGHIEU;
SELECT * FROM TOKENS;

drop database shopapp