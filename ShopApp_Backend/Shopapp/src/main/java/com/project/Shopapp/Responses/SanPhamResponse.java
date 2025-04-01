package com.project.Shopapp.Responses;

import com.project.Shopapp.Models.BaseEntity;
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
}
