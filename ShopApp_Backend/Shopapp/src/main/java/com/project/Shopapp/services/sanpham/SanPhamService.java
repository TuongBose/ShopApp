package com.project.Shopapp.services.sanpham;

import com.github.javafaker.Faker;
import com.project.Shopapp.dtos.HinhAnhDTO;
import com.project.Shopapp.dtos.SanPhamDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.*;
import com.project.Shopapp.repositories.*;
import com.project.Shopapp.responses.sanpham.SanPhamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamService implements ISanPhamService {
    private final SanPhamRepository sanPhamRepository;
    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final AccountRepository accountRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public SanPham createSanPham(SanPhamDTO sanPhamDTO) {
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM"));

        ThuongHieu existingThuongHieu = thuongHieuRepository
                .findById(sanPhamDTO.getMATHUONGHIEU())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MATHUONGHIEU"));

        SanPham newSanPham = SanPham.builder()
                .TENSANPHAM(sanPhamDTO.getTENSANPHAM())
                .GIA(sanPhamDTO.getGIA())
                .MALOAISANPHAM(existingLoaiSanPham)
                .MATHUONGHIEU(existingThuongHieu)
                .MOTA(sanPhamDTO.getMOTA())
                .SOLUONGTONKHO(sanPhamDTO.getSOLUONGTONKHO())
                .THUMBNAIL(sanPhamDTO.getTHUMBNAIL())
                .build();
        return sanPhamRepository.save(newSanPham);
    }

    @Override
    public SanPham getSanPhamByMASANPHAM(int id) {
        return sanPhamRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay MASANPHAM nay"));
    }

    @Override
    public Page<SanPhamResponse> getAllSanPham(
            String keyword,
            int MALOAISANPHAM,
            PageRequest pageRequest
    ) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        Page<SanPham> sanPhamPage = sanPhamRepository.searchSanPhams(MALOAISANPHAM, keyword, pageRequest);

        // Chuyển SanPhamModel sang SanPhamResponse
        return sanPhamPage.map(SanPhamResponse::fromSanPham);
    }

    @Override
    @Transactional
    public SanPham updateSanPham(int id, SanPhamDTO sanPhamDTO) {
        SanPham existingSanPham = sanPhamRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay MASANPHAM nay"));

        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM nay!!!"));

        ThuongHieu existingThuongHieu = thuongHieuRepository
                .findById(sanPhamDTO.getMATHUONGHIEU())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MATHUONGHIEU"));

        if (sanPhamDTO.getTENSANPHAM() != null && !sanPhamDTO.getTENSANPHAM().isEmpty()) {
            existingSanPham.setTENSANPHAM(sanPhamDTO.getTENSANPHAM());
        }

        existingSanPham.setMALOAISANPHAM(existingLoaiSanPham);
        existingSanPham.setMATHUONGHIEU(existingThuongHieu);

        if (sanPhamDTO.getSOLUONGTONKHO() >= 0) {
            existingSanPham.setSOLUONGTONKHO(sanPhamDTO.getSOLUONGTONKHO());
        }

        if (sanPhamDTO.getGIA() != null && sanPhamDTO.getGIA().compareTo(BigDecimal.ZERO) >= 0) {
            existingSanPham.setGIA(sanPhamDTO.getGIA());
        }

        if (sanPhamDTO.getMOTA() != null && !sanPhamDTO.getMOTA().isEmpty()) {
            existingSanPham.setMOTA(sanPhamDTO.getMOTA());
        }

        if (sanPhamDTO.getTHUMBNAIL() != null && !sanPhamDTO.getTHUMBNAIL().isEmpty()) {
            existingSanPham.setTHUMBNAIL(sanPhamDTO.getTHUMBNAIL());
        }

        return sanPhamRepository.save(existingSanPham);
    }

    @Override
    public void deleteSanPham(int id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public boolean existsByTENSANPHAM(String TENSANPHAM) {
        return sanPhamRepository.existsByTENSANPHAM(TENSANPHAM);
    }

    @Override
    public HinhAnh createHinhAnh(HinhAnhDTO hinhAnhDTO) {
        SanPham existingSanPham = getSanPhamByMASANPHAM(hinhAnhDTO.getMASANPHAM());
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(hinhAnhDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Khong tim thay MALOAISANPHAM nay!!!"));

        HinhAnh newHinhAnh = HinhAnh.builder()
                .TENHINHANH(hinhAnhDTO.getTENHINHANH())
                .MASANPHAM(existingSanPham)
                .MALOAISANPHAM(existingLoaiSanPham)
                .build();

        // Không cho thêm quá 5 ảnh cho 1 sản phẩm
        int size = hinhAnhRepository.findByMASANPHAMAndMALOAISANPHAM(existingSanPham, existingLoaiSanPham).size();
        if (size >= HinhAnh.MAXIMUM_IMAGES_PER_PRODUCT)
            throw new RuntimeException("So luong hinh anh cua san pham <= 5");

        return hinhAnhRepository.save(newHinhAnh);
    }

    @Override
    public List<SanPham> findSanPhamByMASANPHAMList(List<Integer> MASANPHAM) {
        return sanPhamRepository.findSanPhamByMASANPHAMs(MASANPHAM);
    }

    @Override
    @Transactional
    public SanPham likeProduct(int userId, int productId) throws Exception {
        // Check if the user and product exist
        if (!accountRepository.existsById(userId) || !sanPhamRepository.existsById(productId)) {
            throw new DataNotFoundException("User or product not found");
        }

        // Check if the user has already liked the product
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            //throw new DataNotFoundException("Product already liked by the user");
        } else {
            // Create a new favorite entry and save it
            Favorite favorite = Favorite.builder()
                    .product(sanPhamRepository.findById(productId).orElse(null))
                    .user(accountRepository.findById(userId).orElse(null))
                    .build();
            favoriteRepository.save(favorite);
        }
        // Return the liked product
        return sanPhamRepository.findById(productId).orElse(null);
    }

    @Override
    @Transactional
    public SanPham unlikeProduct(int userId, int productId) throws Exception {
        // Check if the user and product exist
        if (!accountRepository.existsById(userId) || !sanPhamRepository.existsById(productId)) {
            throw new DataNotFoundException("User or product not found");
        }

        // Check if the user has already liked the product
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId);
            favoriteRepository.delete(favorite);
        }
        return sanPhamRepository.findById(productId).orElse(null);
    }

    @Override
    @Transactional
    public List<SanPhamResponse> findFavoriteProductsByUserId(int userId) throws Exception {
        // Validate the userId
        Optional<Account> optionalUser = accountRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found with ID: " + userId);
        }
        // Retrieve favorite products for the given userId
        List<SanPham> favoriteProducts = sanPhamRepository.findFavoriteProductsByUserId(userId);
        // Convert Product entities to ProductResponse objects
        return favoriteProducts.stream()
                .map(SanPhamResponse::fromSanPham)
                .collect(Collectors.toList());
    }
    @Override
    //@Transactional
    public void generateFakeLikes() throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Get all users with roleId = 1
        List<Account> accounts = accountRepository.findAllByROLENAMEFalse();
        // Get all products
        List<SanPham> sanPhams = sanPhamRepository.findAll();
        final int totalRecords = 1000;
        final int batchSize = 100;
        List<Favorite> favorites = new ArrayList<>();
        for (int i = 0; i < totalRecords; i++) {
            // Select a random user and product
            Account account = accounts.get(random.nextInt(accounts.size()));
            SanPham sanPham = sanPhams.get(random.nextInt(sanPhams.size()));

            if(!favoriteRepository.existsByUserIdAndProductId(account.getUSERID(), sanPham.getMASANPHAM())) {
                // Generate a fake favorite
                Favorite favorite = Favorite.builder()
                        .user(account)
                        .product(sanPham)
                        .build();
                favorites.add(favorite);
            }
            if(favorites.size() >= batchSize) {
                favoriteRepository.saveAll(favorites);
                favorites.clear();
            }
        }
    }
}
