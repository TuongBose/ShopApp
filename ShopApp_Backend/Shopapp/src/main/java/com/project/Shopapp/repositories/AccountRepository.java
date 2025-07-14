package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    boolean existsBySODIENTHOAI(String SODIENTHOAI);
    Optional<Account> findBySODIENTHOAI(String SODIENTHOAI);
    // SELECT * FROM ACCOUNTS WHERE SODIENTHOAI = ?
}
