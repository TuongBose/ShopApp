package com.project.Shopapp.responses.sanpham;

import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.BaseResponse;
import com.project.Shopapp.responses.hinhanh.HinhAnhResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamResponse extends BaseResponse {

    private int MASANPHAM;
    private String TENSANPHAM;
    private BigDecimal GIA;
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
