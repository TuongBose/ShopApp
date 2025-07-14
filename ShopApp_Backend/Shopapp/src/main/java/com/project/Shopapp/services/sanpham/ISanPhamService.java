package com.project.Shopapp.services.sanpham;

import com.project.Shopapp.dtos.HinhAnhDTO;
import com.project.Shopapp.dtos.SanPhamDTO;
import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.SanPhamResponse;
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
