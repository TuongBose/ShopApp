package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Models.DonHang;
import com.project.Shopapp.Models.TrangThaiDonHang;
import com.project.Shopapp.Repositories.AccountRepository;
import com.project.Shopapp.Repositories.DonHangRepository;
import com.project.Shopapp.Responses.DonHangResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Account existingAccount = accountRepository.findById(donHangDTO.getUSERID()).orElseThrow(() -> new RuntimeException("Khong tim thay USERID nay!!!"));

        // Convert DonHangDTO => DonHang, dùng thư viện Model Mapper
        // Tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(DonHangDTO.class, DonHang.class).addMappings(mapper -> mapper.skip(DonHang::setMADONHANG));

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
    public List<DonHang> getDonHangByMASANPHAM(int id) {
        return null;
    }

    @Override
    public DonHangResponse getDonHangByMADONHANG(int id) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));

        modelMapper.typeMap(DonHang.class, DonHangResponse.class);
        return modelMapper.map(existingDonHang, DonHangResponse.class);
    }

    @Override
    public List<DonHang> getDonHangByUSERID(int id) throws Exception {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay USERID"));
        return donHangRepository.findByUSERID(existingAccount);
    }

    @Override
    public DonHang updateDonHang(int id, DonHangDTO donHangDTO) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));
        Account existingAccount = accountRepository.findById(donHangDTO.getUSERID())
                .orElseThrow(() -> new RuntimeException("Khong tim thay USERID"));
        // Ngăn chặn không cho thay đổi USERID
        if(existingDonHang.getUSERID().getUSERID() != donHangDTO.getUSERID())
            throw new RuntimeException("Khong duoc thay doi USERID");

        modelMapper.typeMap(DonHangDTO.class, DonHang.class)
                .addMappings(mapper -> mapper.skip(DonHang::setMADONHANG));
        modelMapper.map(donHangDTO,existingDonHang);
        donHangRepository.save(existingDonHang);
        return existingDonHang;
    }

    @Override
    public void deleteDonHang(int id) throws Exception{
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));
        if(existingDonHang!=null)
        {
            existingDonHang.setIS_ACTIVE(false);
            donHangRepository.save(existingDonHang);
        }
    }
}
