package com.zephyr.jwt.util;

import com.zephyr.jwt.provider.JwtKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.zephyr.jwt.config.JwtConstant.USER_CODE;
import static com.zephyr.jwt.config.JwtConstant.USER_NAME;
import static com.zephyr.jwt.config.JwtConstant.TENANT_CODE;
import static com.zephyr.jwt.config.JwtConstant.ROLE_CODES;

/**
 * jwt 工具类
 *
 * @author Zephyr
 * @since 2025-09-13
 */
public class JwtUtil {

    private final JwtKeyProvider keyProvider;
    private final SignatureAlgorithm algorithm;
    private final Long expiration;

    public JwtUtil(JwtKeyProvider keyProvider, SignatureAlgorithm algorithm, Long expiration) {
        this.keyProvider = keyProvider;
        this.algorithm = algorithm;
        this.expiration = expiration;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Map<String, Object> claims) {
        if (claims == null || !claims.containsKey(USER_CODE)) {
            throw new IllegalArgumentException("Claims must contain userCode");
        }

        // 确保不会覆盖保留字段
        Map<String, Object> safeClaims = new HashMap<>(claims);
        safeClaims.remove("sub");
        safeClaims.remove("iat");
        safeClaims.remove("exp");

        return Jwts.builder()
                .setClaims(safeClaims)
                .setSubject(claims.get(USER_NAME).toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(keyProvider.getPrivateKey(), algorithm)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}