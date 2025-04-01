package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HINHANH")
@Builder
public class HinhAnh {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "MASANPHAM")
    private SanPham MASANPHAM;

    @ManyToOne
    @JoinColumn(name = "MALOAISANPHAM")
    private LoaiSanPham MALOAISANPHAM;

    private String TENHINHANH;
}
