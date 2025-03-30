package com.project.Shopapp.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoaiSanPhamDTO {
    @NotEmpty(message = "Ten loai san pham khong duoc bo trong")
    private String TENLOAISANPHAM;
}
