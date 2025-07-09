package com.project.Shopapp.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountLoginDTO {
    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;
    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;

    private boolean roleid;
}
