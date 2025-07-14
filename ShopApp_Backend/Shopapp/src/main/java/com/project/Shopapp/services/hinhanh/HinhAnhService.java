package com.project.Shopapp.services.hinhanh;

import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.repositories.HinhAnhRepository;
import com.project.Shopapp.responses.HinhAnhResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HinhAnhService implements IHinhAnhService {
    private final HinhAnhRepository hinhAnhRepository;

    @Override
    public List<HinhAnhResponse> getAllHinhAnhByMaSanPham(SanPham sanPham) {
        List<HinhAnh> hinhAnhList = hinhAnhRepository.findByMASANPHAM(sanPham);

        List<HinhAnhResponse> hinhAnhResponseList = new ArrayList<>();
        if (hinhAnhList.isEmpty()) {
            throw new RuntimeException("Không có hình ảnh nào");
        } else {
            for (HinhAnh hinhAnh : hinhAnhList) {
                HinhAnhResponse newHinhAnhResponse = HinhAnhResponse
                        .builder()
                        .MALOAISANPHAM(hinhAnh.getMALOAISANPHAM().getMALOAISANPHAM())
                        .MASANPHAM((hinhAnh.getMASANPHAM().getMASANPHAM()))
                        .TENHINHANH(hinhAnh.getTENHINHANH())
                        .build();
                hinhAnhResponseList.add(newHinhAnhResponse);
            }
            return hinhAnhResponseList;
        }
    }
}
