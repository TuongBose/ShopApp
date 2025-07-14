package com.project.Shopapp.services.sanphamredis;

import com.project.Shopapp.responses.SanPhamResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ISanPhamRedisService {
    void clear();

    List<SanPhamResponse> getAllSanPham(String keyword, int MALOAISANPHAM, PageRequest pageRequest) throws Exception;

    void saveAllSanPham(List<SanPhamResponse> sanPhamResponseList, String keyword,
                        int MALOAISANPHAM, PageRequest pageRequest) throws Exception;
}
