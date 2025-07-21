package com.project.Shopapp.components;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;
import com.project.Shopapp.repositories.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Value("${jwt.expiration}")
    private Long expiration; // Save to an environment variable
    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Account account) throws Exception {
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("SODIENTHOAI", account.getSODIENTHOAI());
        claims.put("USERID", account.getUSERID());
        try {
            return Jwts
                    .builder()
                    .setClaims(claims)
                    .setSubject(account.getSODIENTHOAI())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            // Có thể dùng Logger, instead System.out.println
            throw new InvalidParameterException("Khong the tao jwt token, error: " + e.getMessage());
        }
    }

    // Get all claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get one claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, Account userDetails) {
        try {
            String phoneNumber = extractPhoneNumber(token);
            Token existingToken = tokenRepository.findByTOKEN(token);
            if (existingToken == null || existingToken.isREVOKED() || !userDetails.isIS_ACTIVE()) {
                return false;
            }
            return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        }
        return false;
    }
}
