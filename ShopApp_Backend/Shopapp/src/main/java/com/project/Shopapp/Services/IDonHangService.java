package com.project.Shopapp.Services;

import com.project.Shopapp.Models.DonHang;
import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Responses.DonHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDonHangService {
    DonHangResponse createDonHang(DonHangDTO donHangDTO) throws Exception;
    List<DonHang> getDonHangByMASANPHAM(int id);
    DonHangResponse getDonHangByMADONHANG(int id) throws Exception;
    List<DonHang> getDonHangByUSERID(int id) throws Exception;
    DonHang updateDonHang(int id, DonHangDTO donHangDTO) throws Exception;
    void deleteDonHang(int id) throws Exception;
    Page<DonHangResponse> getAllDonHangByKeyword(String keyword, Pageable pageable);
}
