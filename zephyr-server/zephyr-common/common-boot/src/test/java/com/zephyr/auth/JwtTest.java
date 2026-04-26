package com.zephyr.auth;

import com.zephyr.auth.service.ZephyrUser;
import com.zephyr.common.CommonApplication;
import com.zephyr.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@SpringBootTest(classes = CommonApplication.class)
public class JwtTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void tokenTest() {
        ZephyrUser userDetails = ZephyrUser.builder()
                .username("zephyr")
                .userCode("U001")
                .roleCodes(List.of("ROLE_ADMIN"))
                .build();
        Map<String, Object> claims = Map.of(
                "user_code", userDetails.getUserCode(),
                "role_codes", String.join(",", userDetails.getRoleCodes())
        );
        String s = jwtUtil.generateToken(claims);
        System.out.println(s);

        Claims claims1 = jwtUtil.extractAllClaims(s);
        System.out.println(claims1);
    }
}