package com.project.Shopapp.controllers;

import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.AccountLoginDTO;
import com.project.Shopapp.dtos.RefreshTokenDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.InvalidPasswordException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.responses.account.AccountListResponse;
import com.project.Shopapp.responses.account.AccountResponse;
import com.project.Shopapp.responses.account.LoginResponse;
import com.project.Shopapp.services.account.AccountService;
import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.services.token.TokenService;
import com.project.Shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final LocalizationUtils localizationUtils;
    private final TokenService tokenService;

    private boolean isMobileDevice(String accountAgent) {
        // Kiem tra User-Agent header de xac dinh thiet bi di dong
        // Vi du don gian:
        return accountAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDTO accountDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!accountDTO.getPASSWORD().equals(accountDTO.getRETYPEPASSWORD()))
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));

            Account newAccount = accountService.createAccount(accountDTO);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody AccountLoginDTO accountLoginDTO,
            HttpServletRequest request) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = accountService.login(
                    accountLoginDTO.getSODIENTHOAI(),
                    accountLoginDTO.getPASSWORD(),
                    accountLoginDTO.isRoleid() ? Account.ADMIN : Account.USER
            );
            String accountAgent = request.getHeader("User-Agent");
            Account accountDetail = accountService.getAccountDetailsFromToken(token);
            Token jwtToken = tokenService.addToken(accountDetail, token, isMobileDevice(accountAgent));

            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .tokenType(jwtToken.getTOKEN_TYPE())
                            .refreshToken(jwtToken.getRefreshToken())
                            .userName(accountDetail.getFULLNAME())
                            .role(accountDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .id(accountDetail.getUSERID())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            Account accountDetail = accountService.getAccountDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), accountDetail);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message("Refresh token successfully")
                    .token(jwtToken.getTOKEN())
                    .tokenType(jwtToken.getTOKEN_TYPE())
                    .refreshToken(jwtToken.getRefreshToken())
                    .userName(accountDetail.getFULLNAME())
                    .role(accountDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(accountDetail.getUSERID())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAccountDetails(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            Account account = accountService.getAccountDetailsFromToken(extractedToken);
            return ResponseEntity.ok(AccountResponse.fromAccount(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/details/{userId}")
    //@Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateAccountDetails(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int userId,
            @RequestBody UpdateAccountDTO updateAccountDTO
    ) {
        try {
            String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
            Account account = accountService.getAccountDetailsFromToken(extractedToken);

            // Đảm bảo rằng user gọi request chứa token phải trùng với user muốn update
            if (account.getUSERID() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Account updateAccount = accountService.updateAccount(updateAccountDTO, userId);
            return ResponseEntity.ok(AccountResponse.fromAccount(updateAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllAccountCustomer(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("USERID").ascending()
            );
            Page<AccountResponse> accountPage = accountService.getAllAccountCustomer(keyword, pageRequest)
                    .map(AccountResponse::fromAccount);

            // Lay tong so trang
            int totalPages = accountPage.getTotalPages();
            List<AccountResponse> accountResponses = accountPage.getContent();
            return ResponseEntity.ok(AccountListResponse
                    .builder()
                    .accountResponseList(accountResponses)
                    .totalPages(totalPages)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-password/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword(@Valid @PathVariable int userId) {
        try {
            String newPassword = UUID.randomUUID().toString().substring(0, 5); // Create new password
            accountService.resetPassword(userId, newPassword);
            return ResponseEntity.ok(newPassword);
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body("Invalid password");
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body("Account not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/block/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> block(
            @Valid @PathVariable int userId,
            @Valid @PathVariable int active
    ) {
        try {
            accountService.blockOrEnable(userId, active > 0);
            String message = active > 0 ? "Successfully enable the account." : "Successfully blocked the account.";
            return ResponseEntity.ok().body(message);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body("Account not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
