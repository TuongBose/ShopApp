package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Favorite;
import com.project.Shopapp.models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    boolean existsByUserAndProduct(Account account, SanPham sanPham);
    Favorite findByUserAndProduct(Account account, SanPham sanPham);
}
