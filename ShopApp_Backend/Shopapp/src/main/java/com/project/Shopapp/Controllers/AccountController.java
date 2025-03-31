package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.AccountDTO;
import com.project.Shopapp.DTOs.AccountLoginDTO;
import com.project.Shopapp.Services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDTO accountDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!accountDTO.getPASSWORD().equals(accountDTO.getRETYPEPASSWORD()))
                return ResponseEntity.badRequest().body("Password khong trung");

            accountService.createAccount(accountDTO);
            return ResponseEntity.ok("Dang ky thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AccountLoginDTO accountLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        String token = accountService.login(accountLoginDTO.getSODIENTHOAI(), accountLoginDTO.getPASSWORD());
        return ResponseEntity.ok("some token");
    }
}
