package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCOUNTS")
@Builder
public class Account extends BaseEntity implements UserDetails {
    public static final int USER = 0;
    public static final int ADMIN = 1;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String name = "USER";
        if(ROLENAME)
            name = "ADMIN";

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+ name));
        //authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.PASSWORD;
    }

    @Override
    public String getUsername() {
        return SODIENTHOAI;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
