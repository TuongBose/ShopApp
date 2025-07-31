package com.project.Shopapp.services.hinhanh;

import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.hinhanh.HinhAnhResponse;

import java.util.List;

public interface IHinhAnhService {
    List<HinhAnhResponse> getAllHinhAnhByMaSanPham(SanPham sanPham);
    HinhAnh deleteHinhAnh(int id) throws DataNotFoundException;
}
