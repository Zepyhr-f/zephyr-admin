# RPC 契约与 Feign 规范

> 目的：统一跨服务调用方式，确保接口“可演进、可测试、可治理”。

---

## 1. 契约模块边界（zephyr-api/*）
`zephyr-api/*` 只允许包含：
- `@FeignClient` 接口
- RPC DTO（request/response/vo）、枚举、契约级错误码

禁止包含：
- `@Service/@Component` 实现类
- `Mapper/Entity` 与数据库访问
- 任何业务编排逻辑

---

## 2. Feign 设计规范
- 接口命名：`XxxFeignClient`
- DTO 命名：`XxxDTO` / `XxxVO`（与 system 模块一致）
- 超时：必须显式配置（避免默认值导致雪崩）
- 降级：按场景启用（失败要可观测、可告警）

---

## 3. 版本演进
- 向后兼容优先：新增字段必须可选
- 不兼容变更：新建 endpoint 或新建版本（避免直接改旧字段语义）

