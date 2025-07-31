package com.project.Shopapp.controllers;

import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.services.hinhanh.IHinhAnhService;
import com.project.Shopapp.services.sanpham.ISanPhamService;
import com.project.Shopapp.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/product_images")
@RequiredArgsConstructor
public class HinhAnhSanPhamController {
    private final IHinhAnhService hinhAnhService;
    private final ISanPhamService sanPhamService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> delete(@PathVariable int id) throws DataNotFoundException, Exception {
        HinhAnh hinhAnh = hinhAnhService.deleteHinhAnh(id);
        if (hinhAnh != null) {
            FileUtils.deleteFile(hinhAnh.getTENHINHANH());
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Delete product image successfully")
                .status(HttpStatus.OK)
                .data(hinhAnh)
                .build());
    }
}
