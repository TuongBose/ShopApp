package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.DTOs.AccountLoginDTO;
import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Responses.LoginResponse;
import com.project.Shopapp.Services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDTO accountDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!accountDTO.getPASSWORD().equals(accountDTO.getRETYPEPASSWORD()))
                return ResponseEntity.badRequest().body("Password khong trung");

            Account newAccount = accountService.createAccount(accountDTO);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = accountService.login(accountLoginDTO.getSODIENTHOAI(), accountLoginDTO.getPASSWORD());
            Locale locale = localeResolver.resolveLocale(request);
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(messageSource.getMessage("account.login.login_successfully", null, locale))
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
