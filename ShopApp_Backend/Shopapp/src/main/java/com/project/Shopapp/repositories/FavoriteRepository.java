package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    boolean existsByUserIdAndProductId(int userId, int productId);
    Favorite findByUserIdAndProductId(int userId, int productId);
}
