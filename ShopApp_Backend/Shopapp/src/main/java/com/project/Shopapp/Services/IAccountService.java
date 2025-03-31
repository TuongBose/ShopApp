package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.Models.Account;

public interface IAccountService {
    Account createAccount(AccountDTO accountDTO);
    String login(String SODIENTHOAI, String PASSWORD);
}
