# API 规范与错误码

> 目的：统一 REST 接口设计规范与错误码体系，降低前后端联调成本，提升可观测性。

---

## 文档元信息

| 字段 | 值 |
|---|---|
| 状态 | Draft |
| Owner | Zephyr 文档维护者 |
| 最后更新 | 2026-06-08 |
| 适用范围 | 前端 / 网关 / 后端（接口契约） |
| 关联文档 | [zephyr-core 设计](../2.1-后端设计/05-zephyr-core设计.md)、[RPC 契约与 Feign 规范](./02-RPC契约与Feign规范.md)、[登录与鉴权全链路](../2.3-安全与权限/01-登录与鉴权全链路.md) |

---

## 1. 基本规范
- URL：资源化命名，使用复数（示例：`/users`、`/roles`）
- 方法：GET/POST/PUT/DELETE 语义清晰
- 版本：如需版本化，优先 Header 或 `/v1/`（统一一种方式）
- 幂等：PUT/DELETE 幂等；POST 需按业务定义幂等策略

---

## 2. 统一响应体
所有 JSON 接口统一返回 `R<T>`（详见 `2.1-后端设计/05-zephyr-core设计.md`）：
- 成功：`R.ok(data)`
- 失败：`R.fail(code, msg)`

---

## 3. 分页规范
- 请求：`pageNo`（从 1 开始）、`pageSize`
- 响应：`PageResult<T>`（total/pageNo/pageSize/list）

---

## 4. 错误码规范
- 基础错误码在 `zephyr-core` 统一定义（401/403/422/500 等）
- 模块级错误码按号段扩展（示例：system=41xxx、auth=42xxx、order=51xxx）
