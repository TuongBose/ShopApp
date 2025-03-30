package com.project.Shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCOUNTS")
@Builder
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int USERID;

    @Column(name = "PASSWORD", nullable = false)
    private String PASSWORD;

    private String EMAIL;
    private String FULLNAME;
    private String DIACHI;

    @Column(name = "SODIENTHOAI", nullable = false)
    private String SODIENTHOAI;

    private Date NGAYSINH;
    private boolean IS_ACTIVE;
    private int FACEBOOK_ACCOUNT_ID;
    private int GOOGLE_ACCOUNT_ID;

    private boolean ROLENAME;
}
