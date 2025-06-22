package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.LoaiSanPhamDTO;
import com.project.Shopapp.Models.LoaiSanPham;
import com.project.Shopapp.Responses.UpdateLoaiSanPhamResponse;
import com.project.Shopapp.Services.LoaiSanPhamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/loaisanphams")
// Dependency Injection
@RequiredArgsConstructor
public class LoaiSanPhamController {
    private final LoaiSanPhamService loaiSanPhamService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @PostMapping("")
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
        return ResponseEntity.ok("Day la them LOAI san pham");
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
                            .message(messageSource.getMessage("loaisanpham.update_loaisanpham.update_successfully",null,locale))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoaiSanPham(@PathVariable int id) {
        loaiSanPhamService.deleteLoaiSanPham(id);
        return ResponseEntity.ok("Xoa loai san pham " + id + "thanh cong");
    }

}
