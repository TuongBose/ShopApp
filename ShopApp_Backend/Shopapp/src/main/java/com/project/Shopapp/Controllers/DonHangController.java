package com.project.Shopapp.Controllers;

import com.project.Shopapp.DTOs.DonHangDTO;
import com.project.Shopapp.Responses.DonHangResponse;
import com.project.Shopapp.Services.DonHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.naming.Binding;

@RestController
@RequestMapping("${api.prefix}/donhangs")
@RequiredArgsConstructor
public class DonHangController {
    private final DonHangService donHangService;

    @PostMapping("")
    public ResponseEntity<?> createDonHang(@RequestBody @Valid DonHangDTO donHangDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body("Them khong thanh cong");
            }
            DonHangResponse donHangResponse = donHangService.createDonHang(donHangDTO);
            return ResponseEntity.ok(donHangResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{USERID}")
    public ResponseEntity<?> getDonHang(@Valid @PathVariable("USERID") int userid) {
        try {
            return ResponseEntity.ok("Lay don hang thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonHang(@Valid @PathVariable int id, @Valid @RequestBody DonHangDTO donHangDTO) {
        try {
            return ResponseEntity.ok("Cap nhat thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cap nhat bi loi");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonHang(@Valid @PathVariable int id) {
        try {
            return ResponseEntity.ok("Xoa don hang thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Khong thanh cong!!!");
        }
    }

}
