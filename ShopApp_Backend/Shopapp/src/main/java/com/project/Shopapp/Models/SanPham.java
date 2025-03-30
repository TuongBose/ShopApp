package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SANPHAM")
@Builder
public class SanPham extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MASANPHAM;

    @Column(name = "TENSANPHAM", nullable = false)
    private String TENSANPHAM;

    @Column(name = "GIA", nullable = false)
    private int GIA;

    @Column(name = "MATHUONGHIEU", nullable = false)
    private int MATHUONGHIEU;

    private String MOTA;
    private int SOLUONGTONKHO;

    @ManyToOne
    private LoaiSanPham MALOAISANPHAM;
}
