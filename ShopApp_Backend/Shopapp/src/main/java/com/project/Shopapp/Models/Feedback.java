package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FEEDBACK")
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int FEEDBACKID;

    @ManyToOne
    private Account USERID;

    @Column(name = "NOIDUNG", nullable = false)
    private String NOIDUNG;

    @Column(name = "SOSAO", nullable = false)
    private int SOSAO;

    @ManyToOne
    private SanPham MASANPHAM;
}
