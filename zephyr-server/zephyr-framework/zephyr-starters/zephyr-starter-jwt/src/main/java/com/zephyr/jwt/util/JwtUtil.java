package com.zephyr.jwt.util;

import com.zephyr.jwt.provider.JwtKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.zephyr.jwt.config.JwtConstant.*;

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

    public String extractUserCode(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractJti(String token) {
        return extractClaim(token, claims -> claims.get(JTI, String.class));
    }

    public String extractTenantCode(String token) {
        return extractClaim(token, claims -> claims.get(TENANT_CODE, String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(keyProvider.getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // token 过期时仍返回 claims（用于提取 jti 做黑名单校验等）
            return e.getClaims();
        }
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public long getTokenRemainingTime(String token) {
        Date expirationDate = extractExpiration(token);
        long remaining = expirationDate.getTime() - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }

    public String generateJti() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String generateToken(Map<String, Object> claims) {
        return generateToken(claims, this.expiration);
    }

    public String generateToken(Map<String, Object> claims, long customExpiration) {
        if (claims == null || !claims.containsKey(USER_CODE)) {
            throw new IllegalArgumentException("Claims must contain userCode");
        }

        // 确保不会覆盖保留字段
        Map<String, Object> safeClaims = new HashMap<>(claims);
        safeClaims.remove("sub");
        safeClaims.remove("iat");
        safeClaims.remove("exp");
        safeClaims.remove("jti");

        // 自动注入 jti
        String jti = generateJti();
        safeClaims.put(JTI, jti);

        return Jwts.builder()
                .setClaims(safeClaims)
                .setSubject(claims.get(USER_CODE).toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + customExpiration))
                .setId(jti)
                .signWith(keyProvider.getPrivateKey(), algorithm)
                .compact();
    }

    public Boolean validateToken(String token, String userCode) {
        final String tokenUserCode = extractUserCode(token);
        return (tokenUserCode.equals(userCode) && !isTokenExpired(token));
    }
}
