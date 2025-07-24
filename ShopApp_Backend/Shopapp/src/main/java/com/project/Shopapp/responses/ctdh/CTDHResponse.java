package com.project.Shopapp.responses.ctdh;

import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.models.SanPham;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CTDHResponse {
    private int MADONHANG;
    private SanPham MASANPHAM;
    private int SOLUONG;
    private BigDecimal GIABAN;
    private BigDecimal TONGTIEN;

    public static CTDHResponse fromCTDH(CTDH ctdh)
    {
        return CTDHResponse.builder()
                .MADONHANG(ctdh.getMADONHANG().getMADONHANG())
                .MASANPHAM(ctdh.getMASANPHAM())
                .SOLUONG(ctdh.getSOLUONG())
                .GIABAN(ctdh.getGIABAN())
                .TONGTIEN(ctdh.getTONGTIEN())
                .build();
    }
}
