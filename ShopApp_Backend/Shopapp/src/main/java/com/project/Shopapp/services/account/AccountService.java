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
import com.project.Shopapp.utils.ValidationUtils;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

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
        if (accountDTO.getSODIENTHOAI() != null && !accountDTO.getSODIENTHOAI().trim().isBlank() &&
                accountRepository.existsBySODIENTHOAI(accountDTO.getSODIENTHOAI()))
            throw new DataIntegrityViolationException("Phone number already exists");

        if (accountDTO.getEMAIL() != null && !accountDTO.getEMAIL().trim().isBlank() &&
                accountRepository.existsByEMAIL(accountDTO.getEMAIL()))
            throw new DataIntegrityViolationException("Email already exists");

        // Convert AccountDTO sang Account
        Account newAccount = Account.builder()
                .FULLNAME(accountDTO.getFULLNAME())
                .PASSWORD(accountDTO.getPASSWORD())
                .DIACHI(accountDTO.getDIACHI())
                .NGAYSINH(accountDTO.getNGAYSINH())
                .profileImage(accountDTO.getProfileImage())
                .facebookAccountId(accountDTO.getFACEBOOKACCOUNTID())
                .googleAccountId(accountDTO.getGOOGLEACCOUNTID())
                .ROLENAME(false)
                .IS_ACTIVE(true)
                .build();

        if(accountDTO.getSODIENTHOAI() != null && !accountDTO.getSODIENTHOAI().trim().isBlank()){
            newAccount.setSODIENTHOAI(accountDTO.getSODIENTHOAI());
        }else if(accountDTO.getEMAIL() != null && !accountDTO.getEMAIL().trim().isBlank()){
            newAccount.setEMAIL(accountDTO.getEMAIL());
        }

        // Kiem tra neu co accountId, khong yeu cau password
        if (!accountDTO.isSocialLogin()) {
            String password = accountDTO.getPASSWORD();
            String encodedPassword = passwordEncoder.encode(password);
            newAccount.setPASSWORD(encodedPassword);
        }

        return accountRepository.save(newAccount);
    }

    @Override
    public Account createUserAdmin(AccountDTO accountDTO) throws Exception {
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
                .facebookAccountId(accountDTO.getFACEBOOKACCOUNTID())
                .googleAccountId(accountDTO.getGOOGLEACCOUNTID())
                .ROLENAME(true)
                .IS_ACTIVE(true)
                .build();

        // Kiem tra neu co accountId, khong yeu cau password
        if (!accountDTO.isSocialLogin()) {
            String password = accountDTO.getPASSWORD();
            String encodedPassword = passwordEncoder.encode(password);
            newAccount.setPASSWORD(encodedPassword);
        }

        return accountRepository.save(newAccount);
    }

    @Override
    public String login(AccountLoginDTO accountLoginDTO) throws Exception {
        Optional<Account> accountOptional = Optional.empty();
        String subject = null;
        int roleId = accountLoginDTO.isRoleid() ? Account.ADMIN : Account.USER;

        // Check by Google Account ID
        if (accountLoginDTO.getGoogleAccountId() != null && accountLoginDTO.isGoogleAccountIdValid()) {
            accountOptional = accountRepository.findByGoogleAccountId(accountLoginDTO.getGoogleAccountId());
            subject = "Google:" + accountLoginDTO.getGoogleAccountId();
            // Neu khong tim thay nguoi dung tao ban ghi moi
            if (accountOptional.isEmpty()) {
                Account newAccount = Account.builder()
                        .FULLNAME(accountLoginDTO.getFullName() != null ? accountLoginDTO.getFullName() : "")
                        .EMAIL(accountLoginDTO.getEMAIL() != null ? accountLoginDTO.getEMAIL() : "")
                        .profileImage(accountLoginDTO.getProfileImage() != null ? accountLoginDTO.getProfileImage() : "")
                        .ROLENAME(accountLoginDTO.isRoleid())
                        .googleAccountId(accountLoginDTO.getGoogleAccountId())
                        .PASSWORD("")
                        .DIACHI("")
                        .SODIENTHOAI(accountLoginDTO.getSODIENTHOAI() != null ? accountLoginDTO.getSODIENTHOAI() : "")
                        .NGAYSINH(new Date())
                        .IS_ACTIVE(true)
                        .build();
                // Luu nguoi dung moi vao CSDL
                accountRepository.save(newAccount);

                // Optional tro thanh co gia tri voi nguoi dung moi
                accountOptional = Optional.of(newAccount);
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", accountLoginDTO.getEMAIL());
            return jwtTokenUtils.generateToken(accountOptional.get());
        }

        // Check by Facebook Account ID
        if (accountLoginDTO.isFacebookAccountIdValid()) {
            accountOptional = accountRepository.findByFacebookAccountId(accountLoginDTO.getFacebookAccountId());
            subject = "Facebook:" + accountLoginDTO.getFacebookAccountId();
            // Neu khong tim thay nguoi dung tao ban ghi moi
            if (accountOptional.isEmpty()) {
                Account newAccount = Account.builder()
                        .FULLNAME(accountLoginDTO.getFullName() != null ? accountLoginDTO.getFullName() : "")
                        .EMAIL(accountLoginDTO.getEMAIL() != null ? accountLoginDTO.getEMAIL() : "")
                        .profileImage(accountLoginDTO.getProfileImage() != null ? accountLoginDTO.getProfileImage() : "")
                        .ROLENAME(accountLoginDTO.isRoleid())
                        .facebookAccountId(accountLoginDTO.getFacebookAccountId())
                        .PASSWORD("")
                        .DIACHI("")
                        .SODIENTHOAI(accountLoginDTO.getSODIENTHOAI() != null ? accountLoginDTO.getSODIENTHOAI() : "")
                        .NGAYSINH(new Date())
                        .IS_ACTIVE(true)
                        .build();
                // Luu nguoi dung moi vao CSDL
                accountRepository.save(newAccount);

                // Optional tro thanh co gia tri voi nguoi dung moi
                accountOptional = Optional.of(newAccount);
            }
        }

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
        if ((existingAccount.getFacebookAccountId() == null || existingAccount.getFacebookAccountId().trim().isBlank()) &&
                (existingAccount.getGoogleAccountId() == null || existingAccount.getGoogleAccountId().trim().isBlank())) {
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

        String subject = jwtTokenUtils.getSubject(token);
        Optional<Account> account = accountRepository.findBySODIENTHOAI(subject);

        if (account.isEmpty() && ValidationUtils.isValidEmail(subject)) {
            account = accountRepository.findByEMAIL(subject);
        }

        return account.orElseThrow(() -> new Exception("User not found"));
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
        if (updateAccountDTO.getFACEBOOKACCOUNTID() != null) {
            existingAccount.setFacebookAccountId(updateAccountDTO.getFACEBOOKACCOUNTID());
        }
        if (updateAccountDTO.getGOOGLEACCOUNTID() != null) {
            existingAccount.setGoogleAccountId(updateAccountDTO.getGOOGLEACCOUNTID());
        }

        // Update password
        if (updateAccountDTO.getPASSWORD() != null && !updateAccountDTO.getPASSWORD().isEmpty()) {
            if (!updateAccountDTO.getPASSWORD().equals(updateAccountDTO.getRETYPEPASSWORD())) {
                throw new DataNotFoundException("Password and retype password not the same");
            }
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

    @Override
    public void changeProfileImage(int userId, String imageName) throws Exception {
        Account existingAccount = accountRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        existingAccount.setProfileImage(imageName);
        accountRepository.save(existingAccount);
    }

    @Override
    public String loginSocial(AccountLoginDTO accountLoginDTO) throws Exception {
        return "";
    }

    @Override
    public Account getAccountByEmail(String email) throws Exception {
        if (StringUtils.isEmpty(email)) {
            throw new Exception("Email is empty");
        }
        Optional<Account> optionalUser = accountRepository.findByEMAIL(email);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        return optionalUser.get();
    }
}
