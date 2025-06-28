package com.project.Shopapp.Services;

import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Responses.HinhAnhResponse;

import java.util.List;

public interface IHinhAnhService {
    List<HinhAnhResponse> getAllHinhAnhByMaSanPham(SanPham sanPham);
}
