package com.project.Shopapp.repositories;

import com.project.Shopapp.models.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer> {
    boolean existsByTENLOAISANPHAM(String TENLOAISANPHAM);
}
