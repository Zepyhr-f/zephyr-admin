# Phase 3 · 前端认证与权限体系

> **目标**：完整实现登录流程、Token 管理、路由守卫和基于后端权限的动态菜单，建立前端安全基础。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 3-FE-01 | 实现登录页（`/login`）：表单 + 验证 + 提交 + 错误提示 | 🔴 P0 |
| 3-FE-02 | 实现 `useAuthStore`：存储 Token、用户信息、权限列表，持久化到 `localStorage` | 🔴 P0 |
| 3-FE-03 | 登录成功后调用 `GET /auth/info`，获取完整用户信息和权限列表 | 🔴 P0 |
| 3-FE-04 | 实现路由守卫 `AuthGuard`：未登录跳 `/login`，已登录访问 `/login` 跳首页 | 🔴 P0 |
| 3-FE-05 | 实现动态菜单：基于 `GET /auth/info` 返回的菜单树，动态注册路由 | 🔴 P0 |
| 3-FE-06 | 实现 Axios 401 拦截：自动调用 `POST /auth/refresh` 刷新 Token，失败则 logout | 🟡 P1 |
| 3-FE-07 | 实现权限指令/组件：`<Permission code="sys:user:create">` 控制按钮级别显隐 | 🟡 P1 |
| 3-FE-08 | 实现登出功能：调用 `POST /auth/logout`，清空 Store，跳转 `/login` | 🟡 P1 |
| 3-FE-09 | 实现 403 / 404 异常页面 | 🟢 P2 |

---

## 2. Token 管理策略

| 令牌 | 存储位置 | 说明 |
|---|---|---|
| **Access Token** | `useAuthStore`（localStorage 持久化） | 请求时自动注入 `Authorization` Header |
| **Refresh Token** | **HttpOnly Cookie**（后端 Set-Cookie 设置） | 前端不可读，由浏览器自动携带至 `/auth/refresh` |

**Token 刷新流程（无感刷新）**：
```
请求返回 401
  └─ 是否已在刷新中？（防止并发多次刷新）
       ├─ 是 → 等待刷新完成，重试原请求
       └─ 否 → 调用 POST /auth/refresh
                  ├─ 成功 → 更新 Store 中 Token，重试原请求
                  └─ 失败 → logout() + 跳转 /login
```

---

## 3. 路由结构设计

```
/ (根路由)
  ├─ /login                    ← AuthLayout（不需要登录）
  ├─ / (AdminLayout)           ← AuthGuard 包裹（需要登录）
  │   ├─ /dashboard            ← 首页
  │   ├─ /system/user          ← 用户管理（动态注册）
  │   ├─ /system/role          ← 角色管理（动态注册）
  │   ├─ /system/menu          ← 菜单管理（动态注册）
  │   └─ ...
  ├─ /403                      ← 权限不足
  └─ /404                      ← 页面不存在（* 兜底）
```

**动态路由注册流程**：
```
登录成功
  └─ GET /auth/info
       └─ 获取 menuTree（后端返回的菜单树）
            └─ generateRoutes(menuTree)
                 └─ 将 { path, component } 列表动态注册到 Router
                      └─ 渲染侧边栏菜单（与路由保持同步）
```

---

## 4. 权限控制方案

### 按钮级别权限（基于权限 code）

使用 `<Permission>` 组件包裹需要权限控制的按钮：

```tsx
// 用户有 sys:user:create 权限时才显示"新增"按钮
<Permission code="sys:user:create">
  <Button type="primary">新增用户</Button>
</Permission>
```

`Permission` 组件内部逻辑：
```ts
// 从 useAuthStore 读取权限列表，判断是否包含 code
const hasPermission = userPermissions.includes(code)
return hasPermission ? children : null
```

### 路由级别权限（基于菜单树）

后端只返回用户有权限的菜单节点，前端动态注册路由。用户无权限的页面根本不存在于路由表中，访问时命中 `/*` 兜底路由，返回 404。

---

## 5. 侧边栏菜单渲染

菜单数据来源：`GET /auth/info` 返回的 `menuTree`，缓存在 `useUserStore`。

菜单项字段映射：

| 后端字段 | 前端用途 |
|---|---|
| `name` | 菜单标题 |
| `path` | 路由 path |
| `icon` | Iconify 图标 name |
| `children` | 子菜单（递归渲染） |
| `type` | 0=目录 1=菜单 2=按钮（按钮不在侧边栏渲染） |

---

## 6. 登录页设计规范

- 居中卡片布局，品牌 Logo + 系统名称
- 用户名 / 密码两个输入框，使用 `react-hook-form` + `zod` 表单验证
- 密码显示/隐藏切换按钮
- 登录按钮 loading 状态（请求期间禁用）
- 错误提示展示后端返回的 `msg` 字段
- 支持回车键提交

---

## 7. 阶段验收标准

- [ ] 输入正确账号密码，成功跳转 Dashboard
- [ ] 输入错误密码，显示后端返回的错误信息
- [ ] 未登录访问 `/dashboard` 自动跳转 `/login`
- [ ] 登录后访问 `/login` 自动跳转 `/dashboard`
- [ ] 侧边栏菜单与后端返回的 `menuTree` 一致
- [ ] 无权限按钮（`<Permission>`）正确隐藏
- [ ] 手动在地址栏输入无权限的路由，跳转 403 或 404 页面
- [ ] `POST /auth/logout` 后，页面跳转登录页且 Token 被清除
