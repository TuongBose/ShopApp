package com.project.Shopapp.responses.ctdh;

import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.models.SanPham;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CTDHResponse {
    private int MADONHANG;
    private SanPham MASANPHAM;
    private int SOLUONG;
    private int GIABAN;
    private int TONGTIEN;

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
