package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Models.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    List<DonHang> findByUSERID(Account account);

    @Query("SELECT o FROM DonHang o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR o.FULLNAME LIKE %:keyword% OR o.DIACHI LIKE %:keyword% " +
            "OR o.GHICHU LIKE %:keyword% OR o.EMAIL LIKE %:keyword%)")
    Page<DonHang> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
