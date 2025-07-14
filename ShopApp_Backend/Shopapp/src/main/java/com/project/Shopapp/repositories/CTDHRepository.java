package com.project.Shopapp.repositories;

import com.project.Shopapp.models.CTDH;
import com.project.Shopapp.models.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CTDHRepository extends JpaRepository<CTDH,Integer> {
    List<CTDH> findByMADONHANG(DonHang MADONHANG);
}
