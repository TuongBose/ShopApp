package com.project.Shopapp.services.loaisanpham;

import com.project.Shopapp.dtos.LoaiSanPhamDTO;
import com.project.Shopapp.models.LoaiSanPham;

import java.util.List;

public interface ILoaiSanPhamService {
    LoaiSanPham createLoaiSanPham(LoaiSanPhamDTO loaiSanPhamDTO) throws Exception;
    LoaiSanPham getLoaiSanPhamByMALOAISANPHAM(int id) throws Exception;
    List<LoaiSanPham> getAllLoaiSanPham();
    LoaiSanPham updateLoaiSanPham(int id, LoaiSanPhamDTO loaiSanPhamDTO) throws Exception;
    LoaiSanPham deleteLoaiSanPham(int id) throws Exception;
}
