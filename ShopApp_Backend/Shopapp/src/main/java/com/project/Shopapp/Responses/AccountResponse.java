package com.project.Shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Models.SanPham;
import jakarta.persistence.Column;
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
    private int FACEBOOK_ACCOUNT_ID;

    @JsonProperty("google_account_id")
    private int GOOGLE_ACCOUNT_ID;

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
                .IS_ACTIVE(account.isIS_ACTIVE())
                .FACEBOOK_ACCOUNT_ID(account.getFACEBOOK_ACCOUNT_ID())
                .GOOGLE_ACCOUNT_ID(account.getGOOGLE_ACCOUNT_ID())
                .ROLENAME(account.isROLENAME())
                .build();
    }
}
