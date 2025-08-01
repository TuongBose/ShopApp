package com.project.Shopapp.controllers;

import com.project.Shopapp.components.SecurityUtils;
import com.project.Shopapp.dtos.AccountDTO;
import com.project.Shopapp.dtos.AccountLoginDTO;
import com.project.Shopapp.dtos.RefreshTokenDTO;
import com.project.Shopapp.dtos.UpdateAccountDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.InvalidPasswordException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.responses.account.AccountListResponse;
import com.project.Shopapp.responses.account.AccountResponse;
import com.project.Shopapp.responses.account.LoginResponse;
import com.project.Shopapp.responses.account.RegisterResponse;
import com.project.Shopapp.services.account.AccountService;
import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.services.account.IAccountService;
import com.project.Shopapp.services.auth.IAuthService;
import com.project.Shopapp.services.token.TokenService;
import com.project.Shopapp.utils.FileUtils;
import com.project.Shopapp.utils.MessageKeys;
import com.project.Shopapp.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
    private final IAuthService authService;
    private final LocalizationUtils localizationUtils;
    private final TokenService tokenService;
    private final SecurityUtils securityUtils;

    private boolean isMobileDevice(String accountAgent) {
        // Kiem tra User-Agent header de xac dinh thiet bi di dong
        // Vi du don gian:
        return accountAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createAccount(
            @Valid @RequestBody AccountDTO accountDTO,
            BindingResult result
    ) throws Exception {
        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            registerResponse.setMessage(String.join("; ", errorMessages));
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join("; ", errorMessages))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(registerResponse)
                    .build());
        }

        if (accountDTO.getEMAIL() == null || accountDTO.getEMAIL().trim().isBlank()) {
            if (accountDTO.getSODIENTHOAI() == null || accountDTO.getSODIENTHOAI().isBlank()) {
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                        .message("At least email or phone number is required")
                        .status(HttpStatus.BAD_REQUEST)
                        .data(null)
                        .build());
            } else {
                if (!ValidationUtils.isValidPhoneNumber(accountDTO.getSODIENTHOAI())) {
                    throw new Exception("Invalid phone number");
                }
            }
        } else {
            // Email not blank
            if (!ValidationUtils.isValidEmail(accountDTO.getEMAIL())) {
                throw new Exception("Invalid email format");
            }
        }

        if (!accountDTO.getPASSWORD().equals(accountDTO.getRETYPEPASSWORD())) {
            registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(registerResponse.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(registerResponse)
                    .build());
        }

        Account newAccount = accountService.createAccount(accountDTO);
        registerResponse.setMessage("Signing up successfully");
        registerResponse.setAccount(AccountResponse.fromAccount(newAccount));
        return ResponseEntity.ok(ResponseObject.builder()
                .message(registerResponse.getMessage())
                .status(HttpStatus.CREATED)
                .data(registerResponse)
                .build());
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createUserAdmin(
            @Valid @RequestBody AccountDTO accountDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(errorMessages.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        if (accountDTO.getEMAIL() == null || accountDTO.getEMAIL().trim().isBlank()) {
            if (accountDTO.getSODIENTHOAI() == null || accountDTO.getSODIENTHOAI().isBlank()) {
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .data(null)
                        .message("At least email or phone number is required")
                        .build());
            } else {
                //phone number not blank
                if (!ValidationUtils.isValidPhoneNumber(accountDTO.getSODIENTHOAI())) {
                    throw new Exception("Invalid phone number");
                }
            }
        } else {
            //Email not blank
            if (!ValidationUtils.isValidEmail(accountDTO.getEMAIL())) {
                throw new Exception("Invalid email format");
            }
        }

        if (!accountDTO.getPASSWORD().equals(accountDTO.getRETYPEPASSWORD())) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        Account account = accountService.createUserAdmin(accountDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .data(AccountResponse.fromAccount(account))
                .message("Account registration successful")
                .build());
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @RequestBody AccountLoginDTO accountLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        if (accountLoginDTO.getEMAIL() == null || accountLoginDTO.getEMAIL().trim().isBlank()) {
            if (accountLoginDTO.getSODIENTHOAI() == null || accountLoginDTO.getSODIENTHOAI().isBlank()) {
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                        .message("At least email or phone number is required")
                        .status(HttpStatus.BAD_REQUEST)
                        .data(null)
                        .build());
            } else {
                if (!ValidationUtils.isValidPhoneNumber(accountLoginDTO.getSODIENTHOAI())) {
                    throw new Exception("Invalid phone number");
                }
            }
        } else {
            // Email not blank
            if (!ValidationUtils.isValidEmail(accountLoginDTO.getEMAIL())) {
                throw new Exception("Invalid email format");
            }
        }

        // Kiểm tra thông tin đăng nhập và sinh token
        String token = accountService.login(accountLoginDTO);
        String accountAgent = request.getHeader("User-Agent");
        Account accountDetail = accountService.getAccountDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(accountDetail, token, isMobileDevice(accountAgent));

        LoginResponse loginResponse = LoginResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(token)
                .tokenType(jwtToken.getTOKEN_TYPE())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(accountDetail.getFULLNAME())
                .role(accountDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(accountDetail.getUSERID())
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(loginResponse.getMessage())
                .status(HttpStatus.OK)
                .data(loginResponse)
                .build());
    }

    private ResponseEntity<ResponseObject> loginSocial(
            @Valid @RequestBody AccountLoginDTO accountLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = accountService.loginSocial(accountLoginDTO);

        // Xử lý token và thông tin người dùng
        String userAgent = request.getHeader("User-Agent");
        Account userDetail = accountService.getAccountDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));

        // Tạo đối tượng LoginResponse
        LoginResponse loginResponse = LoginResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(jwtToken.getTOKEN())
                .tokenType(jwtToken.getTOKEN_TYPE())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(userDetail.getFULLNAME())
                .role(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(userDetail.getUSERID())
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(loginResponse.getMessage())
                .status(HttpStatus.OK)
                .data(loginResponse)
                .build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseObject> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) throws Exception {
        Account accountDetail = accountService.getAccountDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), accountDetail);
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getTOKEN())
                .tokenType(jwtToken.getTOKEN_TYPE())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(accountDetail.getFULLNAME())
                .role(accountDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(accountDetail.getUSERID())
                .build();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(loginResponse.getMessage())
                .status(HttpStatus.OK)
                .data(loginResponse)
                .build());
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getAccountDetails(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        Account account = accountService.getAccountDetailsFromToken(extractedToken);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get user's detail successfully")
                .status(HttpStatus.OK)
                .data(AccountResponse.fromAccount(account))
                .build());
    }

    @PutMapping("/details/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateAccountDetails(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int userId,
            @RequestBody UpdateAccountDTO updateAccountDTO
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        Account account = accountService.getAccountDetailsFromToken(extractedToken);

        // Đảm bảo rằng user gọi request chứa token phải trùng với user muốn update
        if (account.getUSERID() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Account updateAccount = accountService.updateAccount(updateAccountDTO, userId);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Update user detail successfully")
                .status(HttpStatus.OK)
                .data(AccountResponse.fromAccount(updateAccount))
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getAllAccountCustomer(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws Exception {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("USERID").ascending()
        );
        Page<AccountResponse> accountPage = accountService.getAllAccountCustomer(keyword, pageRequest)
                .map(AccountResponse::fromAccount);

        // Lay tong so trang
        int totalPages = accountPage.getTotalPages();
        List<AccountResponse> accountResponses = accountPage.getContent();
        AccountListResponse accountListResponse = AccountListResponse
                .builder()
                .accountResponseList(accountResponses)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of Account customer successfully")
                .status(HttpStatus.OK)
                .data(accountListResponse)
                .build());
    }

    @PutMapping("/reset-password/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> resetPassword(@Valid @PathVariable int userId) {
        try {
            String newPassword = UUID.randomUUID().toString().substring(0, 5); // Create new password
            accountService.resetPassword(userId, newPassword);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Reset password successfully")
                    .status(HttpStatus.OK)
                    .data(newPassword)
                    .build());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Invalid password: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Account not found: " + e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/block/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> blockOrEnable(
            @Valid @PathVariable int userId,
            @Valid @PathVariable int active
    ) throws Exception {
        accountService.blockOrEnable(userId, active > 0);
        String message = active > 0 ? "Successfully enable the account." : "Successfully blocked the account.";
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(message)
                .status(HttpStatus.OK)
                .data(null)
                .build());

    }

    @PostMapping(value = "/upload-profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Account loginUser = securityUtils.getLoggedInUser();
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                            .message("Image file is required.")
                            .status(HttpStatus.NOT_FOUND)
                            .data(null)
                            .build()
            );
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(ResponseObject.builder()
                            .message("Image file size exceeds the allowed limit of 10MB.")
                            .status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .data(null)
                            .build());
        }

        // Check file type
        if (!FileUtils.isImageFile(file)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(ResponseObject.builder()
                            .message("Uploaded file must be an image.")
                            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .data(null)
                            .build());
        }

        // Store file and get filename
        String oldFileName = loginUser.getProfileImage();
        String imageName = FileUtils.storeFile(file);

        accountService.changeProfileImage(loginUser.getUSERID(), imageName);
        // Delete old file if exists
        if (!StringUtils.isEmpty(oldFileName)) {
            FileUtils.deleteFile(oldFileName);
        }
