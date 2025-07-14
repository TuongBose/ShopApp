package com.project.Shopapp.services.thuonghieu;

import com.project.Shopapp.models.ThuongHieu;
import com.project.Shopapp.dtos.ThuongHieuDTO;

import java.util.List;

public interface IThuongHieuService {
    ThuongHieu createThuongHieu(ThuongHieuDTO thuongHieuDTO);
    List<ThuongHieu> getAllThuongHieu();
    ThuongHieu getThuongHieuByMATHUONGHIEU(int id);
    ThuongHieu updateThuongHieu(int id, ThuongHieuDTO thuongHieuDTO);
    void deleteThuongHieu(int id);
}
