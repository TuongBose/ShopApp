package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "donhang")
@Builder
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MADONHANG;

    @ManyToOne
    @JoinColumn(name = "USERID")
    private Account USERID;

    @Column(name = "FULLNAME", nullable = false)
    private String FULLNAME;

    private String EMAIL;

    @Column(name = "SODIENTHOAI", nullable = false)
    private String SODIENTHOAI;

    @Column(name = "DIACHI", nullable = false)
    private String DIACHI;

    private String GHICHU;
    private String TRANGTHAI;
    private LocalDate NGAYDATHANG;
    private int TONGTIEN;
    private String PHUONGTHUCTHANHTOAN;
    private boolean IS_ACTIVE;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
