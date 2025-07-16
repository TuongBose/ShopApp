package com.project.Shopapp.repositories;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findByUSERID(Account account);
    Token findByTOKEN(String token);
    Token findByRefreshToken(String refreshToken);
}
