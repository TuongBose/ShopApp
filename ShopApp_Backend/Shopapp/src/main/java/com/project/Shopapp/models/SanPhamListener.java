package com.project.Shopapp.models;

import com.project.Shopapp.services.sanphamredis.ISanPhamRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class SanPhamListener {
    private final ISanPhamRedisService sanPhamRedisService;
    private static final Logger logger = LoggerFactory.getLogger(SanPhamListener.class);

    @PrePersist
    public void prePersist(SanPham sanPham) {
        logger.info("prePersist");
    }

    @PostPersist // save = persis
    public void postPersist(SanPham sanPham) {
        logger.info("postPersist");
        sanPhamRedisService.clear();
    }

    @PreUpdate
    public void preUpdate(SanPham sanPham) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(SanPham sanPham) {
        logger.info("postUpdate");
        sanPhamRedisService.clear();
    }

    @PreRemove
    public void preRemove(SanPham sanPham) {
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(SanPham sanPham) {
        logger.info("postRemove");
        sanPhamRedisService.clear();
    }
}
