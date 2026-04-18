package com.zephyr.system.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 在线用户控制器
 *
 * @author zephyr
 * @since 2026-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/online")
@Tag(name = "在线用户", description = "在线用户监控")
public class OnlineUserController {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询在线用户列表", description = "从 Redis 实时获取活跃 Token")
    public R<List<Map<String, Object>>> list(@RequestParam(name = "username", required = false) String username) {
        // 获取所有 token: 开头的 key
        Set<String> keys = redisUtil.keys(RedisConstant.TOKEN_PREFIX + "*");
        List<Map<String, Object>> list = new ArrayList<>();

        for (String key : keys) {
            String tokenUsername = key.substring(RedisConstant.TOKEN_PREFIX.length());
            
            // 如果指定了用户名搜索
            if (username != null && !username.isEmpty() && !tokenUsername.contains(username)) {
                continue;
            }

            String token = redisUtil.getString(key);
            try {
                // 解析 token 获取信息 (这里简化处理，实际可能需要更完整的 UserDetails)
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("token", token.substring(0, Math.min(token.length(), 20)) + "...");
                userInfo.put("username", tokenUsername);
                // 暂时返回固定 IP/地点，等后续完善登录时解析
                userInfo.put("ipaddr", "127.0.0.1");
                userInfo.put("loginLocation", "本地");
                userInfo.put("loginTime", jwtUtil.extractAllClaims(token).getIssuedAt());
                
                list.add(userInfo);
            } catch (Exception e) {
                // token 解析失败或过期，清理掉
                redisUtil.deleteKey(key);
            }
        }
        
        // 按登录时间排序
        list.sort((a,b) -> ((Date)b.get("loginTime")).compareTo((Date)a.get("loginTime")));
        
        return R.data(list);
    }

    @PostMapping("/kickout")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "强退用户", description = "通过用户名强制其无效化")
    public R<Boolean> kickout(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        if (username != null) {
            redisUtil.deleteKey(RedisConstant.TOKEN_PREFIX + username);
            return R.success("用户已强制下线");
        }
        return R.fail("缺少用户名");
    }
}
