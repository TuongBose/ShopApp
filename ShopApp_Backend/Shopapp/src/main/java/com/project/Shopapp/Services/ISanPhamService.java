package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.HinhAnhDTO;
import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Models.HinhAnh;
import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Responses.SanPhamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ISanPhamService {
    public SanPham createSanPham(SanPhamDTO sanPhamDTO);
    SanPham getSanPhamByMASANPHAM(int id);
    Page<SanPhamResponse> getAllSanPham(PageRequest pageRequest);
    SanPham updateSanPham(int id, SanPhamDTO sanPhamDTO);
    void deleteSanPham(int id);
    boolean existsByTENSANPHAM(String TENSANPHAM);
    HinhAnh createHinhAnh(HinhAnhDTO hinhAnhDTO);
}
