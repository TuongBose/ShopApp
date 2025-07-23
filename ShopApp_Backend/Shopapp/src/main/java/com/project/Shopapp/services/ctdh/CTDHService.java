package com.project.Shopapp.services.ctdh;

import com.project.Shopapp.dtos.CTDH_DTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.models.DonHang;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.repositories.CTDHRepository;
import com.project.Shopapp.repositories.DonHangRepository;
import com.project.Shopapp.repositories.SanPhamRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CTDHService implements ICTDHService {
    private final CTDHRepository ctdhRepository;
    private final SanPhamRepository sanPhamRepository;
    private final DonHangRepository donHangRepository;

    @Override
    public CTDH createCTDH(CTDH_DTO ctdh_dto) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(ctdh_dto.getMADONHANG())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));

        SanPham existingSanPham = sanPhamRepository.findById(ctdh_dto.getMASANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MASANPHAM"));

        CTDH newCTDH = CTDH
                .builder()
                .MADONHANG(existingDonHang)
                .MASANPHAM(existingSanPham)
                .SOLUONG(ctdh_dto.getSOLUONG())
                .GIABAN(ctdh_dto.getGIABAN())
                .TONGTIEN(ctdh_dto.getTONGTIEN())
                .build();


        return ctdhRepository.save(newCTDH);
    }

    @Override
    public CTDH getCTDHByID(int id) throws Exception {
        return ctdhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay id = " + id));
    }

    @Override
    public CTDH updateCTDH(int id, CTDH_DTO ctdh_dto) throws DataNotFoundException{
        CTDH existingCTDH = ctdhRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Khong tim thay ID"));

        DonHang existingDonHang = donHangRepository.findById(ctdh_dto.getMADONHANG())
                .orElseThrow(() -> new DataNotFoundException("Khong tim thay MADONHANG"));

        SanPham existingSanPham = sanPhamRepository.findById(ctdh_dto.getMASANPHAM())
                .orElseThrow(() -> new DataNotFoundException("Khong tim thay MASANPHAM"));

        existingCTDH.setMADONHANG(existingDonHang);
        existingCTDH.setMASANPHAM(existingSanPham);
        existingCTDH.setSOLUONG(ctdh_dto.getSOLUONG());
        existingCTDH.setGIABAN(ctdh_dto.getGIABAN());
        existingCTDH.setTONGTIEN(ctdh_dto.getTONGTIEN());

        ctdhRepository.save(existingCTDH);
        return existingCTDH;
    }

    @Override
    @Transactional
    public void deleteCTDH(int id) {
        ctdhRepository.deleteById(id);
    }

    @Override
    public List<CTDH> getCTDHByMADONHANG(int MADONHANG) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(MADONHANG)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));
        return ctdhRepository.findByMADONHANG(existingDonHang);
    }
}
