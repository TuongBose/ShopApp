package com.project.Shopapp.services.sanpham;

import com.github.javafaker.Faker;
import com.project.Shopapp.dtos.HinhAnhDTO;
import com.project.Shopapp.dtos.SanPhamDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.InvalidParamException;
import com.project.Shopapp.models.*;
import com.project.Shopapp.repositories.*;
import com.project.Shopapp.responses.sanpham.SanPhamResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DateTimeException;
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
    @Transactional
    public SanPham createSanPham(SanPhamDTO sanPhamDTO) throws Exception{
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new DataNotFoundException("Cannot find MALOAISANPHAM"));

        ThuongHieu existingThuongHieu = thuongHieuRepository
                .findById(sanPhamDTO.getMATHUONGHIEU())
                .orElseThrow(() -> new DateTimeException("Cannot find MATHUONGHIEU"));

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
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find MASANPHAM"));
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
                .orElseThrow(() -> new RuntimeException("Cannot find MASANPHAM"));

        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(sanPhamDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new RuntimeException("Cannot find MALOAISANPHAM"));

        ThuongHieu existingThuongHieu = thuongHieuRepository
                .findById(sanPhamDTO.getMATHUONGHIEU())
                .orElseThrow(() -> new RuntimeException("Cannot find MATHUONGHIEU"));

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
        Optional<SanPham> optionalSanPham = sanPhamRepository.findById(id);
        optionalSanPham.ifPresent(sanPhamRepository::delete);
    }

    @Override
    public boolean existsByTENSANPHAM(String TENSANPHAM) {
        return sanPhamRepository.existsByTENSANPHAM(TENSANPHAM);
    }

    @Override
    @Transactional
    public HinhAnh createHinhAnh(HinhAnhDTO hinhAnhDTO) throws Exception{
        SanPham existingSanPham = getSanPhamByMASANPHAM(hinhAnhDTO.getMASANPHAM());
        LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository
                .findById(hinhAnhDTO.getMALOAISANPHAM())
                .orElseThrow(() -> new DataNotFoundException("Cannot find MALOAISANPHAM"));

        HinhAnh newHinhAnh = HinhAnh.builder()
                .TENHINHANH(hinhAnhDTO.getTENHINHANH())
                .MASANPHAM(existingSanPham)
                .MALOAISANPHAM(existingLoaiSanPham)
                .build();

        // Không cho thêm quá 5 ảnh cho 1 sản phẩm
        int size = hinhAnhRepository.findByMASANPHAMAndMALOAISANPHAM(existingSanPham, existingLoaiSanPham).size();
        if (size >= HinhAnh.MAXIMUM_IMAGES_PER_PRODUCT)
            throw new InvalidParamException("Number of images must be <= "+HinhAnh.MAXIMUM_IMAGES_PER_PRODUCT);

        if(existingSanPham.getTHUMBNAIL()==null){
            existingSanPham.setTHUMBNAIL(newHinhAnh.getTENHINHANH());
        }
        sanPhamRepository.save(existingSanPham);
        hinhAnhRepository.save(newHinhAnh);
        return newHinhAnh;
    }

    @Override
    public List<SanPham> findSanPhamByMASANPHAMList(List<Integer> MASANPHAM) {
        return sanPhamRepository.findSanPhamByMASANPHAMs(MASANPHAM);
    }

    @Override
    @Transactional
    public SanPham likeProduct(int userId, int productId) throws Exception {
        Account existingAccount=accountRepository.findById(userId)
                .orElseThrow(()->new DataNotFoundException("Does not exist Account"));

        SanPham existingSanPham = sanPhamRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException("Does not exist SanPham"));

        // Check if the user has already liked the product
        if (favoriteRepository.existsByUserAndProduct(existingAccount, existingSanPham)) {
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
        Account existingAccount=accountRepository.findById(userId)
                .orElseThrow(()->new DataNotFoundException("Does not exist Account"));

        SanPham existingSanPham = sanPhamRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException("Does not exist SanPham"));

        // Check if the user has already liked the product
        if (favoriteRepository.existsByUserAndProduct(existingAccount, existingSanPham)) {
            Favorite favorite = favoriteRepository.findByUserAndProduct(existingAccount, existingSanPham);
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

            if(!favoriteRepository.existsByUserAndProduct(account, sanPham)) {
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
