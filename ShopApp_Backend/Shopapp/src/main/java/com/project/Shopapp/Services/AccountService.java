package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountDTO accountDTO) {
        String SODIENTHOAI = accountDTO.getSODIENTHOAI();
        if (accountRepository.existsBySODIENTHOAI(SODIENTHOAI))
            throw new DataIntegrityViolationException("So dien thoai nay da ton tai");

        // Convert AccountDTO sang Account
        Account newAccount = Account.builder()
                .FULLNAME(accountDTO.getFULLNAME())
                .SODIENTHOAI(accountDTO.getSODIENTHOAI())
                .PASSWORD(accountDTO.getPASSWORD())
                .EMAIL(accountDTO.getEMAIL())
                .DIACHI(accountDTO.getDIACHI())
                .NGAYSINH(accountDTO.getNGAYSINH())
                .FACEBOOK_ACCOUNT_ID(accountDTO.getFACEBOOKACCOUNTID())
                .GOOGLE_ACCOUNT_ID(accountDTO.getGOOGLEACCOUNTID())
                .ROLENAME(false)
                .build();

        // Kiem tra neu co accountId, khong yeu cau password
        if (accountDTO.getFACEBOOKACCOUNTID() == 0 && accountDTO.getGOOGLEACCOUNTID() == 0) {
            String pass = accountDTO.getPASSWORD();
        }

        return accountRepository.save(newAccount);
    }

    @Override
    public String login(String SODIENTHOAI, String PASSWORD) {
        return null;
    }
}
