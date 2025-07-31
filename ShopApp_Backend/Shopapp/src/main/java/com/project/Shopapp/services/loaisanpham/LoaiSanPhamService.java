package com.project.Shopapp.services.loaisanpham;

import com.project.Shopapp.dtos.LoaiSanPhamDTO;
import com.project.Shopapp.models.LoaiSanPham;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.repositories.LoaiSanPhamRepository;
import com.project.Shopapp.repositories.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiSanPhamService implements ILoaiSanPhamService {
    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;

    @Override
    @Transactional
    public LoaiSanPham createLoaiSanPham(LoaiSanPhamDTO loaiSanPhamDTO) {
        LoaiSanPham newLoaiSanPham = LoaiSanPham
                .builder()
                .TENLOAISANPHAM(loaiSanPhamDTO.getTENLOAISANPHAM())
                .build();
        return loaiSanPhamRepository.save(newLoaiSanPham);
    }

    @Override
    public LoaiSanPham getLoaiSanPhamByMASANPHAM(int id) {
        return loaiSanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<LoaiSanPham> getAllLoaiSanPham() {
        return loaiSanPhamRepository.findAll();
    }

    @Override
    @Transactional
    public LoaiSanPham updateLoaiSanPham(int id, LoaiSanPhamDTO loaiSanPhamDTO) {
        LoaiSanPham existingLoaiSanPham = getLoaiSanPhamByMASANPHAM(id);
        existingLoaiSanPham.setTENLOAISANPHAM(loaiSanPhamDTO.getTENLOAISANPHAM());
        loaiSanPhamRepository.save(existingLoaiSanPham);
        return existingLoaiSanPham;
    }

    @Override
    @Transactional
    public LoaiSanPham deleteLoaiSanPham(int id) throws Exception {
        LoaiSanPham loaiSanPham = loaiSanPhamRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        List<SanPham> sanPhams = sanPhamRepository.findByMALOAISANPHAM(loaiSanPham);
        if (!sanPhams.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated products");
        } else {
            loaiSanPhamRepository.deleteById(id);
            return loaiSanPham;
        }
    }
}
