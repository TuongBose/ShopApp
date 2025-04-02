package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.LoaiSanPhamDTO;
import com.project.Shopapp.Models.LoaiSanPham;

import java.util.List;

public interface ILoaiSanPhamService {
    LoaiSanPham createLoaiSanPham(LoaiSanPhamDTO loaiSanPhamDTO);
    LoaiSanPham getLoaiSanPhamByMASANPHAM(int id);
    List<LoaiSanPham> getAllLoaiSanPham();
    LoaiSanPham updateLoaiSanPham(int id, LoaiSanPhamDTO loaiSanPhamDTO);
    void deleteLoaiSanPham(int id);
}
