package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Models.DonHang;
import com.project.Shopapp.Models.TrangThaiDonHang;
import com.project.Shopapp.Repositories.AccountRepository;
import com.project.Shopapp.Repositories.DonHangRepository;
import com.project.Shopapp.Responses.DonHangResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonHangService implements IDonHangService {
    private final DonHangRepository donHangRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public DonHangResponse createDonHang(DonHangDTO donHangDTO) throws Exception {
        // Tìm xem Account có tồn tại không
        Account existingAccount = accountRepository
                .findById(donHangDTO.getUSERID())
                .orElseThrow(() -> new RuntimeException("Khong tim thay USERID nay!!!"));

        // Convert DonHangDTO => DonHang, dùng thư viện Model Mapper
        // Tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(DonHangDTO.class, DonHang.class)
                .addMappings(mapper -> mapper.skip(DonHang::setMADONHANG));

        // Cập nhật các trường của DonHang từ DonHangDTO
        DonHang donHang = new DonHang();
        modelMapper.map(donHangDTO, donHang);
        donHang.setUSERID(existingAccount);
        donHang.setNGAYDATHANG(LocalDate.now());
        donHang.setTRANGTHAI(TrangThaiDonHang.CHUAXULY);
        donHang.setIS_ACTIVE(true);

        donHangRepository.save(donHang);
        return modelMapper.map(donHang, DonHangResponse.class);
    }

    @Override
    public DonHang getDonHangByMASANPHAM(int id) {
        return null;
    }

    @Override
    public List<DonHang> getAllDonHang() {
        return List.of();
    }

    @Override
    public DonHang updateDonHang(int id, DonHangDTO donHangDTO) {
        return null;
    }

    @Override
    public void deleteDonHang(int id) {

    }
}
