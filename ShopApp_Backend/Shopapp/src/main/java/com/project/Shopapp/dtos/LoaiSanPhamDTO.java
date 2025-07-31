package com.project.Shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoaiSanPhamDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String TENLOAISANPHAM;
}
