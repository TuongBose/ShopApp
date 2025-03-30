package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.CTDH;
import com.project.Shopapp.Models.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CTDHRepository extends JpaRepository<CTDH,Integer> {
    List<CTDH> findByMADONHANG(DonHang MADONHANG);
}
