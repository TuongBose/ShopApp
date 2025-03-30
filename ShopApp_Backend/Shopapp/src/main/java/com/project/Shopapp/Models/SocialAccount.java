package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SOCIAL_ACCOUNTS")
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SOCIAL_ACCOUNT_ID;

    @Column(name = "PROVIDER", nullable = false)
    private String PROVIDER;

    @Column(name = "PROVIDER_ID", nullable = false)
    private String PROVIDER_ID;

    @Column(name = "EMAIL", nullable = false)
    private String EMAIL;

    @Column(name = "NAME", nullable = false)
    private String NAME;

    @ManyToOne
    private Account USERID;
}
