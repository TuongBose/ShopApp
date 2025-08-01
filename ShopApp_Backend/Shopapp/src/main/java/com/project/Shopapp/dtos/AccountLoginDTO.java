package com.project.Shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountLoginDTO extends  SocialAccountDTO{
    @NotBlank(message = "Email khong duoc bo trong")
    private String EMAIL;

    @NotBlank(message = "So dien thoai khong duoc bo trong")
    private String SODIENTHOAI;

    @NotBlank(message = "Password khong duoc bo trong")
    private String PASSWORD;

    @JsonProperty("profile_image")
    private String profileImage;

    private String fullName;
    private String googleAccountId;
    private String facebookAccountId;
    private boolean roleid;

    public boolean isPasswordBlank() {
        return PASSWORD == null || PASSWORD.trim().isEmpty();
    }

    // Kiem tra facebookAccountId co hop le khong
    public boolean isFacebookAccountIdValid() {
        return facebookAccountId != null && !facebookAccountId.isEmpty();
    }

    // Kiem tra googleAccountId co hop le khong
    public boolean isGoogleAccountIdValid() {
        return googleAccountId != null && !googleAccountId.isEmpty();
    }
}
