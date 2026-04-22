# Phase 2 · 后端接口契约

> **目标**：在真正写业务逻辑之前，先完整定义所有接口规范，输出 OpenAPI 文档给前端，让两端并行开发彻底解耦。这是**前后端协作的关键节点**。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 2-BE-01 | 定义全局 API 规范（URL 风格、HTTP 方法、错误码） | 🔴 P0 |
| 2-BE-02 | 为所有接口编写 JavaDoc 注释（Smart-Doc 读取） | 🔴 P0 |
| 2-BE-03 | 执行 `mvn smart-doc:openapi` 生成 OpenAPI 3.0 JSON | 🔴 P0 |
| 2-BE-04 | 将 OpenAPI 文档发布至团队共享地址（Apifox / Postman / 静态页面） | 🔴 P0 |
| 2-BE-05 | 编写 Flyway 迁移脚本：`sys_user`、`sys_role`、`sys_menu`、`sys_dept`、`sys_dict` 建表 SQL | 🔴 P0 |
| 2-BE-06 | 配置 `GlobalExceptionHandler`，覆盖所有异常类型 | 🟡 P1 |
| 2-BE-07 | 建立统一分页请求/响应 DTO：`PageQuery`、`PageResult<T>` | 🟡 P1 |
| 2-BE-08 | 配置 CORS（开发环境允许 `localhost:5173`） | 🟡 P1 |

---

## 2. API URL 设计规范

| 规则 | 说明 | 示例 |
|---|---|---|
| **RESTful 风格** | 名词复数，动词由 HTTP Method 表达 | `/api/system/users` |
| **版本前缀** | 当前版本 `v1`（暂可省略，待多版本需求出现再加） | `/api/system/users` |
| **模块前缀** | 按业务域分组 | `/api/auth/**`、`/api/system/**`、`/api/biz/**` |
| **操作前缀** | 非 CRUD 操作用动词 | `/api/system/users/{id}/reset-password` |

HTTP Method 约定：

| Method | 场景 |
|---|---|
| `GET` | 查询（列表、详情） |
| `POST` | 创建 |
| `PUT` | 全量更新 |
| `PATCH` | 部分更新（如状态变更） |
| `DELETE` | 删除 |

---

## 3. 分页接口规范

**请求**（`PageQuery`）：
```json
{
  "page": 1,
  "size": 20,
  "sortField": "createTime",
  "sortOrder": "DESC"
}
```

**响应**（`PageResult<T>`）：
```json
{
  "code": 200,
  "success": true,
  "data": {
    "records": [ ... ],
    "total": 100,
    "page": 1,
    "size": 20
  }
}
```

---

## 4. 核心数据库表清单（Flyway 脚本）

| 脚本文件 | 建表内容 |
|---|---|
| `V1.0.0__init_schema.sql` | 建库配置 |
| `V1.0.1__create_sys_user.sql` | `sys_user`：用户表 |
| `V1.0.2__create_sys_role.sql` | `sys_role`：角色表、`sys_user_role`：用户角色关联 |
| `V1.0.3__create_sys_menu.sql` | `sys_menu`：菜单/权限表 |
| `V1.0.4__create_sys_role_menu.sql` | `sys_role_menu`：角色菜单关联 |
| `V1.0.5__create_sys_dept.sql` | `sys_dept`：部门表 |
| `V1.0.6__create_sys_dict.sql` | `sys_dict`、`sys_dict_data`：数据字典 |

---

## 5. Smart-Doc JavaDoc 规范

每个 Controller 方法必须写标准 JavaDoc：

```java
/**
 * 分页查询用户列表
 *
 * @param query 分页查询参数
 * @return 用户分页数据
 * @author zephyr
 * @since 1.0.0
 */
@GetMapping("/users")
public R<PageResult<UserVO>> listUsers(@Valid PageQuery query) { ... }
```

每个 DTO 字段必须有注释：
```java
public class UserCreateRequest {
    /** 用户名，4-20位字符，不可重复 */
    @NotBlank
    private String username;

    /** 手机号，国内11位格式 */
    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;
}
```

---

## 6. CORS 配置

开发环境允许前端 `localhost:5173` 跨域访问网关：

```yaml
# application-dev.yml（在 gateway 配置）
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "http://localhost:5173"
            allowed-methods: "*"
            allowed-headers: "*"
            allow-credentials: true
```

---

## 7. 阶段验收标准

- [ ] OpenAPI JSON 已生成，团队可通过 URL 访问
- [ ] 所有核心接口（认证 + 系统模块）已在文档中定义
- [ ] 所有 Flyway 建表脚本执行成功，表结构与设计一致
- [ ] 前端 Apifox / MSW 已导入 OpenAPI，Mock 可用
- [ ] `POST /auth/login` 接口已定义（前端 Phase 3 依赖此接口 Mock）
