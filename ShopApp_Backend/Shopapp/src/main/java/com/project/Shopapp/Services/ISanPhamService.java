package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.HinhAnhDTO;
import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Models.HinhAnh;
import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Responses.SanPhamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ISanPhamService {
    public SanPham createSanPham(SanPhamDTO sanPhamDTO);
    SanPham getSanPhamByMASANPHAM(int id);
    Page<SanPhamResponse> getAllSanPham(String keyword,
                                        int MALOAISANPHAM,
                                        PageRequest pageRequest);
    SanPham updateSanPham(int id, SanPhamDTO sanPhamDTO);
    void deleteSanPham(int id);
    boolean existsByTENSANPHAM(String TENSANPHAM);
    HinhAnh createHinhAnh(HinhAnhDTO hinhAnhDTO);
    List<SanPham> findSanPhamByMASANPHAMList(List<Integer> MASANPHAM);
}
