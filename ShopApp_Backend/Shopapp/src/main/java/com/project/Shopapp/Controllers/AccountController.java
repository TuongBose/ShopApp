package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.DTOs.AccountLoginDTO;
import com.project.Shopapp.DTOs.UpdateAccountDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Responses.AccountResponse;
import com.project.Shopapp.Responses.LoginResponse;
import com.project.Shopapp.Services.AccountService;
import com.project.Shopapp.Components.LocalizationUtils;
import com.project.Shopapp.Utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final LocalizationUtils localizationUtils;

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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AccountLoginDTO accountLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = accountService.login(
                    accountLoginDTO.getSODIENTHOAI(),
                    accountLoginDTO.getPASSWORD(),
                    accountLoginDTO.isRoleid() ? Account.ADMIN : Account.USER
            );
            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
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

    @PostMapping("/details")
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
}
