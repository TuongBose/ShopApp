package com.project.Shopapp.services.account;

import com.project.Shopapp.components.JwtTokenUtils;
import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.AccountLoginDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.InvalidPasswordException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.repositories.AccountRepository;
import com.project.Shopapp.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public Account createAccount(AccountDTO accountDTO) throws Exception {
        // Register Account
        if (accountDTO.getSODIENTHOAI() != null && accountRepository.existsBySODIENTHOAI(accountDTO.getSODIENTHOAI()))
            throw new DataIntegrityViolationException("Phone number already exists");

        if (accountDTO.getEMAIL() != null && accountRepository.existsByEMAIL(accountDTO.getEMAIL()))
            throw new DataIntegrityViolationException("Email already exists");

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
    public String login(AccountLoginDTO accountLoginDTO) throws Exception {
        Optional<Account> accountOptional = Optional.empty();
        String subject = null;
        int roleId = accountLoginDTO.isRoleid() ? Account.ADMIN : Account.USER;

        // Check if the user exists by phone number
        if (accountLoginDTO.getSODIENTHOAI() != null && !accountLoginDTO.getSODIENTHOAI().isBlank()) {
            accountOptional = accountRepository.findBySODIENTHOAI(accountLoginDTO.getSODIENTHOAI());
            subject = accountLoginDTO.getSODIENTHOAI();
        }

        if (accountOptional.isEmpty() && accountLoginDTO.getEMAIL() != null) {
            accountOptional = accountRepository.findByEMAIL(accountLoginDTO.getEMAIL());
            subject = accountLoginDTO.getEMAIL();
        }

        if (accountOptional.isEmpty()) {
            throw new DataNotFoundException("Phone number/email or password not found");
        }

        // Return Account
        //return accountOptional.get();
        // Get the existing Account
        Account existingAccount = accountOptional.get();

        // Check password
        if (existingAccount.getFACEBOOK_ACCOUNT_ID() == 0 && existingAccount.getGOOGLE_ACCOUNT_ID() == 0) {
            if (!passwordEncoder.matches(accountLoginDTO.getPASSWORD(), existingAccount.getPassword())) {
                throw new BadCredentialsException("Phone number/email or password not found");
            }
        }

        // Check role account exist
        boolean expectedRole = roleId == Account.ADMIN;
        if (existingAccount.isROLENAME() != expectedRole) {
            throw new RuntimeException("Rolename does not exists");
        }

        // check account is active
        if (!existingAccount.isIS_ACTIVE()) {
            throw new Exception("Account is locked");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                subject,
                accountLoginDTO.getPASSWORD(),
                existingAccount.getAuthorities()
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
    public Account getAccountDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getAccountDetailsFromToken(existingToken.getTOKEN());
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
        if (updateAccountDTO.getEMAIL() != null) {
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

    @Override
    public Page<Account> getAllAccountCustomer(String keyword, Pageable pageable) throws Exception {
        return accountRepository.findAll(keyword, pageable);
    }

    @Override
    @Transactional
    public void resetPassword(int userId, String newPassword) throws DataNotFoundException, InvalidPasswordException {
        Account existingAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingAccount.setPASSWORD(encodedPassword);
        accountRepository.save(existingAccount);

        // reset password => clear token
        List<Token> tokens = tokenRepository.findByUSERID(existingAccount);
        for (Token token : tokens) {
            tokenRepository.delete(token);
        }
    }

    @Override
    @Transactional
    public void blockOrEnable(int userId, boolean active) throws DataNotFoundException {
        Account existingAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));
        existingAccount.setIS_ACTIVE(active);
        accountRepository.save(existingAccount);
    }
}
