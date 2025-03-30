package com.project.Shopapp.DTOs;
import lombok.*;

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
    private int GIABAN;
    private int TONGTIEN;
}
