package com.project.Shopapp.services.loaisanpham;

import com.project.Shopapp.dtos.LoaiSanPhamDTO;
import com.project.Shopapp.models.LoaiSanPham;

import java.util.List;

public interface ILoaiSanPhamService {
    LoaiSanPham createLoaiSanPham(LoaiSanPhamDTO loaiSanPhamDTO);
    LoaiSanPham getLoaiSanPhamByMASANPHAM(int id);
    List<LoaiSanPham> getAllLoaiSanPham();
    LoaiSanPham updateLoaiSanPham(int id, LoaiSanPhamDTO loaiSanPhamDTO);
    void deleteLoaiSanPham(int id);
}
