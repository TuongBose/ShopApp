package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Feedback;
import com.project.Shopapp.models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByUSERID(Account account);
    List<Feedback> findByMASANPHAM (SanPham sanPham);
    List<Feedback> findByUSERIDAndMASANPHAM(Account account, SanPham sanPham);
}
