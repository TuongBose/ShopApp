package com.project.Shopapp.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class HinhAnhDTO {
    private int MASANPHAM;
    private int MALOAISANPHAM;
    private String TENHINHANH;
}
