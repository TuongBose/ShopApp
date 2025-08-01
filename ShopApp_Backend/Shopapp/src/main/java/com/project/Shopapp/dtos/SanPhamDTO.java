package com.project.Shopapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamDTO {
    @NotBlank(message = "Ten san pham khong duoc bo trong")
    @Size(min = 5, max = 200, message = "Ten san pham phai tu 5 den 200 ky tu")
    private String TENSANPHAM;

    @Min(value = 0, message = "Gia san pham phai lon hon hoac bang 0")
    private BigDecimal GIA;
    private int MATHUONGHIEU;
    private String MOTA;
    private int SOLUONGTONKHO;
    private int MALOAISANPHAM;
    private String THUMBNAIL;
}
