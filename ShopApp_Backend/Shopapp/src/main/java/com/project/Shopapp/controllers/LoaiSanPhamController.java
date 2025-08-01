package com.project.Shopapp.controllers;

import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.components.converters.CategoryMessageConverter;
import com.project.Shopapp.dtos.LoaiSanPhamDTO;
import com.project.Shopapp.models.LoaiSanPham;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.services.loaisanpham.LoaiSanPhamService;
import com.project.Shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final LocalizationUtils localizationUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createLoaiSanPham(
            @Valid @RequestBody LoaiSanPhamDTO loaisanphamDTO,
            BindingResult result) throws Exception{
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join("; ", errorMessage))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        LoaiSanPham loaiSanPham = loaiSanPhamService.createLoaiSanPham(loaisanphamDTO);

        this.kafkaTemplate.send("insert-a-category",loaiSanPham); // producer
        this.kafkaTemplate.setMessageConverter(new CategoryMessageConverter());

        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_LOAISANPHAM_SUCCESSFULLY))
                .status(HttpStatus.CREATED)
                .data(loaiSanPham)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllLoaiSanPham(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<LoaiSanPham> dsLoaiSanPham = loaiSanPhamService.getAllLoaiSanPham();
        this.kafkaTemplate.send("get-all-categories", dsLoaiSanPham);

        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of categories successfully")
                .status(HttpStatus.OK)
                .data(dsLoaiSanPham)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateLoaiSanPham(
            @PathVariable int id,
            @Valid @RequestBody LoaiSanPhamDTO loaiSanPhamDTO
    ) throws Exception{
        LoaiSanPham loaiSanPham = loaiSanPhamService.updateLoaiSanPham(id, loaiSanPhamDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_LOAISANPHAM_SUCCESSFULLY))
                .status(HttpStatus.OK)
                .data(loaiSanPham)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteLoaiSanPham(@PathVariable int id) throws Exception {
        loaiSanPhamService.deleteLoaiSanPham(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Delete LoaiSanPham successfully")
                .build());

        //return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_LOAISANPHAM_SUCCESSFULLY, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getLoaiSanPhamById(
            @PathVariable int id
    ) throws Exception{
        LoaiSanPham existingLoaiSanPham = loaiSanPhamService.getLoaiSanPhamByMALOAISANPHAM(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(existingLoaiSanPham)
                .message("Get Loai San Pham information successfully")
                .status(HttpStatus.OK)
                .build());
    }
}
