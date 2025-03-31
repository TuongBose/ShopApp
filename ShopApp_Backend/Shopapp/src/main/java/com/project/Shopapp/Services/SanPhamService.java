package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.HinhAnhDTO;
import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Exceptions.DataNotFoundException;
import com.project.Shopapp.Models.HinhAnh;
import com.project.Shopapp.Models.LoaiSanPham;
import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Models.ThuongHieu;
import com.project.Shopapp.Repositories.HinhAnhRepository;
import com.project.Shopapp.Repositories.LoaiSanPhamRepository;
import com.project.Shopapp.Repositories.SanPhamRepository;
import com.project.Shopapp.Repositories.ThuongHieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SanPhamService implements ISanPhamService {
    private final SanPhamRepository sanPhamRepository;
    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final ThuongHieuRepository thuongHieuRepository;

    @Override
    public SanPham createSanPham(SanPhamDTO sanPhamDTO) {
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM"));

        ThuongHieu existingThuongHieu = thuongHieuRepository
                .findById(sanPhamDTO.getMATHUONGHIEU())
                .orElseThrow(()-> new RuntimeException("Khong tim thay MATHUONGHIEU"));

        SanPham newSanPham = SanPham.builder()
                .TENSANPHAM(sanPhamDTO.getTENSANPHAM())
                .GIA(sanPhamDTO.getGIA())
                .MALOAISANPHAM(existingLoaiSanPham)
                .MATHUONGHIEU(existingThuongHieu)
                .build();
        return sanPhamRepository.save(newSanPham);
    }

    @Override
    public SanPham getSanPhamByMASANPHAM(int id) {
        return sanPhamRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay MASANPHAM nay"));
    }

    @Override
    public Page<SanPham> getAllSanPham(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        return sanPhamRepository.findAll(pageRequest);
    }

    @Override
    public SanPham updateSanPham(int id, SanPhamDTO sanPhamDTO) {
        SanPham existingSanPham = getSanPhamByMASANPHAM(id);
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM nay!!!"));

        existingSanPham.setTENSANPHAM(sanPhamDTO.getTENSANPHAM());
        existingSanPham.setMALOAISANPHAM(existingLoaiSanPham);
        existingSanPham.setGIA(sanPhamDTO.getGIA());
        existingSanPham.setMOTA(sanPhamDTO.getMOTA());

        return sanPhamRepository.save(existingSanPham);
    }

    @Override
    public void deleteSanPham(int id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public boolean existsByTENSANPHAM(String TENSANPHAM) {
        return sanPhamRepository.existsByTENSANPHAM(TENSANPHAM);
    }

    @Override
    public HinhAnh createHinhAnh(HinhAnhDTO hinhAnhDTO) {
        SanPham existingSanPham = getSanPhamByMASANPHAM(hinhAnhDTO.getMASANPHAM());
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(hinhAnhDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM nay!!!"));

        HinhAnh newHinhAnh = HinhAnh.builder()
                .TENHINHANH(hinhAnhDTO.getTENHINHANH())
                .MASANPHAM(existingSanPham)
                .MALOAISANPHAM(existingLoaiSanPham)
                .build();

        // Không cho thêm quá 5 ảnh cho 1 sản phẩm
        int size = hinhAnhRepository.findByMASANPHAMAndMALOAISANPHAM(existingSanPham, existingLoaiSanPham).size();
        if (size >= 5) throw new RuntimeException("So luong hinh anh cua san pham <= 5");

        return hinhAnhRepository.save(newHinhAnh);
    }
}
