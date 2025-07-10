package com.project.Shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Models.BaseEntity;
import com.project.Shopapp.Models.HinhAnh;
import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Services.HinhAnhService;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamResponse extends BaseResponse {

    private int MASANPHAM;
    private String TENSANPHAM;
    private int GIA;
    private int MATHUONGHIEU;
    private String MOTA;
    private int SOLUONGTONKHO;
    private int MALOAISANPHAM;
    private String THUMBNAIL;
    private List<HinhAnhResponse> hinhAnhUrls;

    public static SanPhamResponse fromSanPham(SanPham sanPham) {
        SanPhamResponse newSanPhamResponse = SanPhamResponse
                .builder()
                .MASANPHAM(sanPham.getMASANPHAM())
                .TENSANPHAM(sanPham.getTENSANPHAM())
                .GIA(sanPham.getGIA())
                .MATHUONGHIEU(sanPham.getMATHUONGHIEU().getMATHUONGHIEU())
                .MOTA(sanPham.getMOTA())
                .SOLUONGTONKHO(sanPham.getSOLUONGTONKHO())
                .MALOAISANPHAM(sanPham.getMALOAISANPHAM().getMALOAISANPHAM())
                .THUMBNAIL(sanPham.getTHUMBNAIL())
                .build();
        newSanPhamResponse.setNGAYTAO(sanPham.getNGAYTAO());
        newSanPhamResponse.setCHINHSUA(sanPham.getCHINHSUA());
        return newSanPhamResponse;
    }

    public static SanPhamResponse fromSanPhamForDetail(SanPham sanPham, List<HinhAnhResponse> hinhAnhList) {
        SanPhamResponse newSanPhamResponse = SanPhamResponse
                .builder()
                .MASANPHAM(sanPham.getMASANPHAM())
                .TENSANPHAM(sanPham.getTENSANPHAM())
                .GIA(sanPham.getGIA())
                .MATHUONGHIEU(sanPham.getMATHUONGHIEU().getMATHUONGHIEU())
                .MOTA(sanPham.getMOTA())
                .SOLUONGTONKHO(sanPham.getSOLUONGTONKHO())
                .MALOAISANPHAM(sanPham.getMALOAISANPHAM().getMALOAISANPHAM())
                .THUMBNAIL(sanPham.getTHUMBNAIL())
                .hinhAnhUrls(hinhAnhList)
                .build();
        newSanPhamResponse.setNGAYTAO(sanPham.getNGAYTAO());
        newSanPhamResponse.setCHINHSUA(sanPham.getCHINHSUA());
        return newSanPhamResponse;
    }
}
