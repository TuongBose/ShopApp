package com.project.Shopapp.controllers;

import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.dtos.CTDH_DTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.responses.ctdh.CTDHResponse;
import com.project.Shopapp.services.ctdh.CTDHService;
import com.project.Shopapp.utils.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/chitietdonhangs")
@RequiredArgsConstructor
public class CTDHController {
    private final CTDHService ctdhService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createCTDH(
            @Valid @RequestBody CTDH_DTO ctdh_dto,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Create order details failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        CTDH newCTDH = ctdhService.createCTDH(ctdh_dto);
        CTDHResponse newCTDHResponse = CTDHResponse.fromCTDH(newCTDH);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Create order detail successfully")
                .status(HttpStatus.CREATED)
                .data(newCTDHResponse)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getCTDH(@Valid @PathVariable int id) {
        try {
            CTDH newCTDH = ctdhService.getCTDHByID(id);
            CTDHResponse newCTDHResponse = CTDHResponse.fromCTDH(newCTDH);
            return ResponseEntity.ok(newCTDHResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi lay CTDH khong thanh cong");
        }
    }

    @GetMapping("/donhang/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getCTDHs(@Valid @PathVariable int id) throws Exception {
        List<CTDH> ctdhList = ctdhService.getCTDHByMADONHANG(id);
        List<CTDHResponse> ctdhResponseList = ctdhList.stream().map(CTDHResponse::fromCTDH).toList();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get order details by orderId successfully")
                .status(HttpStatus.OK)
                .data(ctdhResponseList)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateCTDH(
            @Valid @PathVariable int id,
            @RequestBody CTDH_DTO ctdh_dto
    ) throws DataNotFoundException, Exception {
        CTDH ctdh = ctdhService.updateCTDH(id, ctdh_dto);
        CTDHResponse ctdhResponse = CTDHResponse.fromCTDH(ctdh);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Update order detail successfully")
                .status(HttpStatus.OK)
                .data(ctdhResponse)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> deleteCTDH(@Valid @PathVariable int id) {
        ctdhService.deleteCTDH(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CHITIETDONHANG_SUCCESSFULLY, id))
                .status(HttpStatus.OK)
                .build());
    }
}
