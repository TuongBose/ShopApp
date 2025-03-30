package com.project.Shopapp.Repositories;

import com.project.Shopapp.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    boolean existsBySODIENTHOAI(String SODIENTHOAI);
    Optional<Account> findBySODIENTHOAI(String SODIENTHOAI);
    // SELECT * FROM ACCOUNTS WHERE SODIENTHOAI = ?
}
