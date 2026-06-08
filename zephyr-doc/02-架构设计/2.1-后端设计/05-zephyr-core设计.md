# zephyr-core 核心基类设计

> 本文档定义 `zephyr-framework/zephyr-core` 的职责边界、包结构与核心基类/通用模型的设计规范。`zephyr-core` 是**全工程最底层**公共依赖，必须保持“纯净、稳定、可复用、零业务”。

---

## 1. 定位与边界

### 1.1 模块定位
- **定位**：为所有服务（`zephyr-platform-service`、`zephyr-business-service`、`zephyr-gateway`）与所有业务模块提供最基础的通用能力与基类模型。
- **使用方式**：业务模块与 starter 通过 Maven 依赖引入；代码中直接 import 使用。

### 1.2 严格边界（必须遵守）
`zephyr-core` **允许**包含：
- 统一响应体/分页模型/通用 DTO 模型基类
- 全局错误码、业务异常、断言工具
- 通用常量、上下文载体（非框架绑定）
- 纯 Java 工具类（日期、字符串、集合等**可选**，避免重复引入第三方工具包）

`zephyr-core` **禁止**包含（避免污染底座）：
- 任何业务语义（如 `SysUser`、`Order`、`Menu`、`Tenant` 等业务实体）
- Spring 组件（`@Component/@Service/@Configuration` 等）与自动装配逻辑  
  > Spring 相关能力请放在 `zephyr-starter-*` 中
- 数据库/Redis/MQ/HTTP 客户端等任何基础设施代码
- MyBatis/JPA 注解实体、Mapper 接口

### 1.3 依赖约束（推荐）
- `zephyr-core` 依赖尽可能少：建议仅依赖 `jackson-annotations`（可选）、`validation-api`（可选）、`lombok`（可选，仅编译期）
- `zephyr-core` 不应反向依赖任何 starter、platform、business、gateway、api 契约模块。

---

## 2. 推荐包结构

> 包名以 `com.zephyr.core` 为根，按“语义稳定性”组织。

```
com.zephyr.core
├── constants/          # 常量（系统级通用，不带业务语义）
├── context/            # 上下文对象（请求链路/租户/用户等载体；不依赖 Spring）
├── exception/          # 异常体系（BusinessException 等）
├── model/              # 通用模型（R、PageResult、BaseEntity 等）
│   ├── entity/
│   ├── page/
│   └── response/
├── result/             # 错误码/结果码定义（ResultCode/错误码接口）
└── util/               # 纯 Java 工具（谨慎收敛，避免“大杂烩”）
```

---

## 3. 统一响应体设计（`R<T>`）

### 3.1 设计目标
- **前后端协议稳定**：任何服务返回结构一致，网关/前端/日志平台可统一解析
- **可扩展**：允许增加 traceId、timestamp 等字段，但保持向后兼容
- **严禁返回裸对象**：Controller 必须返回 `R<T>`（除下载流等特例）

### 3.2 数据结构建议
```java
public class R<T> {
    private int code;
    private boolean success;
    private String msg;
    private T data;
    private Long ts;        // 服务器时间戳（可选）
    private String traceId; // 链路追踪 ID（可选）

    public static <T> R<T> ok(T data) { ... }
    public static <T> R<T> fail(int code, String msg) { ... }
}
```

### 3.3 约定
- `success=true` 时：`code=200`（或 `ResultCode.SUCCESS`），`msg` 可为空
- `success=false` 时：`data` 必须为 `null`（避免误用）
- `traceId` 由网关或 web starter 注入（`zephyr-core` 只提供字段与模型）

---

## 4. 分页模型（`PageResult<T>`）

> 分页是后台管理系统的高频能力，必须全局统一，避免前端适配多套格式。

```java
public class PageResult<T> {
    private long total;
    private long pageNo;
    private long pageSize;
    private java.util.List<T> list;
}
```

建议：
- `pageNo` 从 1 开始（与多数后台系统一致）
- `pageSize` 强制上限（例如 200），上限校验放到 web starter 或 controller 参数校验层

---

