package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsBySODIENTHOAI(String SODIENTHOAI);
    boolean existsByEMAIL(String EMAIL);

    Optional<Account> findBySODIENTHOAI(String SODIENTHOAI);
    // SELECT * FROM ACCOUNTS WHERE SODIENTHOAI = ?

    Optional<Account> findByEMAIL(String EMAIL);
    // SELECT * FROM ACCOUNTS WHERE EMAIL = ?

    Optional<Account> findByFACEBOOK_ACCOUNT_ID(String facebookAccountId);
    // SELECT * FROM ACCOUNTS WHERE FACEBOOK_ACCOUNT_ID = ?

    Optional<Account> findByGOOGLE_ACCOUNT_ID(String googleAccountId);
    // SELECT * FROM ACCOUNTS WHERE GOOGLE_ACCOUNT_ID = ?

    @Query("SELECT o FROM Account o WHERE o.IS_ACTIVE = true AND o.ROLENAME = false AND (:keyword IS NULL OR :keyword = '' OR " +
            "o.FULLNAME LIKE %:keyword% " +
            "OR o.DIACHI LIKE %:keyword% " +
            "OR o.SODIENTHOAI LIKE %:keyword%)")
    Page<Account> findAll(@Param("keyword") String keyword, Pageable pageable);
}
