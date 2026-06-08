# 日志规范与 traceId

> 目的：统一日志结构与 traceId 贯穿方案，提升排障效率与审计可追溯性。

---

## 文档元信息

| 字段 | 值 |
|---|---|
| 状态 | Draft |
| Owner | Zephyr 文档维护者 |
| 最后更新 | 2026-06-08 |
| 适用范围 | 网关 / 后端 / 可观测性 |
| 关联文档 | [系统架构总览](../2.0-总览/00-系统架构总览.md)、[指标与告警](./02-指标与告警.md)、[链路追踪](./03-链路追踪.md) |

---

## 1. traceId 贯穿
- 入口：网关生成 `traceId`（若上游未提供）
- 传播：网关将 `traceId` 透传到下游服务（Header）
- 下游：写入 MDC，输出到日志

> 具体注入与 MDC 管理建议在 `zephyr-starter-web` 落地。

---

## 2. 日志级别约定
- INFO：关键业务流程节点（避免刷屏）
- WARN：可恢复异常、降级、重试
- ERROR：不可恢复异常（必须带 traceId 与关键上下文）

---

## 3. 日志字段建议（结构化）
- `traceId`
- `service` / `instanceId`
- `userCode` / `tenantCode`（脱敏或按合规）
- `path` / `method`
- `durationMs`