## 5. 错误码体系（`ResultCode` / `IResultCode`）

### 5.1 目标
- 错误码跨服务可复用：网关、平台域、业务域都遵守同一套语义
- 错误码可扩展：平台域/业务域可“追加”自己的模块错误码，但不应修改 core 的基础码

### 5.2 建议形式
```java
public interface IResultCode {
    int code();
    String msg();
}

public enum ResultCode implements IResultCode {
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    VALIDATION_ERROR(422, "参数校验失败"),
    SYSTEM_ERROR(500, "系统错误");
}
```

### 5.3 扩展规范（推荐）
平台域/业务域新增错误码时：
- 以模块为维度新建枚举（例如 `SystemErrorCode`、`OrderErrorCode`）
- code 号段按模块划分（例如 `41xxx`=system、`42xxx`=auth、`51xxx`=order）

---

## 6. 异常体系（`BusinessException` + 断言）

### 6.1 异常类型建议
```java
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(IResultCode rc) { ... }
    public BusinessException(int code, String message) { ... }
}
```

扩展（可选）：
- `ForbiddenException` / `UnauthorizedException`（仍属于业务异常语义层）
- `ValidationException`（配合参数校验结果）

> 全局异常处理器 `@RestControllerAdvice` 不建议放在 `zephyr-core`，应落在 `zephyr-starter-web` 中；`zephyr-core` 只提供异常类型与错误码。

### 6.2 断言工具（推荐）
避免在业务代码里反复写：
```java
if (x == null) throw new BusinessException(...);
```

提供：
```java
public final class Assert {
    public static void notNull(Object obj, IResultCode rc) { ... }
    public static void isTrue(boolean condition, IResultCode rc) { ... }
}
```

---

## 7. 基础实体（`BaseEntity`）

### 7.1 目标
- 所有数据库实体继承同一基类，字段语义一致，便于审计、分页、通用拦截器处理
- 字段填充策略与 ORM 框架解耦：填充动作由 `zephyr-starter-mybatis`（MetaObjectHandler）实现

### 7.2 字段建议
```java
public abstract class BaseEntity {
    private Long id;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer deleted; // 0/1 逻辑删除（具体映射由 MyBatis-Plus 配置）
}
```

约定：
- `createUser/updateUser` 存“用户唯一标识”（如 userCode），而不是昵称
- `deleted` 的字段名与含义全局统一（避免每表不一致）

---

## 8. 上下文与链路字段（可选）

`zephyr-core` 可提供“无框架绑定”的上下文载体，具体注入由网关/web starter 完成。

示例：
```java
public final class RequestContext {
    private String traceId;
    private String userCode;
    private String tenantCode;
    private java.util.Set<String> roles;
}
```

注意：
- 不要在 `zephyr-core` 中使用 `ThreadLocal` 强行绑定框架行为；如需 ThreadLocal，请在 `zephyr-starter-web` 中封装生命周期管理。

---

## 9. 与其它模块的关系（非常重要）

| 模块 | 关系 | 说明 |
|---|---|---|
| `zephyr-starter-web` | 依赖 `zephyr-core` | 全局异常处理、统一返回封装、traceId 注入等在 starter-web 落地 |
| `zephyr-starter-security` | 依赖 `zephyr-core` | 认证/鉴权过程中产生的错误码与异常使用 core 统一定义 |
| `zephyr-starter-mybatis` | 依赖 `zephyr-core` | `BaseEntity`、分页模型等由 starter-mybatis 负责与 MP 集成 |
| `zephyr-api/*` | 可依赖 `zephyr-core` | DTO 可复用 `PageResult`、通用错误码接口，但不要依赖 Spring |
| `platform/business/gateway` | 依赖 `zephyr-core` | 所有服务与模块共享底座能力 |

---

## 10. 演进与兼容性策略
- `zephyr-core` 的公开 API（`R`、`BaseEntity`、错误码接口）应尽量保持稳定
- 对外字段新增采用“可选字段”方式，避免破坏前端与网关兼容
- 重大调整必须走版本号升级与迁移说明（写入 changelog）

