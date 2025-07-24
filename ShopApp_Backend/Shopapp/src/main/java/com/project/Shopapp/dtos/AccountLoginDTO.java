package com.project.Shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountLoginDTO {
    @NotBlank(message = "Email khong duoc bo trong")
    private String EMAIL;

    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;

    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;

    private boolean roleid;
}