//1aba82e1-4599-4c8b-8ec5-9c16e5aad379_3734888057500.png
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Upload profile image successfully")
                .status(HttpStatus.CREATED)
                .data(imageName) // Return the filename or image URL
                .build());
    }

    @GetMapping("/profile-images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/default-profile-image.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Angular, bam dang nhap google, redirect den trang dang nhap google, dang nhap xong co "code"
    // Tu "code" => google token => lay ra cac thong tin khac
    @GetMapping("/auth/social-login")
    public ResponseEntity<String> socialAuth(
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) {
        // request.getRequestURI()
        loginType = loginType.trim().toLowerCase(); // Loai bo dau cach va chuyen thanh chu thuong
        String url = authService.generateAuthUrl(loginType);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/auth/social/callback")
    public ResponseEntity<ResponseObject> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) throws Exception {
        // Call the AuthService to get user info
        Map<String, Object> userInfo = authService.authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Failed to authenticate")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        String accountId = "";
        String name = "";
        String picture = "";
        String email = "";

        if (loginType.trim().equals("google")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
        } else if (loginType.trim().equals("facebook")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            // Lay URL anh tu cau truc du lieu cua facebook
            Object pictureObj = userInfo.get("picture");
            if (pictureObj instanceof Map) {
                Map<?, ?> pictureData = (Map<?, ?>) pictureObj;
                Object dataObj = pictureData.get("data");

                if (dataObj instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) dataObj;
                    Object urlObj = dataMap.get("url");
                    if (urlObj instanceof String) {
                        picture = (String) urlObj;
                    }
                }
            }
        }

        // Create object AccountLoginDTO
        AccountLoginDTO accountLoginDTO = AccountLoginDTO.builder()
                .EMAIL(email)
                .fullName(name)
                .PASSWORD("")
                .SODIENTHOAI("")
                .profileImage(picture)
                .build();

        if (loginType.trim().equals("google")) {
            accountLoginDTO.setGoogleAccountId(accountId);
        } else if (loginType.trim().equals("facebook")) {
            accountLoginDTO.setGoogleAccountId("");
        }

        return this.loginSocial(accountLoginDTO, request);
    }
}
