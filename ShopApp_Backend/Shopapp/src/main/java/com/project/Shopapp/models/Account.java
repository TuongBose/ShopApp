package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@Builder
public class Account extends BaseEntity implements UserDetails, OAuth2User {
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
    private String FACEBOOK_ACCOUNT_ID;
    private String GOOGLE_ACCOUNT_ID;
    private String profileImage;
    private boolean ROLENAME;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String name = "USER";
        if (ROLENAME)
            name = "ADMIN";

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + name));
        //authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.PASSWORD;
    }

    @Override
    public String getUsername() {
        if (SODIENTHOAI != null && !SODIENTHOAI.isEmpty()) {
            return SODIENTHOAI;
        } else if (EMAIL != null && !EMAIL.isEmpty()) {
            return EMAIL;
        }
        return "";
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

    @Override
    public String getName() {
        return getAttribute("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<String, Object>();
    }


}
