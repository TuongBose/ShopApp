package com.project.Shopapp.Controllers;

import java.nio.file.*;

import com.project.Shopapp.DTOs.HinhAnhDTO;
import com.project.Shopapp.DTOs.SanPhamDTO;
import com.project.Shopapp.Models.SanPham;
import com.project.Shopapp.Services.SanPhamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/sanphams")
@RequiredArgsConstructor
public class SanPhamController {
    private final SanPhamService sanPhamService;

    @GetMapping("")
    public ResponseEntity<String> getAll_SanPham(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("Day la danh sach san pham, page: %d, limit: %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOne_SanPham(@PathVariable("id") String sanpham) {
        return ResponseEntity.ok("Day la chi tiet san pham" + sanpham);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createSanPham(
            @Valid @ModelAttribute SanPhamDTO sanphamDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            SanPham newSanpham = sanPhamService.createSanPham(sanphamDTO);

            List<MultipartFile> files = sanphamDTO.getFiles();
            if (files == null) {
                files = new ArrayList<MultipartFile>();
            }
            for (MultipartFile file : files) {
                if (file.getSize() == 0) continue;

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File qua lon");
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File khong dung dinh dang");
                }

                // Lưu file
                String filename = storeFile(file);

                // Lưu vào hình ảnh vào bảng HINHANH trong DataBase
                HinhAnhDTO newHinhAnhDTO = HinhAnhDTO.builder()
                        .MALOAISANPHAM(sanphamDTO.getMALOAISANPHAM())
                        .MASANPHAM(newSanpham.getMASANPHAM())
                        .TENHINHANH(filename)
                        .build();
                sanPhamService.createHinhAnh(newHinhAnhDTO);
            }

            return ResponseEntity.ok("Them 1 san pham thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update_SanPham(@PathVariable Long id) {
        return ResponseEntity.ok("Day la cap nhat san pham");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete_SanPham(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body("Day la xoa");
    }

}
