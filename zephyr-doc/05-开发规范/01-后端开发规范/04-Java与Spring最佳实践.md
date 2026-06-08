# Java 与 Spring 最佳实践

本规范旨在整合 Java 21 与 Spring Boot 3 的核心优势，提升系统性能与开发效率（具体版本以《版本与环境矩阵》为准）。

## RESTful API 设计规范

### 统一返回格式
- **必须使用统一的包装类**：后端返回给前端的数据必须统一封装在 `R<T>`（或 `Result<T>`）对象中。
- **状态码**：禁止直接返回 200/500，应结合业务层面的枚举状态码。
- **空值处理**：集合类返回空列表 `[]` 而非 `null`；对象类若查询为空应明确提示或按业务逻辑返回。

### 接口定义
- **路径命名**：URL 采用全小写加连字符（kebab-case），如 `/sys-user/list-by-dept`。
- **动词映射**：
    - `POST`: 新增资源。
    - `PUT`: 更新完整资源。
    - `PATCH`: 部分更新资源。
    - `GET`: 获取资源（禁止有副作用）。
    - `DELETE`: 删除资源（逻辑删除）。

---

## 分层架构最佳实践

### 控制层 (Controller)
- **参数验证**：使用 `@Validated` 与 `javax.validation` 注解。
- **转换逻辑**：使用 `MapStruct` 或 `BeanUtil` 将 DTO 转换为 Entity，禁止在 Controller 中手动拼装复杂对象。

### 业务层 (Service)
- **单一入口**：复杂的业务流程应拆分为多个内部私有方法，保持 `execute/run` 类方法的清晰逻辑。
- **事务范围**：尽量缩小 `@Transactional` 的范围，避免在长耗时（如外部 HTTP 请求）的操作上开启事务。
- **解耦**：不同微服务间的调用必须通过网关或 Feign 接口，严禁绕过安全验证直接请求内部端口。

### 持久层 (Mapper)
- **MyBatis-Plus 插件**：利用好自动填充（Auto Fill）、分页插件（PaginationInnerInterceptor）。
- **SQL 优化**：
    - 禁止使用 `SELECT *`。
    - 避免在 Mapper.xml 中编写过于复杂的逻辑 SQL，优先在代码中处理。
    - 索引覆盖：确保查询条件命中索引。

---

## Spring Boot 核心特性应用

### 配置管理
- **敏感信息隐藏**：禁止在 `application.yml` 中提交数据库密码、密钥等明文，应使用环境变量或配置中心（Nacos）。
- **Profile 隔离**：明确划分 `dev`, `test`, `prod` 配置文件。

### 组件使用
- **Bean 注入**：优先使用构造器注入（Constructor Injection）而非 `@Autowired`（字段注入），这有助于单测且符合 Spring 官方推荐。
- **异步处理**：对于导出文件、发送告警等非主流程操作，使用 `@Async`。
- **跨域处理**：在 Gateway 层面统一配置跨域（CORS），微服务内部不再重复配置。

---

## 代码风格细节

- **Optional 使用**：在业务代码中利用 `Optional` 优雅处理 `null`，避免过多的 `if (obj != null)`。
- **集合操作**：熟练运用 Java 8+ 的 `Stream API` 进行过滤、转换、聚合操作。
- **资源关闭**：使用 `try-with-resources` 自动管理文件流、网络连接等资源。
