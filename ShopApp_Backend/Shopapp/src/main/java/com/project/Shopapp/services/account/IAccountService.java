package com.project.Shopapp.services.account;

import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.models.Account;

public interface IAccountService {
    Account createAccount(AccountDTO accountDTO) throws Exception;
    String login(String SODIENTHOAI, String PASSWORD, Integer roleId) throws Exception;
    Account getAccountDetailsFromToken(String token) throws Exception;
    Account updateAccount(UpdateAccountDTO updateAccountDTO, int userId) throws Exception;
}
