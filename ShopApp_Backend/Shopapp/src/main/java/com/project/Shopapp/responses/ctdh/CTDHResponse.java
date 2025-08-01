package com.project.Shopapp.responses.ctdh;

import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.sanpham.SanPhamResponse;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CTDHResponse {
    private int MADONHANG;
    private SanPhamResponse sanPhamResponse;
    private int SOLUONG;
    private BigDecimal GIABAN;
    private BigDecimal TONGTIEN;

    public static CTDHResponse fromCTDH(CTDH ctdh)
    {
        return CTDHResponse.builder()
                .MADONHANG(ctdh.getMADONHANG().getMADONHANG())
                .sanPhamResponse(SanPhamResponse.fromSanPham(ctdh.getMASANPHAM()))
                .SOLUONG(ctdh.getSOLUONG())
                .GIABAN(ctdh.getGIABAN())
                .TONGTIEN(ctdh.getTONGTIEN())
                .build();
    }
}
