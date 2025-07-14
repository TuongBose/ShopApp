package com.project.Shopapp.controllers;

import java.nio.file.*;

import com.github.javafaker.Faker;
import com.project.Shopapp.components.LocalizationUtils;
import com.project.Shopapp.dtos.HinhAnhDTO;
import com.project.Shopapp.dtos.SanPhamDTO;
import com.project.Shopapp.models.HinhAnh;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.responses.HinhAnhResponse;
import com.project.Shopapp.responses.SanPhamListResponse;
import com.project.Shopapp.responses.SanPhamResponse;
import com.project.Shopapp.services.hinhanh.HinhAnhService;
import com.project.Shopapp.services.sanphamredis.ISanPhamRedisService;
import com.project.Shopapp.services.sanpham.SanPhamService;
import com.project.Shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/sanphams")
@RequiredArgsConstructor
public class SanPhamController {
    private final SanPhamService sanPhamService;
    private final HinhAnhService hinhAnhService;
    private final LocalizationUtils localizationUtils;
    private final ISanPhamRedisService sanPhamRedisService;
    private static final Logger logger = LoggerFactory.getLogger(SanPhamController.class);


    // Create thông tin sản phẩm
    @PostMapping("")
    public ResponseEntity<?> createSanPham(
            @Valid @RequestBody SanPhamDTO sanphamDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            SanPham newSanpham = sanPhamService.createSanPham(sanphamDTO);
            return ResponseEntity.ok("Them 1 san pham thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Upload ảnh
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable int id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            SanPham existingSanPham = sanPhamService.getSanPhamByMASANPHAM(id);

            if (files == null) {
                files = new ArrayList<MultipartFile>();
            }
            if (files.size() > HinhAnh.MAXIMUM_IMAGES_PER_PRODUCT)
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_5));
            List<HinhAnhDTO> hinhAnhDTOS = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) continue;

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
                }

                // Lưu file
                String filename = storeFile(file);

                // Lưu vào hình ảnh vào bảng HINHANH trong DataBase
                HinhAnhDTO newHinhAnhDTO = HinhAnhDTO.builder()
                        .MALOAISANPHAM(existingSanPham.getMALOAISANPHAM().getMALOAISANPHAM())
                        .MASANPHAM(existingSanPham.getMASANPHAM())
                        .TENHINHANH(filename)
                        .build();
                sanPhamService.createHinhAnh(newHinhAnhDTO);
                hinhAnhDTOS.add(newHinhAnhDTO);
            }
            return ResponseEntity.ok().body(hinhAnhDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) && file.getOriginalFilename() != null) throw new RuntimeException("Khong phai file anh");

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    // Hàm kiểm tra xem upload file ảnh có phải là file ảnh không
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // Fake sản phẩm
    @PostMapping("/generateFakeSanPhams")
    public ResponseEntity<String> generateFakeSanPhams() {
        Faker faker = new Faker();
        for (int i = 0; i < 5700; i++) {
            String tenSanPham = faker.commerce().productName();
            if (sanPhamService.existsByTENSANPHAM(tenSanPham)) continue;

            SanPhamDTO newSanPhamDTO = SanPhamDTO
                    .builder()
                    .TENSANPHAM(tenSanPham)
                    .GIA(faker.number().numberBetween(10, 90000000))
                    .MATHUONGHIEU(faker.number().numberBetween(1, 5))
                    .MOTA(faker.lorem().sentence())
                    .SOLUONGTONKHO(faker.number().numberBetween(1, 10000))
                    .MALOAISANPHAM(faker.number().numberBetween(1, 5))
                    .build();

            try {
                sanPhamService.createSanPham(newSanPhamDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake SanPhams thanh cong!!!");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllSanPham(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "MALOAISANPHAM") int MALOAISANPHAM,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int tongSoTrang = 0;
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("NGAYTAO").descending()
                Sort.by("MASANPHAM").ascending()
        );
        logger.info(String.format("keyword = %s, MALOAISANPHAM = %d, page = %d, limit = %d",
                keyword, MALOAISANPHAM, page, limit));

        try {
            List<SanPhamResponse> sanPhamResponseList = sanPhamRedisService.getAllSanPham(keyword, MALOAISANPHAM, pageRequest);
            if (sanPhamResponseList == null || sanPhamResponseList.isEmpty()) {
                Page<SanPhamResponse> sanPhamResponses = sanPhamService.getAllSanPham(keyword, MALOAISANPHAM, pageRequest);

                // Lấy tổng số trang
                tongSoTrang = sanPhamResponses.getTotalPages();
                sanPhamResponseList = sanPhamResponses.getContent();
                sanPhamRedisService.saveAllSanPham(sanPhamResponseList, keyword, MALOAISANPHAM, pageRequest);
            }
            return ResponseEntity.ok(SanPhamListResponse
                    .builder()
                    .sanPhamResponseList(sanPhamResponseList)
                    .tongSoTrang(tongSoTrang)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSanPham(@PathVariable int id) {
        try {
            SanPham existingSanPham = sanPhamService.getSanPhamByMASANPHAM(id);
            List<HinhAnhResponse> hinhAnhList = hinhAnhService.getAllHinhAnhByMaSanPham(existingSanPham);
            return ResponseEntity.ok(SanPhamResponse.fromSanPhamForDetail(existingSanPham, hinhAnhList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSanPham(
            @PathVariable int id,
            @Valid @RequestBody SanPhamDTO sanPhamDTO
    ) {
        try {
            sanPhamService.updateSanPham(id, sanPhamDTO);
            return ResponseEntity.ok("Cap nhat san pham " + id + " thanh cong");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSanPham(@PathVariable int id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.ok("Xoa san pham " + id + " thanh cong");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getSanPhamByMASANPHAM(@RequestParam("ids") String ids) {
        // eg: 1,3,4,5,7,9
        try {
            // Tách chuỗi ids thành một mảng các số nguyên
            List<Integer> maSanPhamList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<SanPham> sanPhamList = sanPhamService.findSanPhamByMASANPHAMList(maSanPhamList);
            return ResponseEntity.ok(sanPhamList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
