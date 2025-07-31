package com.project.Shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DonHangDTO {
    private int USERID;
    private String FULLNAME;
    private String EMAIL;

    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;

    private String DIACHI;
    private String GHICHU;
    private BigDecimal TONGTIEN;
    private String PHUONGTHUCTHANHTOAN;

    @JsonProperty("status")
    private String status;

    private List<CartItemDTO> cartitems;
}
