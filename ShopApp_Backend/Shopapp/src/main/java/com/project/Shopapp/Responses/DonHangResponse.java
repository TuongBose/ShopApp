package com.project.Shopapp.Responses;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonHangResponse {
    private int MADONHANG;
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
    private List<CTDHResponse> ctdhList;
}
