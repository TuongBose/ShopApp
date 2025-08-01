package com.project.Shopapp.services.donhang;

import com.project.Shopapp.models.DonHang;
import com.project.Shopapp.dtos.DonHangDTO;
import com.project.Shopapp.responses.donhang.DonHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDonHangService {
    DonHangResponse createDonHang(DonHangDTO donHangDTO) throws Exception;
    DonHangResponse getDonHangByMADONHANG(int id) throws Exception;
    List<DonHang> getDonHangByUSERID(int id) throws Exception;
    DonHang updateDonHang(int id, DonHangDTO donHangDTO) throws Exception;
    void deleteDonHang(int id) throws Exception;
    Page<DonHangResponse> getAllDonHangByKeyword(String keyword, Pageable pageable);
    DonHangResponse updateStatus(String status, int id) throws Exception;
}
