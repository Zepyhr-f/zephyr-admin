# Phase 1 · 后端工程脚手架

> **目标**：搭建 Maven 多模块骨架，建立统一的基础设施层（BOM / 响应体 / 异常 / Starter），让后续所有业务模块在零配置下直接开工。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 1-BE-01 | 创建根 `pom.xml`，定义模块继承关系 | 🔴 P0 |
| 1-BE-02 | 创建 `zephyr-bom` 模块，统一锁定三方依赖版本 | 🔴 P0 |
| 1-BE-03 | 创建 `zephyr-core`：`R<T>` 响应体、`ResultCode`、`BaseEntity`、`BusinessException`、`GlobalExceptionHandler` | 🔴 P0 |
| 1-BE-04 | 创建 `zephyr-starter-jwt`：`JwtUtil`、`JwtConfig`（ES256 默认） | 🔴 P0 |
| 1-BE-05 | 创建 `zephyr-starter-mp`：`MetaObjectHandler`（自动填充）、乐观锁插件、逻辑删除配置 | 🔴 P0 |
| 1-BE-06 | 创建 `zephyr-starter-redis`：`RedisUtil`、`RedisConstant` | 🟡 P1 |
| 1-BE-07 | 创建 `zephyr-gateway` 骨架，配置 Spring Cloud Gateway 路由 | 🟡 P1 |
| 1-BE-08 | 配置 Flyway 迁移脚本目录，建立版本命名规范 | 🟡 P1 |
| 1-BE-09 | 配置 Smart-Doc Maven 插件，确保 `mvn smart-doc:html` 可正常生成文档 | 🟢 P2 |
| 1-BE-10 | 配置 Lefthook + Commitlint，提交前自动 Checkstyle | 🟢 P2 |

---

## 2. 目录结构产出

```
zephyr-server/
├── pom.xml                     ← 根 POM，聚合所有子模块
├── zephyr-bom/pom.xml          ← 版本锁定 BOM
├── zephyr-base/
│   ├── zephyr-core/            ← R<T>、BaseEntity、异常体系
│   └── zephyr-starter/
│       ├── zephyr-starter-jwt/
│       ├── zephyr-starter-mp/
│       └── zephyr-starter-redis/
├── zephyr-gateway/             ← 网关骨架
└── zephyr-common/              ← 占位，Phase 3 填充
```

---

## 3. 统一响应体规范

所有接口必须返回 `R<T>` 结构：

```json
{
  "code": 200,
  "success": true,
  "msg": "操作成功",
  "data": { ... }
}
```

错误码规范：

| code | 含义 |
|---|---|
| 200 | 操作成功 |
| 400 | 参数校验失败 |
| 401 | Token 无效或已过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 4. Maven BOM 关键依赖版本

| 依赖 | 版本 |
|---|---|
| `spring-boot-dependencies` | 3.4.x |
| `spring-cloud-dependencies` | 2023.0.x |
| `mybatis-plus-bom` | 3.5.x |
| `mapstruct` | 1.6.x |
| `lombok` | 1.18.x |
| `flyway-core` | 10.x |
| `smart-doc` Maven Plugin | 3.x |

---

## 5. MyBatis-Plus 自动填充约定

`BaseEntity` 基类字段：

| 字段 | Java 类型 | 填充时机 | 说明 |
|---|---|---|---|
| `id` | `Long` | INSERT | Snowflake ID 自动生成 |
| `createTime` | `LocalDateTime` | INSERT | `MetaObjectHandler` 自动注入 |
| `updateTime` | `LocalDateTime` | INSERT + UPDATE | 自动更新 |
| `createUser` | `Long` | INSERT | 从 SecurityContext 取当前用户 ID |
| `updateUser` | `Long` | INSERT + UPDATE | 同上 |
| `deleted` | `Integer` | — | 逻辑删除标志（0=正常，1=已删除） |

---

## 6. 数据库迁移脚本命名规范（Flyway）

```
db/migration/
├── V1.0.0__init_schema.sql          # 首次建表
├── V1.0.1__add_sys_user_table.sql   # 增量变更
└── V1.1.0__add_sys_dict_table.sql   # 版本迭代
```

格式：`V<版本号>__<描述>.sql`，版本号严格单调递增。

---

## 7. 阶段验收标准

- [ ] `mvn clean install -DskipTests` 全模块无报错
- [ ] `R.ok()` / `R.fail()` 响应体可正常序列化
- [ ] `GlobalExceptionHandler` 捕获 `BusinessException` 返回标准错误格式
- [ ] `MetaObjectHandler` 在 INSERT 时自动填充 `createTime`、`updateTime`
- [ ] `mvn smart-doc:html` 成功生成 HTML 文档
- [ ] Flyway 首次迁移脚本成功执行
