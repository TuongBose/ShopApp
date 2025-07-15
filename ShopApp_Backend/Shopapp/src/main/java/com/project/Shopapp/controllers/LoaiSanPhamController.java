package com.project.Shopapp.controllers;

import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.dtos.LoaiSanPhamDTO;
import com.project.Shopapp.models.LoaiSanPham;
import com.project.Shopapp.responses.UpdateLoaiSanPhamResponse;
import com.project.Shopapp.services.loaisanpham.LoaiSanPhamService;
import com.project.Shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/loaisanphams")
// Dependency Injection
@RequiredArgsConstructor
public class LoaiSanPhamController {
    private final LoaiSanPhamService loaiSanPhamService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    @PreAuthorize("hasRole(ROLE_ADMIN)")
    public ResponseEntity<?> createLoaiSanPham(
            @Valid @RequestBody LoaiSanPhamDTO loaisanphamDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        loaiSanPhamService.createLoaiSanPham(loaisanphamDTO);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_LOAISANPHAM_SUCCESSFULLY));
    }

    @GetMapping("")
    public ResponseEntity<List<LoaiSanPham>> getAllLoaiSanPham(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<LoaiSanPham> dsLoaiSanPham = loaiSanPhamService.getAllLoaiSanPham();
        return ResponseEntity.ok(dsLoaiSanPham);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateLoaiSanPhamResponse> updateLoaiSanPham(
            @PathVariable int id,
            @Valid @RequestBody LoaiSanPhamDTO loaiSanPhamDTO,
            HttpServletRequest request) {
        try {
            loaiSanPhamService.updateLoaiSanPham(id, loaiSanPhamDTO);
            Locale locale = localeResolver.resolveLocale(request);
            return ResponseEntity.ok(UpdateLoaiSanPhamResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_LOAISANPHAM_SUCCESSFULLY))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    UpdateLoaiSanPhamResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoaiSanPham(@PathVariable int id) {
        loaiSanPhamService.deleteLoaiSanPham(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_LOAISANPHAM_SUCCESSFULLY,id));
    }

}
