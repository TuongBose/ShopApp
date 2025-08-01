package com.project.Shopapp.responses.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.Shopapp.models.Account;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private int USERID;
    private String FULLNAME;
    private String DIACHI;
    private String SODIENTHOAI;
    private Date NGAYSINH;
    private String EMAIL;

    @JsonProperty("is_active")
    private boolean IS_ACTIVE;

    @JsonProperty("facebook_account_id")
    private String FACEBOOK_ACCOUNT_ID;

    @JsonProperty("google_account_id")
    private String GOOGLE_ACCOUNT_ID;

    @JsonProperty("profile_image")
    private String profileImage;

    private boolean ROLENAME;

    public static AccountResponse fromAccount(Account account) {
        return AccountResponse
                .builder()
                .USERID(account.getUSERID())
                .FULLNAME(account.getFULLNAME())
                .DIACHI(account.getDIACHI())
                .SODIENTHOAI(account.getSODIENTHOAI())
                .NGAYSINH(account.getNGAYSINH())
                .EMAIL(account.getEMAIL())
                .profileImage(account.getProfileImage())
                .IS_ACTIVE(account.isIS_ACTIVE())
                .FACEBOOK_ACCOUNT_ID(account.getFacebookAccountId())
                .GOOGLE_ACCOUNT_ID(account.getGoogleAccountId())
                .ROLENAME(account.isROLENAME())
                .build();
    }
}
