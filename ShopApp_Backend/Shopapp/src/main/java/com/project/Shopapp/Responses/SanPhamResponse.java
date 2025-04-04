package com.project.Shopapp.Responses;

import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Models.BaseEntity;
import com.project.Shopapp.Models.SanPham;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamResponse extends BaseResponse {
    private String TENSANPHAM;
    private int GIA;
    private int MATHUONGHIEU;
    private String MOTA;
    private int SOLUONGTONKHO;
    private int MALOAISANPHAM;

    public static SanPhamResponse fromSanPham(SanPham sanPham)
    {
        SanPhamResponse newSanPhamResponse = SanPhamResponse
                .builder()
                .TENSANPHAM(sanPham.getTENSANPHAM())
                .GIA(sanPham.getGIA())
                .MATHUONGHIEU(sanPham.getMATHUONGHIEU().getMATHUONGHIEU())
                .MOTA(sanPham.getMOTA())
                .SOLUONGTONKHO(sanPham.getSOLUONGTONKHO())
                .MALOAISANPHAM(sanPham.getMALOAISANPHAM().getMALOAISANPHAM())
                .build();
        newSanPhamResponse.setNGAYTAO(sanPham.getNGAYTAO());
        newSanPhamResponse.setCHINHSUA(sanPham.getCHINHSUA());
        return newSanPhamResponse;
    }
}
