package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {
    boolean existsByTENSANPHAM(String TENSANPHAM);
    Page<SanPham> findAll(Pageable pageable); // Phan trang

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
}
