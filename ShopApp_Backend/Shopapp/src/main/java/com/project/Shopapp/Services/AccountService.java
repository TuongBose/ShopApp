package com.project.Shopapp.Services;

import com.project.Shopapp.Components.JwtTokenUtil;
import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public Account createAccount(AccountDTO accountDTO) throws Exception {
        // Register Account
        String SODIENTHOAI = accountDTO.getSODIENTHOAI();
        if (accountRepository.existsBySODIENTHOAI(SODIENTHOAI))
            throw new DataIntegrityViolationException("So dien thoai nay da ton tai");

        // Convert AccountDTO sang Account
        Account newAccount = Account.builder()
                .FULLNAME(accountDTO.getFULLNAME())
                .SODIENTHOAI(accountDTO.getSODIENTHOAI())
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
            String encodedPassword = passwordEncoder.encode(pass);
            newAccount.setPASSWORD(encodedPassword);
        }

        return accountRepository.save(newAccount);
    }

    @Override
    public String login(String SODIENTHOAI, String PASSWORD) throws Exception {
        Optional<Account> accountOptional = accountRepository.findBySODIENTHOAI(SODIENTHOAI);
        if (accountOptional.isEmpty())
            throw new RuntimeException("Khong tim thay SODIENTHOAI hoac PASSWORD");

        // Return Account
        //return accountOptional.get();

        Account existingAccount = accountOptional.get();

        // Check password
        if (existingAccount.getFACEBOOK_ACCOUNT_ID() == 0 && existingAccount.getGOOGLE_ACCOUNT_ID() == 0) {
            if (!passwordEncoder.matches(PASSWORD, existingAccount.getPassword())) {
                throw new BadCredentialsException("Sai SODIENTHOAI hoac PASSWORD");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                SODIENTHOAI, PASSWORD, existingAccount.getAuthorities()
        );
        // Authenticate with Java Spring Security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingAccount); // Return token
    }
}
