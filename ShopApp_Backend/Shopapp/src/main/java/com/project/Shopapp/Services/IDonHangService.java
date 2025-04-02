package com.project.Shopapp.Services;

import com.project.Shopapp.Models.DonHang;
import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Responses.DonHangResponse;

import java.util.List;

public interface IDonHangService {
    DonHangResponse createDonHang(DonHangDTO donHangDTO) throws Exception;
    DonHang getDonHangByMASANPHAM(int id);
    List<DonHang> getAllDonHang();
    DonHang updateDonHang(int id, DonHangDTO donHangDTO);
    void deleteDonHang(int id);
}
