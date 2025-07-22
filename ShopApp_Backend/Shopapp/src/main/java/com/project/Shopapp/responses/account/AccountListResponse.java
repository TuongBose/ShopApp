package com.project.Shopapp.responses.account;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountListResponse {
    private List<AccountResponse> accountResponseList;
    private int totalPages;
}
