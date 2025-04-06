package com.project.Shopapp.Components;

import com.project.Shopapp.Models.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private Long expiration; // Save to an environment variable
    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Account account) throws Exception{
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("SODIENTHOAI", account.getSODIENTHOAI());
        try {
            return Jwts
                    .builder()
                    .setClaims(claims)
                    .setSubject(account.getSODIENTHOAI())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            // Có thể dùng Logger, instead System.out.println
            throw new InvalidParameterException("Khong the tao jwt token, error: " + e.getMessage());
        }
    }

    // Get all claims
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    // Get one claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
            final Claims claims = this.extractAllClaims(token);
            return claimsResolver.apply(claims);
    }

    // Check expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
}
