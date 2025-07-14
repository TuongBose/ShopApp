package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sanpham")
@Builder
// Event-driven approach with Spring Data JPA
@EntityListeners(SanPhamListener.class)
public class SanPham extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MASANPHAM;

    @Column(name = "TENSANPHAM", nullable = false)
    private String TENSANPHAM;

    @Column(name = "GIA", nullable = false)
    private int GIA;

    @ManyToOne
    @JoinColumn(name = "MATHUONGHIEU")
    private ThuongHieu MATHUONGHIEU;

    private String MOTA;
    private int SOLUONGTONKHO;

    @ManyToOne
    @JoinColumn(name = "MALOAISANPHAM")
    private LoaiSanPham MALOAISANPHAM;

    private String THUMBNAIL;
}
