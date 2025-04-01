package com.project.Shopapp.Responses;

import com.project.Shopapp.Models.SanPham;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamListResponse {
    private List<SanPhamResponse> sanPhamResponseList;
    private int tongSoTrang;
}
