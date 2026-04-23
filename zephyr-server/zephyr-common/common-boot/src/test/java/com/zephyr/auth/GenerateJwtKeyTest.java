package com.zephyr.auth;

import com.zephyr.jwt.provider.ES256KeyProvider;
import com.zephyr.jwt.provider.HS256KeyProvider;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * 密钥生成
 *
 * @author Zephyr
 * @since 2025-09-12
 */
public class GenerateJwtKeyTest {

    /**
     * 生成 ES256 密钥对
     */
    @Test
    public void generateES256Key() throws Exception {
        ES256KeyProvider keyProvider = new ES256KeyProvider("","");
        KeyPair keyPair = keyProvider.generateECKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        System.out.println("Private Key: " + Base64.getEncoder().encodeToString(aPrivate.getEncoded()));
        System.out.println("Public Key: " + Base64.getEncoder().encodeToString(aPublic.getEncoded()));

    }

    /**
     * 生成 HS256 密钥
     */
    @Test
    public void generateRS256Key() throws Exception {
        HS256KeyProvider keyProvider = new HS256KeyProvider("");
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(keyProvider.generateHSKey().getEncoded()));
    }

    @Test
    public void generatePassword() throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = "123456"; // 明文密码
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后密码: " + encodedPassword);

        // 验证是否匹配
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证是否通过: " + matches);
    }
}