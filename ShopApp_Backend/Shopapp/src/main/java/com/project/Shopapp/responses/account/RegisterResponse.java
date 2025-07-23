package com.project.Shopapp.responses.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.Shopapp.models.Account;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("account")
    private Account account;
}
