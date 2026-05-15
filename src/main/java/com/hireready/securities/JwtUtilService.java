package com.hireready.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {
    @Value("${jwt.secret}")
    private String jwtSignatureKey;

    private static final Long JWT_TOKEN_VALIDITY = 1000L * 60 * 60 * 3;

    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSignatureKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(extractAllClaims(token));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserSecurity user) {
        String email = extractEmail(token);
        return (!isTokenExpired(token)) && (email.equals(user.getUsername()));
    }

    private String createToken(String subject, Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))  // ⭐ esta línea es la corregida
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UserSecurity securityUser) {
        Map<String, Object> claims = new HashMap<>();
        Object authorities = securityUser.getAuthorities().stream()
                .map(n -> String.valueOf(n.getAuthority())).toList();
        claims.put("authorities", authorities);
        claims.put("user_id", securityUser.getUser().getId());
        return createToken(securityUser.getUsername(), claims);
    }
}
