package com.zephyr.jwt.provider;


import com.zephyr.jwt.config.JwtConstant;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.*;

/**
 * HS256 Key Provider
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Component(JwtConstant.ALGORITHM_HS256)
public class HS256KeyProvider implements JwtKeyProvider{

    private final SecretKey secretKey;

    public HS256KeyProvider(@Value("${jwt.hs256.secret:}") String secret) {
        if(secret == null || secret.isEmpty()){
            this.secretKey = this.generateHSKey();
        } else {
            if(secret.length() < 32){
                throw new IllegalArgumentException("The HS256 secret key must be at least 32 characters long for security reasons.");
            }
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    @Override
    public Key getPrivateKey() {
        return secretKey;
    }

    @Override
    public Key getPublicKey() {
        return secretKey;
    }

    public SecretKey generateHSKey(){
        // 生成 HS256 安全密钥
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}