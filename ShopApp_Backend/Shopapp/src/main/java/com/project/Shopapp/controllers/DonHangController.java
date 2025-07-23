package com.project.Shopapp.controllers;

import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.dtos.DonHangDTO;
import com.project.Shopapp.models.DonHang;
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

    @PostMapping("")
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
        List<DonHang> donHangList = donHangService.getDonHangByUSERID(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of orders successfully")
                .status(HttpStatus.OK)
                .data(donHangList)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDonHang_MADONHANG(@Valid @PathVariable int id) throws Exception{
        DonHangResponse existingDonHangResponse = donHangService.getDonHangByMADONHANG(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of orders successfully")
                .status(HttpStatus.OK)
                .data(existingDonHangResponse)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonHang(@Valid @PathVariable int id, @Valid @RequestBody DonHangDTO donHangDTO) {
        try {
            return ResponseEntity.ok(donHangService.updateDonHang(id, donHangDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonHang(@Valid @PathVariable int id) {
        try {
            donHangService.deleteDonHang(id);
            return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_DONHANG_SUCCESSFULLY, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
}
