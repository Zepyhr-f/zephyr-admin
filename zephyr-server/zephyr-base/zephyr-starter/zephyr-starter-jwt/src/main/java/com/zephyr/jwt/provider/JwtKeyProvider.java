package com.zephyr.jwt.provider;

import java.security.Key;

public interface JwtKeyProvider {
    Key getPrivateKey();
    Key getPublicKey();
}
