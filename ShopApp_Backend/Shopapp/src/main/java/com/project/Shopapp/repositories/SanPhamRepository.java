package com.project.Shopapp.repositories;

import com.project.Shopapp.models.LoaiSanPham;
import com.project.Shopapp.models.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {
    boolean existsByTENSANPHAM(String TENSANPHAM);
    Page<SanPham> findAll(Pageable pageable); // Phan trang
    List<SanPham> findByMALOAISANPHAM(LoaiSanPham loaiSanPham);

    @Query(
            "SELECT p FROM SanPham p WHERE " +
                    "(:MALOAISANPHAM IS NULL OR :MALOAISANPHAM = 0 OR p.MALOAISANPHAM.MALOAISANPHAM = :MALOAISANPHAM) " +
                    "AND (:keyword IS NULL OR :keyword = '' OR p.TENSANPHAM LIKE CONCAT('%', :keyword, '%') OR p.MOTA LIKE CONCAT('%', :keyword, '%'))"
    )
    Page<SanPham> searchSanPhams(
                    @Param("MALOAISANPHAM") int MALOAISANPHAM,
                    @Param("keyword") String keyword,
                    Pageable pageable
    );

    @Query("SELECT p FROM SanPham p WHERE p.MASANPHAM IN :MASANPHAM")
    List<SanPham> findSanPhamByMASANPHAMs(@Param("MASANPHAM") List<Integer> MASANPHAM);

    @Query("SELECT p FROM SanPham p JOIN p.favorites f WHERE f.user.USERID = :userId")
    List<SanPham> findFavoriteProductsByUserId(@Param("userId") int userId);


}
