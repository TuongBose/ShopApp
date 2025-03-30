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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @ManyToOne
    private SanPham MASANPHAM;

    @ManyToOne
    private LoaiSanPham MALOAISANPHAM;

    private String TENHINHANH;
}
