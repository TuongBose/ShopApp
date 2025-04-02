package com.project.Shopapp.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThuongHieuDTO {
    @NotEmpty(message = "Ten thuong hieu khong duoc bo trong")
    private String TENTHUONGHIEU;
}
