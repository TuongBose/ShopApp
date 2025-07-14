package com.project.Shopapp.responses;

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
