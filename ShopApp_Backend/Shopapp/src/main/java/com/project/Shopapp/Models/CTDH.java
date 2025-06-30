package com.project.Shopapp.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHITIETDONHANG")
@Builder
public class CTDH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "MADONHANG")
    @JsonBackReference
    private DonHang MADONHANG;

    @ManyToOne
    @JoinColumn(name = "MASANPHAM")
    private SanPham MASANPHAM;

    @Column(name = "SOLUONG", nullable = false)
    private int SOLUONG;

    @Column(name = "GIABAN", nullable = false)
    private int GIABAN;

    @Column(name = "TONGTIEN", nullable = false)
    private int TONGTIEN;
}
