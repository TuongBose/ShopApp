package com.project.Shopapp.controllers;

import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.components.SecurityUtils;
import com.project.Shopapp.dtos.DonHangDTO;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.DonHang;
import com.project.Shopapp.models.OrderStatus;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.responses.donhang.DonHangListResponse;
import com.project.Shopapp.responses.donhang.DonHangResponse;
import com.project.Shopapp.services.donhang.DonHangService;
import com.project.Shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/donhangs")
@RequiredArgsConstructor
public class DonHangController {
    private final DonHangService donHangService;
    private final LocalizationUtils localizationUtils;
    private final SecurityUtils securityUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createDonHang(
            @RequestBody @Valid DonHangDTO donHangDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join("; ", errorMessage))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        Account loginAccount = securityUtils.getLoggedInUser();
        if (donHangDTO.getUSERID() != loginAccount.getUSERID()) {
            throw new Exception("You can not order as another user");
        }
        DonHangResponse donHangResponse = donHangService.createDonHang(donHangDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Insert order successfully")
                .status(HttpStatus.OK)
                .data(donHangResponse)
                .build());
    }

    @GetMapping("/account/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getDonHang_USERID(@Valid @PathVariable int id) throws Exception {
        Account loginAccount = securityUtils.getLoggedInUser();
        boolean isUserIdBlank = id <= 0;
        List<DonHang> donHangList = donHangService.getDonHangByUSERID(isUserIdBlank ? loginAccount.getUSERID() : id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of orders successfully")
                .status(HttpStatus.OK)
                .data(donHangList)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getDonHang_MADONHANG(@Valid @PathVariable int id) throws Exception {
        DonHangResponse existingDonHangResponse = donHangService.getDonHangByMADONHANG(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of orders successfully")
                .status(HttpStatus.OK)
                .data(existingDonHangResponse)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateDonHang(
            @Valid @PathVariable int id,
            @Valid @RequestBody DonHangDTO donHangDTO
    ) throws Exception {
        Account loginAccount = securityUtils.getLoggedInUser();
        if (donHangDTO.getUSERID() != loginAccount.getUSERID()) {
            if (!loginAccount.isROLENAME()) {
                throw new Exception("You can not update order as another user");
            }
        }

        DonHang donHang = donHangService.updateDonHang(id, donHangDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Update order successfully")
                .status(HttpStatus.OK)
                .data(donHang)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteDonHang(@Valid @PathVariable int id) throws Exception {
        donHangService.deleteDonHang(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_DONHANG_SUCCESSFULLY, id))
                .status(HttpStatus.OK)
                .data(null)
                .build());
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatusDonHang(
            @RequestParam(defaultValue = "", required = false) String status,
            @Valid @PathVariable int id
    ) {
        try {
            return ResponseEntity.ok(donHangService.updateStatus(status, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-alldonhang-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DonHangListResponse> getAllDonHangByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("MADONHANG").ascending()
        );
        Page<DonHangResponse> donHangResponsePage = donHangService.getAllDonHangByKeyword(keyword, pageRequest);

        int totalPages = donHangResponsePage.getTotalPages();
        List<DonHangResponse> donHangResponseList = donHangResponsePage.getContent();
        return ResponseEntity.ok(DonHangListResponse
                .builder()
                .donHangResponseList(donHangResponseList)
                .totalPages(totalPages)
                .build()
        );
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> cancelOrder(@Valid @PathVariable int id) throws Exception {
        DonHangResponse donHangResponse = donHangService.getDonHangByMADONHANG(id);

        // Kiểm tra xem người dùng hiện tại có phải là người đã đặt đơn hàng hay không
        Account loginAccount = securityUtils.getLoggedInUser();
        if (loginAccount.getUSERID() != donHangResponse.getUSERID()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("You do not have permission to cancel this order")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        if (donHangResponse.getTRANGTHAI().equals(OrderStatus.DELIVERED) ||
                donHangResponse.getTRANGTHAI().equals(OrderStatus.SHIPPED) ||
                donHangResponse.getTRANGTHAI().equals(OrderStatus.PROCESSING)) {

            String message = "You cannot cancel an order with status: " + donHangResponse.getTRANGTHAI();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(message)
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        DonHangDTO donHangDTO = DonHangDTO.builder()
                .USERID(donHangResponse.getUSERID())
                /*
                .email(order.getEmail())
                .note(order.getNote())
                .address(order.getAddress())
                .fullName(order.getFullName())
                .totalMoney(order.getTotalMoney())
                .couponCode(order.getCoupon().getCode())
                */
                .status(OrderStatus.CANCELLED)
                .build();

        DonHang donHang = donHangService.updateDonHang(id, donHangDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Cancel order successfully")
                        .status(HttpStatus.OK)
                        .data(donHang)
                        .build());
    }
}
