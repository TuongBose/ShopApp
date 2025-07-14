package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "thuonghieu")
@Builder
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MATHUONGHIEU;

    private String TENTHUONGHIEU;
}
