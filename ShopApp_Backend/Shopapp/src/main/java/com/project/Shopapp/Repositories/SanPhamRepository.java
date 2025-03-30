package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {
    boolean existsByTENSANPHAM(String TENSANPHAM);
    Page<SanPham> findAll(Pageable pageable); // Phan trang
}
