package com.zephyr.jwt.provider;


import com.zephyr.jwt.config.JwtConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * ES256 Key Provider
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Component(JwtConstant.ALGORITHM_ES256)
public class ES256KeyProvider implements JwtKeyProvider{

    private final KeyPair keyPair;

    public ES256KeyProvider(
            @Value("${jwt.es256.privateKey:}") String base64Private,
            @Value("${jwt.es256.publicKey:}") String base64Public
    ) {
        try{
            if(base64Private==null || base64Private.isEmpty() ||
               base64Public==null  || base64Public.isEmpty()) {
                this.keyPair = this.generateECKeyPair();
            } else {
                KeyFactory kf = KeyFactory.getInstance("EC");

                byte[] privateBytes = Base64.getDecoder().decode(base64Private);
                PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

                byte[] publicBytes = Base64.getDecoder().decode(base64Public);
                PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicBytes));

                validateECKeyPair(privateKey, publicKey);

                this.keyPair = new KeyPair(publicKey, privateKey);
            }
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e){
            throw new IllegalStateException("Failed to generate ES256 key pair", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Key getPrivateKey() {
        return keyPair.getPrivate();
    }

    @Override
    public Key getPublicKey() {
        return keyPair.getPublic();
    }

    public KeyPair generateECKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        keyPairGen.initialize(new ECGenParameterSpec("secp256r1")); // P-256
        return keyPairGen.generateKeyPair();
    }

    /**
     * 校验 EC 公私钥是否匹配
     */
    void validateECKeyPair(PrivateKey privateKey, PublicKey publicKey) {
        try {
            byte[] data = "test-data-for-validation".getBytes();
            Signature signer = Signature.getInstance("SHA256withECDSA");
            signer.initSign(privateKey);
            signer.update(data);
            byte[] signature = signer.sign();

            Signature verifier = Signature.getInstance("SHA256withECDSA");
            verifier.initVerify(publicKey);
            verifier.update(data);
            verifier.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException("EC 公私钥合法性检查失败",e);
        }
    }
}