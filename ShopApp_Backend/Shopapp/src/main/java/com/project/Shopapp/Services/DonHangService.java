package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.CartItemDTO;
import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Models.*;
import com.project.Shopapp.Repositories.AccountRepository;
import com.project.Shopapp.Repositories.CTDHRepository;
import com.project.Shopapp.Repositories.DonHangRepository;
import com.project.Shopapp.Repositories.SanPhamRepository;
import com.project.Shopapp.Responses.CTDHResponse;
import com.project.Shopapp.Responses.DonHangResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonHangService implements IDonHangService {
    private final DonHangRepository donHangRepository;
    private final AccountRepository accountRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CTDHRepository ctdhRepository;
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
        donHang.setIS_ACTIVE(true); // Đoạn này nên set sẵn trong SQL
        donHang.setTONGTIEN(donHangDTO.getTONGTIEN());
        donHangRepository.save(donHang);

        List<CTDH> ctdhList = new ArrayList<>();
        for(CartItemDTO cartItemDTO : donHangDTO.getCartitems())
        {
            CTDH ctdh =  new CTDH();
            ctdh.setMADONHANG(donHang);
            int maSanPham = cartItemDTO.getMasanpham();
            int quantity =  cartItemDTO.getQuantity();

            SanPham sanPham = sanPhamRepository.findById(maSanPham)
                    .orElseThrow(()-> new RuntimeException("Product ID does not exists"));

            ctdh.setMASANPHAM(sanPham);
            ctdh.setSOLUONG(quantity);
            ctdh.setGIABAN(sanPham.getGIA());
            ctdh.setTONGTIEN(sanPham.getGIA()*quantity);

            ctdhList.add(ctdh);
        }
        ctdhRepository.saveAll(ctdhList);
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
        DonHangResponse donHangResponse = modelMapper.map(existingDonHang, DonHangResponse.class);

        List<CTDH> ctdhList = ctdhRepository.findByMADONHANG(existingDonHang);
        List<CTDHResponse> ctdhResponseList = new ArrayList<>();

        if(ctdhList.isEmpty())
            donHangResponse.setCtdhList(null);
        else{
            for(CTDH ctdh : ctdhList)
            {
                ctdhResponseList.add(CTDHResponse.fromCTDH(ctdh));
            }
            donHangResponse.setCtdhList(ctdhResponseList);
        }

        return donHangResponse;
    }

    @Override
    public List<DonHang> getDonHangByUSERID(int id) throws Exception {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay USERID"));
        return donHangRepository.findByUSERID(existingAccount);
    }

    @Override
    @Transactional
    public DonHang updateDonHang(int id, DonHangDTO donHangDTO) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MADONHANG"));
        Account existingAccount = accountRepository.findById(donHangDTO.getUSERID())
                .orElseThrow(() -> new RuntimeException("Khong tim thay USERID"));

        existingDonHang.setUSERID(existingAccount);
        existingDonHang.setFULLNAME(donHangDTO.getFULLNAME());
        existingDonHang.setEMAIL(donHangDTO.getEMAIL());
        existingDonHang.setSODIENTHOAI(donHangDTO.getSODIENTHOAI());
        existingDonHang.setDIACHI(donHangDTO.getDIACHI());
        existingDonHang.setGHICHU(donHangDTO.getGHICHU());
        existingDonHang.setTONGTIEN(donHangDTO.getTONGTIEN());
        existingDonHang.setPHUONGTHUCTHANHTOAN(donHangDTO.getPHUONGTHUCTHANHTOAN());

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
