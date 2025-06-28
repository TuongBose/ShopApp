package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.HinhAnh;
import com.project.Shopapp.Models.LoaiSanPham;
import com.project.Shopapp.Models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByMASANPHAMAndMALOAISANPHAM(SanPham sanPham, LoaiSanPham loaiSanPham);
    List<HinhAnh> findByMASANPHAM(SanPham sanPham);
}
