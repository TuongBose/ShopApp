package com.project.Shopapp.services.account;

import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.AccountLoginDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.InvalidPasswordException;
import com.project.Shopapp.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {
    Account createAccount(AccountDTO accountDTO) throws Exception;
    Account createUserAdmin(AccountDTO accountDTO) throws Exception;
    //String login(String EMAIL, String SODIENTHOAI, String PASSWORD, Integer roleId) throws Exception;
    String login(AccountLoginDTO accountLoginDTO) throws Exception;
    Account getAccountDetailsFromToken(String token) throws Exception;
    Account getAccountDetailsFromRefreshToken(String refreshToken) throws Exception;
    Account updateAccount(UpdateAccountDTO updateAccountDTO, int userId) throws Exception;
    Page<Account> getAllAccountCustomer(String keyword, Pageable pageable) throws Exception;
    void resetPassword(int userId, String newPassword) throws DataNotFoundException, InvalidPasswordException;
    void blockOrEnable(int userId, boolean active) throws DataNotFoundException;
    void changeProfileImage(int userId, String imageName) throws Exception;
    String loginSocial(AccountLoginDTO accountLoginDTO) throws Exception;
}
