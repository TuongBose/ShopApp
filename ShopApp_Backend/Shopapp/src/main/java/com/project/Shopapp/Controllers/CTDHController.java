package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.CTDH_DTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/chitietdonhangs")
public class CTDHController {
    @PostMapping("")
    public ResponseEntity<?> createCTDH(@Valid @RequestBody CTDH_DTO ctdh_dto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body("Them khong thanh cong");
            }
            return ResponseEntity.ok("Tao thanh cong");
        } catch (
                Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCTDH(@Valid @PathVariable("id") int id) {
        try {
            return ResponseEntity.ok("Lay CTDH thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi lay CTDH khong thanh cong");
        }
    }

    @GetMapping("/donhang/{donhangId}")
    public ResponseEntity<?> getCTDHs(@Valid @PathVariable("donhangId") int donhangId) {
        try {
            return ResponseEntity.ok("Lay CTDH trong don hang thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi lay CTDH trong don hang khong thanh cong");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCTDH(@Valid @PathVariable int id, @RequestBody CTDH_DTO ctdh_dto) {
        try {
            return ResponseEntity.ok("Cap nhat CTDH thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi cap nhat CTDH khong thanh cong");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCTDH(@Valid @PathVariable int id) {
        try {
            return ResponseEntity.ok("Xoa CTDH thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Xoa CTDH khong thanh cong");
        }
    }
}
