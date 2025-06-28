package com.project.Shopapp.Responses;

import com.project.Shopapp.Models.LoaiSanPham;
import com.project.Shopapp.Models.SanPham;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HinhAnhResponse {
    private int MASANPHAM;
    private int MALOAISANPHAM;
    private String TENHINHANH;
}
