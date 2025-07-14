package com.project.Shopapp.services.account;

import com.project.Shopapp.components.JwtTokenUtils;
import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public Account createAccount(AccountDTO accountDTO) throws Exception {
        // Register Account
        String SODIENTHOAI = accountDTO.getSODIENTHOAI();
        if (accountRepository.existsBySODIENTHOAI(SODIENTHOAI))
            throw new RuntimeException("So dien thoai nay da ton tai");

        // Convert AccountDTO sang Account
        Account newAccount = Account.builder()
                .FULLNAME(accountDTO.getFULLNAME())
                .PASSWORD(accountDTO.getPASSWORD())
                .SODIENTHOAI(accountDTO.getSODIENTHOAI())
                .EMAIL(accountDTO.getEMAIL())
                .DIACHI(accountDTO.getDIACHI())
                .NGAYSINH(accountDTO.getNGAYSINH())
                .FACEBOOK_ACCOUNT_ID(accountDTO.getFACEBOOKACCOUNTID())
                .GOOGLE_ACCOUNT_ID(accountDTO.getGOOGLEACCOUNTID())
                .ROLENAME(false)
                .IS_ACTIVE(true)
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
    public String login(String SODIENTHOAI, String PASSWORD, Integer roleId) throws Exception {
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

        // Check role account exist
        if (roleId != null) {
            boolean expectedRole = roleId == Account.ADMIN;
            if (existingAccount.isROLENAME() != expectedRole) {
                throw new RuntimeException("Rolename does not exists");
            }
        }

        // check account is active
        if (!accountOptional.get().isIS_ACTIVE()) {
            throw new Exception("Account is locked");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                SODIENTHOAI, PASSWORD, existingAccount.getAuthorities()
        );
        // Authenticate with Java Spring Security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingAccount); // Return token
    }

    @Override
    public Account getAccountDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }

        String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
        Optional<Account> account = accountRepository.findBySODIENTHOAI(phoneNumber);

        if (account.isPresent()) {
            return account.get();
        } else {
            throw new Exception("Account not found");
        }
    }

    @Override
    @Transactional
    public Account updateAccount(UpdateAccountDTO updateAccountDTO, int userId) throws Exception {
        Account existingAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String newPhoneNumber = updateAccountDTO.getSODIENTHOAI();
        if (!existingAccount.getSODIENTHOAI().equals(newPhoneNumber)
                && accountRepository.existsBySODIENTHOAI(newPhoneNumber)) {
            throw new RuntimeException("Phone number already exists");
        }

        // Check null and update
        if (updateAccountDTO.getFULLNAME() != null) {
            existingAccount.setFULLNAME(updateAccountDTO.getFULLNAME());
        }
        if (updateAccountDTO.getSODIENTHOAI() != null) {
            existingAccount.setSODIENTHOAI(updateAccountDTO.getSODIENTHOAI());
        }
        if (updateAccountDTO.getDIACHI() != null) {
            existingAccount.setDIACHI(updateAccountDTO.getDIACHI());
        }
        if (updateAccountDTO.getNGAYSINH() != null) {
            existingAccount.setNGAYSINH(updateAccountDTO.getNGAYSINH());
        }
        if(updateAccountDTO.getEMAIL()!=null){
            existingAccount.setEMAIL(updateAccountDTO.getEMAIL());
        }
        if (updateAccountDTO.getFACEBOOKACCOUNTID() > 0) {
            existingAccount.setFACEBOOK_ACCOUNT_ID(updateAccountDTO.getFACEBOOKACCOUNTID());
        }
        if (updateAccountDTO.getGOOGLEACCOUNTID() > 0) {
            existingAccount.setGOOGLE_ACCOUNT_ID(updateAccountDTO.getGOOGLEACCOUNTID());
        }

        // Update password
        if (updateAccountDTO.getPASSWORD() != null && !updateAccountDTO.getPASSWORD().isEmpty()) {
            String newPassword = updateAccountDTO.getPASSWORD();
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingAccount.setPASSWORD(encodedPassword);
        }

        return accountRepository.save(existingAccount);
    }
}
