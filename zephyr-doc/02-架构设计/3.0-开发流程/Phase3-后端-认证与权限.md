# Phase 3 · 后端认证与权限体系

> **目标**：完整实现双令牌认证、RBAC 权限模型和角色权限缓存，为所有后续业务接口提供安全基础。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 3-BE-01 | 实现 `POST /auth/login`：BCrypt 验证 + 双令牌发行 | 🔴 P0 |
| 3-BE-02 | 实现 `POST /auth/refresh`：校验 Refresh Token，发行新 Access Token | 🔴 P0 |
| 3-BE-03 | 实现 `POST /auth/logout`：Access Token jti 写黑名单，删 Refresh Token | 🔴 P0 |
| 3-BE-04 | 实现 `GET /auth/info`：解析 Token，返回用户信息 + 完整权限列表 | 🔴 P0 |
| 3-BE-05 | 实现 `AuthFilter`（Gateway 全局过滤器）：白名单放行 + Token 校验 + 黑名单比对 | 🔴 P0 |
| 3-BE-06 | `RolePermsLoader`：服务启动时预热角色权限至 Redis（Key: `role:{roleId}`） | 🔴 P0 |
| 3-BE-07 | 配置 Spring Security：禁用 Session、关闭 CSRF、配置 `PasswordEncoder` | 🟡 P1 |
| 3-BE-08 | 实现 `zephyr-system` 基础：`sys_user` 查询、角色查询接口（供 auth 服务 Feign 调用） | 🟡 P1 |
| 3-BE-09 | 配置 Nacos 动态白名单（`authProperties.whiteApi`），支持热更新 | 🟢 P2 |

---

## 2. 双令牌实现细节

### 登录流程
```
POST /auth/login { username, password }
  ├─ 查询 sys_user（Feign → zephyr-system）
  ├─ BCrypt.matches(password, hashedPassword)
  ├─ 生成 Access Token（ES256，Claims: userId / username / roleId，TTL: 30min）
  ├─ 生成 Refresh Token（UUID，存入 Redis: refresh:{username} TTL: 7天）
  ├─ Response Header: Authorization: Bearer <accessToken>
  └─ Response Set-Cookie: Refresh-Token=<refreshToken>; HttpOnly; SameSite=Strict
```

### 刷新流程
```
POST /auth/refresh（携带 Cookie）
  ├─ 读取 Cookie 中的 Refresh Token
  ├─ 比对 Redis: refresh:{username}
  ├─ 有效 → 生成新 Access Token
  └─ 返回新 Token（同登录响应格式）
```

### 强制下线
```
POST /auth/logout
  ├─ 解析 Access Token，取 jti（JWT ID）
  ├─ 写入 Redis 黑名单：blacklist:{jti}，TTL = Token 剩余有效期
  └─ 删除 Redis: refresh:{username}
```

---

## 3. AuthFilter 设计（Gateway 层）

```
请求进入 Gateway
  └─ 匹配白名单（/auth/login, /auth/refresh, /actuator/**）？
       ├─ 是 → 直接放行
       └─ 否 → 读取 Authorization Header
                  └─ Header 存在？
                       ├─ 否 → 返回 401（Token 缺失）
                       └─ 是 → JWT 签名验证
                                └─ 签名有效？
                                     ├─ 否 → 返回 401（Token 非法）
                                     └─ 是 → 查询 Redis 黑名单（blacklist:{jti}）
                                              └─ 在黑名单？
                                                   ├─ 是 → 返回 401（Token 已注销）
                                                   └─ 否 → 注入 X-User-Id、X-Username、X-Role-Id → 转发
```

---

## 4. RBAC 权限缓存

服务启动时，`RolePermsLoader`（实现 `ApplicationRunner`）预热所有角色权限：

```
FOR 每个角色 IN sys_role:
  查询 sys_role_menu JOIN sys_menu.perms → 得到权限标识集合
  Redis SET: "role:{roleId}" = {"sys:user:list", "sys:user:create", ...}
```

接口鉴权时（在各业务服务中）：
- 从网关注入的 `X-Role-Id` Header 读取角色 ID
- 查 Redis `role:{roleId}` 得到权限集
- 与接口所需权限（`@PreAuthorize("hasPermission('sys:user:list')")`）比对

---

## 5. Redis Key 规范

| Key 格式 | 含义 | TTL |
|---|---|---|
| `zephyr:token:{username}` | Access Token 副本（用于比对） | 30 min |
| `zephyr:refresh:{username}` | Refresh Token | 7 days |
| `zephyr:blacklist:{jti}` | 已注销 Token 黑名单 | Access Token 剩余有效期 |
| `zephyr:role:{roleId}` | 角色权限标识集合 | 永久（权限变更时主动刷新） |

---

## 6. 阶段验收标准

- [ ] `POST /auth/login` 正常返回 Access Token 并设置 Refresh Cookie
- [ ] `POST /auth/refresh` 使用 Refresh Cookie 成功刷新 Access Token
- [ ] `POST /auth/logout` 后，旧 Access Token 请求其他接口返回 401
- [ ] 未携带 Token 访问非白名单接口返回 401
- [ ] Redis 中可看到 `zephyr:role:{id}` 权限缓存数据
- [ ] `GET /auth/info` 返回当前用户完整信息和权限列表
