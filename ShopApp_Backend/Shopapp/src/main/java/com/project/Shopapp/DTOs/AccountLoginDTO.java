package com.project.Shopapp.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginDTO {
    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;
    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;
}
