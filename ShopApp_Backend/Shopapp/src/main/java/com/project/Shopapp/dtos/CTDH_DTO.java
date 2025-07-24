package com.project.Shopapp.dtos;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CTDH_DTO {
    private int MADONHANG;
    private int MASANPHAM;
    private int SOLUONG;
    private BigDecimal GIABAN;
    private BigDecimal TONGTIEN;
}
