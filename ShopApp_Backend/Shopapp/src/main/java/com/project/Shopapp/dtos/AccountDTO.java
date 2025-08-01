package com.project.Shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO extends SocialAccountDTO{
    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;

    private String RETYPEPASSWORD;
    private String EMAIL;
    private String FULLNAME;
    private String DIACHI;
    private String SODIENTHOAI;
    private Date NGAYSINH;
    private String profileImage;

    @JsonProperty("FACEBOOK_ACCOUNT_ID")
    private String FACEBOOKACCOUNTID;

    @JsonProperty("GOOGLE_ACCOUNT_ID")
    private String GOOGLEACCOUNTID;
}
