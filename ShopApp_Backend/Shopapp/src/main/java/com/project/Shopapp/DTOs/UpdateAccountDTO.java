package com.project.Shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.security.PrivateKey;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAccountDTO {
    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;
    private String EMAIL;
    private String FULLNAME;
    private String DIACHI;

    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;
    private Date NGAYSINH;
    @JsonProperty("FACEBOOK_ACCOUNT_ID")
    private int FACEBOOKACCOUNTID;
    @JsonProperty("GOOGLE_ACCOUNT_ID")
    private int GOOGLEACCOUNTID;
}
