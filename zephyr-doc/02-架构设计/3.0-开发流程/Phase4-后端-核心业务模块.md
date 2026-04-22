# Phase 4 · 后端核心业务模块

> **目标**：实现 System 模块全部 CRUD 接口（用户/角色/菜单/部门/字典），这是管理后台的核心业务基础。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 4-BE-01 | `UserController`：用户列表（分页）/ 新增 / 编辑 / 删除 / 重置密码 / 分配角色 | 🔴 P0 |
| 4-BE-02 | `RoleController`：角色列表 / 新增 / 编辑 / 删除 / 分配菜单权限 | 🔴 P0 |
| 4-BE-03 | `MenuController`：菜单树查询 / 新增 / 编辑 / 删除 | 🔴 P0 |
| 4-BE-04 | `DeptController`：部门树查询 / 新增 / 编辑 / 删除 | 🟡 P1 |
| 4-BE-05 | `DictController`：字典类型 + 字典数据的 CRUD | 🟡 P1 |
| 4-BE-06 | 角色权限变更时主动刷新 Redis 权限缓存 | 🔴 P0 |
| 4-BE-07 | 用户状态变更（启用/禁用）时，主动失效对应 Token（写黑名单） | 🟡 P1 |
| 4-BE-08 | 导出功能：用户列表 Excel 导出（EasyExcel） | 🟢 P2 |
| 4-BE-09 | 操作日志记录：通过 AOP 拦截关键操作，写入审计日志表 | 🟢 P2 |

---

## 2. 接口清单

### 用户管理（`/api/system/users`）

| Method | Path | 说明 |
|---|---|---|
| `GET` | `/api/system/users` | 分页查询用户列表 |
| `GET` | `/api/system/users/{id}` | 查询用户详情 |
| `POST` | `/api/system/users` | 新增用户 |
| `PUT` | `/api/system/users/{id}` | 编辑用户 |
| `DELETE` | `/api/system/users/{id}` | 删除用户 |
| `PATCH` | `/api/system/users/{id}/status` | 修改用户状态（启用/禁用） |
| `PUT` | `/api/system/users/{id}/password/reset` | 重置用户密码 |
| `PUT` | `/api/system/users/{id}/roles` | 分配角色 |

### 角色管理（`/api/system/roles`）

| Method | Path | 说明 |
|---|---|---|
| `GET` | `/api/system/roles` | 分页查询角色列表 |
| `GET` | `/api/system/roles/all` | 查询全部角色（用于下拉选择） |
| `POST` | `/api/system/roles` | 新增角色 |
| `PUT` | `/api/system/roles/{id}` | 编辑角色 |
| `DELETE` | `/api/system/roles/{id}` | 删除角色 |
| `PUT` | `/api/system/roles/{id}/menus` | 分配菜单权限 |

### 菜单管理（`/api/system/menus`）

| Method | Path | 说明 |
|---|---|---|
| `GET` | `/api/system/menus/tree` | 查询菜单树 |
| `POST` | `/api/system/menus` | 新增菜单/权限 |
| `PUT` | `/api/system/menus/{id}` | 编辑 |
| `DELETE` | `/api/system/menus/{id}` | 删除 |

---

## 3. 代码分层规范

以 `UserController` 为例，严格遵守分层原则：

```
UserController          ← 只做参数校验 + 调用 Service + 包装 R<T>
  └─ IUserService        ← 定义业务接口
       └─ UserServiceImpl ← 实现业务逻辑（调用 Mapper、Feign、Redis）
            └─ UserMapper  ← MyBatis-Plus Mapper，只做数据库操作
```

**MapStruct 转换**（`UserConvert`）：
```
UserCreateRequest → SysUser（创建时）
SysUser → UserVO（查询响应）
SysUser → UserDetailVO（详情响应）
```

---

## 4. 权限标识规范

每个接口对应一个权限标识，写在 `sys_menu` 表的 `perms` 字段：

| 接口 | 权限标识 |
|---|---|
| 用户列表查询 | `sys:user:list` |
| 新增用户 | `sys:user:create` |
| 编辑用户 | `sys:user:update` |
| 删除用户 | `sys:user:delete` |
| 重置密码 | `sys:user:reset-password` |
| 分配角色 | `sys:user:assign-role` |

格式：`<模块>:<实体>:<操作>`

---

## 5. 角色权限缓存刷新时机

| 触发操作 | 缓存刷新行为 |
|---|---|
| 角色分配菜单（`PUT /roles/{id}/menus`） | 主动刷新 `zephyr:role:{roleId}` |
| 删除菜单 | 刷新所有关联角色的缓存 |
| 用户角色变更 | 刷新该用户下次请求时重新加载权限 |

---

## 6. 阶段验收标准

- [ ] 所有接口通过 Postman/Apifox 测试，响应格式符合规范
- [ ] 新增用户后，用户列表可查询到
- [ ] 角色分配菜单后，Redis 权限缓存立即更新
- [ ] 禁用用户后，该用户再次请求接口返回 401
- [ ] MapStruct 转换无字段丢失（重点检查嵌套对象）
- [ ] 分页接口 `page=1&size=20` 返回正确的分页结构
