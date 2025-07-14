package com.project.Shopapp.controllers;

import com.project.Shopapp.dtos.ThuongHieuDTO;
import com.project.Shopapp.models.ThuongHieu;
import com.project.Shopapp.services.thuonghieu.ThuongHieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/thuonghieus")
@RequiredArgsConstructor
public class ThuongHieuController {
    private final ThuongHieuService thuongHieuService;

    @PostMapping("")
    public ResponseEntity<?> createThuongHieu(
            @Valid @RequestBody ThuongHieuDTO thuongHieuDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        thuongHieuService.createThuongHieu(thuongHieuDTO);
        return ResponseEntity.ok("Day la them 1 thuong hieu");
    }

    @GetMapping("")
    public ResponseEntity<List<ThuongHieu>> getAllThuongHieu(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<ThuongHieu> dsThuongHieu = thuongHieuService.getAllThuongHieu();
        return ResponseEntity.ok(dsThuongHieu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateThuongHieu(
            @PathVariable int id,
            @Valid @RequestBody ThuongHieuDTO thuongHieuDTO) {
        try {
            thuongHieuService.updateThuongHieu(id, thuongHieuDTO);
            return ResponseEntity.ok("Cap nhat thuong hieu" + id + " thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteThuongHieu(@PathVariable int id) {
        thuongHieuService.deleteThuongHieu(id);
        return ResponseEntity.ok("Xoa thuong hieu " + id + " thanh cong");
    }

}
