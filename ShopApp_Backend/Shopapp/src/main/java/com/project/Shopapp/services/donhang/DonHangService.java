package com.project.Shopapp.services.donhang;

import com.project.Shopapp.dtos.CartItemDTO;
import com.project.Shopapp.dtos.DonHangDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.*;
import com.project.Shopapp.repositories.*;
import com.project.Shopapp.responses.ctdh.CTDHResponse;
import com.project.Shopapp.responses.donhang.DonHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonHangService implements IDonHangService {
    private final DonHangRepository donHangRepository;
    private final AccountRepository accountRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CTDHRepository ctdhRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
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
        donHang.setTRANGTHAI(OrderStatus.PENDING);
        donHang.setIS_ACTIVE(true); // Đoạn này nên set sẵn trong SQL
        donHang.setTONGTIEN(donHangDTO.getTONGTIEN());

        // Xu ly coupon
        String couponCode =donHangDTO.getCouponCode();
        if(couponCode!=null){
            Coupon coupon = couponRepository.findByCode(couponCode)
                    .orElseThrow(()-> new IllegalArgumentException("Coupon not found"));

            if (!coupon.isActive()) {
                throw new IllegalArgumentException("Coupon is not active");
            }

            donHang.setCoupon(coupon);
        }else {
            donHang.setCoupon(null);
        }

        donHangRepository.save(donHang);

        List<CTDH> ctdhList = new ArrayList<>();
        for (CartItemDTO cartItemDTO : donHangDTO.getCartitems()) {
            CTDH ctdh = new CTDH();
            ctdh.setMADONHANG(donHang);
            int maSanPham = cartItemDTO.getMasanpham();
            int quantity = cartItemDTO.getQuantity();

            SanPham sanPham = sanPhamRepository.findById(maSanPham)
                    .orElseThrow(() -> new RuntimeException("Product ID does not exists"));

            ctdh.setMASANPHAM(sanPham);
            ctdh.setSOLUONG(quantity);
            ctdh.setGIABAN(sanPham.getGIA());
            ctdh.setTONGTIEN(sanPham.getGIA().multiply(BigDecimal.valueOf(quantity)));
            ctdh.setCoupon(donHang.getCoupon());

            ctdhList.add(ctdh);
        }

        ctdhRepository.saveAll(ctdhList);

        List<CTDHResponse> ctdhResponseList = ctdhList.stream().map(CTDHResponse::fromCTDH).toList();
        DonHangResponse donHangResponse = modelMapper.map(donHang, DonHangResponse.class);
        donHangResponse.setCtdhList(ctdhResponseList);

        return  donHangResponse;
    }

    @Override
    public DonHangResponse getDonHangByMADONHANG(int id) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find MADONHANG"));

        modelMapper.typeMap(DonHang.class, DonHangResponse.class);
        DonHangResponse donHangResponse = modelMapper.map(existingDonHang, DonHangResponse.class);

        List<CTDH> ctdhList = ctdhRepository.findByMADONHANG(existingDonHang);
        List<CTDHResponse> ctdhResponseList = new ArrayList<>();

        if (ctdhList.isEmpty())
            donHangResponse.setCtdhList(null);
        else {
            for (CTDH ctdh : ctdhList) {
                ctdhResponseList.add(CTDHResponse.fromCTDH(ctdh));
            }
            donHangResponse.setCtdhList(ctdhResponseList);
        }

        return donHangResponse;
    }

    @Override
    public List<DonHang> getDonHangByUSERID(int id) throws Exception {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find USERID"));
        return donHangRepository.findByUSERID(existingAccount);
    }

    @Override
    @Transactional
    public DonHang updateDonHang(int id, DonHangDTO donHangDTO) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find MADONHANG"));
        Account existingAccount = accountRepository.findById(donHangDTO.getUSERID())
                .orElseThrow(() -> new RuntimeException("Cannot find USERID"));

        existingDonHang.setUSERID(existingAccount);

        if(donHangDTO.getFULLNAME()!=null&&!donHangDTO.getFULLNAME().trim().isEmpty()) {
            existingDonHang.setFULLNAME(donHangDTO.getFULLNAME());
        }
        if(donHangDTO.getEMAIL()!=null&&!donHangDTO.getEMAIL().trim().isEmpty()) {
            existingDonHang.setEMAIL(donHangDTO.getEMAIL().trim());
        }
        if(donHangDTO.getSODIENTHOAI()!=null&&!donHangDTO.getSODIENTHOAI().trim().isEmpty()) {
            existingDonHang.setSODIENTHOAI(donHangDTO.getSODIENTHOAI().trim());
        }
        if(donHangDTO.getDIACHI()!=null&&!donHangDTO.getDIACHI().trim().isEmpty()) {
            existingDonHang.setDIACHI(donHangDTO.getDIACHI());
        }
        if(donHangDTO.getGHICHU()!=null&&!donHangDTO.getGHICHU().trim().isEmpty()) {
            existingDonHang.setGHICHU(donHangDTO.getGHICHU());
        }
        if(donHangDTO.getTONGTIEN()!=null) {
            existingDonHang.setTONGTIEN(donHangDTO.getTONGTIEN());
        }
        if(donHangDTO.getPHUONGTHUCTHANHTOAN()!=null&&!donHangDTO.getPHUONGTHUCTHANHTOAN().trim().isEmpty()) {
            existingDonHang.setPHUONGTHUCTHANHTOAN(donHangDTO.getPHUONGTHUCTHANHTOAN().trim());
        }
        if(donHangDTO.getStatus()!=null&&!donHangDTO.getStatus().trim().isEmpty()){
            existingDonHang.setTRANGTHAI(donHangDTO.getStatus().trim());
        }

        donHangRepository.save(existingDonHang);
        return existingDonHang;
    }

    @Override
    public void deleteDonHang(int id) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find MADONHANG"));

        if (existingDonHang != null) {
            existingDonHang.setIS_ACTIVE(false);
            donHangRepository.save(existingDonHang);
        }
    }

    @Override
    public Page<DonHangResponse> getAllDonHangByKeyword(String keyword, Pageable pageable) {
        Page<DonHang> donHangPage = donHangRepository.findByKeyword(keyword, pageable);
        return donHangPage.map(donHang -> {
            List<CTDHResponse> ctdhResponseList = ctdhRepository.findByMADONHANG(donHang)
                    .stream()
                    .map(CTDHResponse::fromCTDH)
                    .collect(Collectors.toList());
            return DonHangResponse.fromDonHang(donHang, ctdhResponseList);
        });
    }

    @Override
    public DonHangResponse updateStatus(String status, int id) throws Exception {
        DonHang existingDonHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Does not exist MADONHANG"));

        existingDonHang.setTRANGTHAI(status);
        donHangRepository.save(existingDonHang);

        List<CTDHResponse> ctdhResponseList = ctdhRepository.findByMADONHANG(existingDonHang)
                .stream()
                .map(CTDHResponse::fromCTDH)
                .collect(Collectors.toList());

        return DonHangResponse.fromDonHang(existingDonHang, ctdhResponseList);
    }
}
