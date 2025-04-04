package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.CTDH_DTO;
import com.project.Shopapp.Models.CTDH;
import com.project.Shopapp.Responses.CTDHResponse;
import com.project.Shopapp.Services.CTDHService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/chitietdonhangs")
@RequiredArgsConstructor
public class CTDHController {
    private final CTDHService ctdhService;

    @PostMapping("")
    public ResponseEntity<?> createCTDH(@Valid @RequestBody CTDH_DTO ctdh_dto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body("Them khong thanh cong");
            }
            CTDH newCTDH = ctdhService.createCTDH(ctdh_dto);
            CTDHResponse newCTDHResponse= CTDHResponse.fromCTDH(newCTDH);
            return ResponseEntity.ok(newCTDHResponse);
        } catch (
                Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCTDH(@Valid @PathVariable int id) {
        try {
            CTDH newCTDH = ctdhService.getCTDHByID(id);
            CTDHResponse newCTDHResponse= CTDHResponse.fromCTDH(newCTDH);
            return ResponseEntity.ok(newCTDHResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi lay CTDH khong thanh cong");
        }
    }

    @GetMapping("/donhang/{id}")
    public ResponseEntity<?> getCTDHs(@Valid @PathVariable int id) {
        try {
            List<CTDH> ctdhList = ctdhService.getCTDHByMADONHANG(id);
            List<CTDHResponse> ctdhResponseList = ctdhList.stream().map(CTDHResponse::fromCTDH).toList();
            return ResponseEntity.ok(ctdhResponseList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi lay CTDH trong don hang khong thanh cong");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCTDH(@Valid @PathVariable int id, @RequestBody CTDH_DTO ctdh_dto) {
        try {
            CTDH ctdh = ctdhService.updateCTDH(id,ctdh_dto);
            CTDHResponse ctdhResponse = CTDHResponse.fromCTDH(ctdh);
            return ResponseEntity.ok(ctdhResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Loi cap nhat CTDH khong thanh cong");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCTDH(@Valid @PathVariable int id) {
        try {
            ctdhService.deleteCTDH(id);
            return ResponseEntity.ok("Xoa CTDH thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Xoa CTDH khong thanh cong");
        }
    }
}
