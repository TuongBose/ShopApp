package com.project.Shopapp.services.hinhanh;

import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.HinhAnhResponse;

import java.util.List;

public interface IHinhAnhService {
    List<HinhAnhResponse> getAllHinhAnhByMaSanPham(SanPham sanPham);
}
