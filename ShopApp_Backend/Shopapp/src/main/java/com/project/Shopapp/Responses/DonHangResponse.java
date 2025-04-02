package com.project.Shopapp.Responses;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonHangResponse {
    private int USERID;
    private String FULLNAME;
    private String EMAIL;
    private String SODIENTHOAI;
    private String DIACHI;
    private String GHICHU;
    private String TRANGTHAI;
    private LocalDate NGAYDATHANG;
    private int TONGTIEN;
    private String PHUONGTHUCTHANHTOAN;
    private boolean IS_ACTIVE;
}
