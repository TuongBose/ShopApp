package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.Account;
import com.project.Shopapp.Models.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang,Integer> {
    List<DonHang> findByUSERID(Account account);
}
