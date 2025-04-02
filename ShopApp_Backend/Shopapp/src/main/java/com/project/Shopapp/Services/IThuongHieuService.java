package com.project.Shopapp.Services;

import com.project.Shopapp.Models.ThuongHieu;
import com.project.Shopapp.DTOs.ThuongHieuDTO;

import java.util.List;

public interface IThuongHieuService {
    ThuongHieu createThuongHieu(ThuongHieuDTO thuongHieuDTO);
    List<ThuongHieu> getAllThuongHieu();
    ThuongHieu getThuongHieuByMATHUONGHIEU(int id);
    ThuongHieu updateThuongHieu(int id, ThuongHieuDTO thuongHieuDTO);
    void deleteThuongHieu(int id);
}
