package com.project.Shopapp.Models;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    private LocalDateTime NGAYTAO;
    private LocalDateTime CHINHSUA;

    @PrePersist
    protected void onCreate() {
        NGAYTAO = LocalDateTime.now();
        CHINHSUA = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        CHINHSUA = LocalDateTime.now();
    }
}
