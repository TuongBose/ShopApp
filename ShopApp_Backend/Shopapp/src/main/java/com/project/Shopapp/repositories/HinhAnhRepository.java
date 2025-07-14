package com.project.Shopapp.repositories;

import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.models.LoaiSanPham;
import com.project.Shopapp.models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByMASANPHAMAndMALOAISANPHAM(SanPham sanPham, LoaiSanPham loaiSanPham);
    List<HinhAnh> findByMASANPHAM(SanPham sanPham);
}
