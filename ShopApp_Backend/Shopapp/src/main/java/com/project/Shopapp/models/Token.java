package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TOKENS")
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int TOKEN_ID;

    @Column(name = "TOKEN", nullable = false)
    private String TOKEN;

    @Column(name = "TOKEN_TYPE",nullable = false)
    private String TOKEN_TYPE;

    private LocalDateTime EXPIRATION_DATE;
    private boolean REVOKED;
    private boolean EXPIRED;

    @ManyToOne
    @JoinColumn(name = "USERID")
    private Account USERID;
}
