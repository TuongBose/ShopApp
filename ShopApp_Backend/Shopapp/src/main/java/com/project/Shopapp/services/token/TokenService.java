package com.project.Shopapp.services.token;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final TokenRepository tokenRepository;
    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Override
    @Transactional
    public void addToken(Account account, String token, boolean isMobileDevice) {
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
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
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
        tokenRepository.save(newToken);
    }
}
