package com.project.Shopapp.services.token;

import com.project.Shopapp.components.JwtTokenUtils;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.exceptions.ExpiredTokenException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Override
    @Transactional
    public Token addToken(Account account, String token, boolean isMobileDevice) {
        List<Token> accountTokens = tokenRepository.findByUSERID(account);
        int tokenCount = accountTokens.size();
        // If count over limit, delete one token old
        if (tokenCount >= MAX_TOKENS) {
            // Kiem tra xem trong danh sach accountTokens co ton tai it nhat
            // mot token khong phai la thiet bi di dong (non-mobile)
            boolean hasNonMobileToken = !accountTokens.stream().allMatch(Token::isIS_MOBILE);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = accountTokens.stream()
                        .filter(accountToken -> !accountToken.isIS_MOBILE())
                        .findFirst()
                        .orElse(accountTokens.getFirst());
            } else {
                // Tat ca cac token deu la thiet bi di dong, chung ta se xoa token dau tien trong danh sach
                tokenToDelete = accountTokens.getFirst();
            }
            tokenRepository.delete(tokenToDelete);
        }

        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        // Create new token for account
        Token newToken = Token.builder()
                .USERID(account)
                .TOKEN(token)
                .REVOKED(false)
                .EXPIRED(false)
                .TOKEN_TYPE("Bearer")
                .EXPIRATION_DATE(expirationDateTime)
                .IS_MOBILE(isMobileDevice)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setREFRESH_EXPIRATION_DATE(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token refreshToken(String refreshToken, Account account) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if (existingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        if (existingToken.getREFRESH_EXPIRATION_DATE().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new ExpiredTokenException("Refresh token is expired");
        }

        String token = jwtTokenUtils.generateToken(account); // Return token
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);

        existingToken.setTOKEN(token);
        existingToken.setEXPIRATION_DATE(expirationDateTime);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setREFRESH_EXPIRATION_DATE(LocalDateTime.now().plusSeconds(expirationRefreshToken));

        tokenRepository.save(existingToken);
        return existingToken;

    }
}
