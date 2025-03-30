package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LOAISANPHAM")
@Builder
public class LoaiSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "MALOAISANPHAM") // Khong can thiet vi MALOAISANPHAM trung voi ten trong database
    private int MALOAISANPHAM;

    @Column(name = "TENLOAISANPHAM", nullable = false) // Can thiet vi TENLOAISANPHAM trong database not null
    private String TENLOAISANPHAM;
}
