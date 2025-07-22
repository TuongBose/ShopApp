package com.project.Shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedback")
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int FEEDBACKID;

    @ManyToOne
    @JoinColumn(name = "USERID")
    private Account USERID;

    @Column(name = "NOIDUNG", nullable = false)
    private String NOIDUNG;

    @Column(name = "SOSAO", nullable = false)
    private int SOSAO;

    @ManyToOne
    @JoinColumn(name = "MASANPHAM")
    private SanPham MASANPHAM;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
