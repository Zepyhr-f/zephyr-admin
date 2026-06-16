# 关键设计决策（ADR）

> ADR（Architecture Decision Record）用于记录关键架构选择的“决策原因”，避免后续反复争论与口径漂移。  
> 建议每个 ADR 一条记录，按时间递增追加。

---

## ADR-001：后端工程分层（framework / api / platform / business / gateway）

**状态**：Accepted  
**日期**：YYYY-MM-DD  

### 背景
后端需要同时满足：平台通用能力复用、甲方业务隔离、统一网关入口、跨服务调用标准化、以及团队协作下的结构可维护性。

### 决策
采用 Go 语言与 go-zero 微服务架构分层：
- `zephyr-framework/`：纯技术底座（BOM/Core/Starters），不包含业务代码
- `zephyr-api/`：跨服务调用契约（Feign + DTO），不包含实现
- `zephyr-platform/`：平台域服务（`zephyr-platform-service` + platform 模块）
- `zephyr-business/`：业务域服务（`zephyr-business-service` + business 模块）
- `zephyr-gateway/`：对外唯一入口

### 结果
- 正向：复用边界清晰、可独立部署与扩展、治理能力统一落地
- 代价：模块数量增加，需要依赖规则约束与持续维护

---

## ADR-002：不引入独立 infra 聚合层（采用经典 MVC + 模块内持久层）

**状态**：Accepted  
**日期**：YYYY-MM-DD  

### 背景
后台管理系统以 CRUD/RBAC 为主，若过早引入复杂分层（如独立 infra 聚合层）会提升理解与维护成本。

### 决策
不设独立 infra 聚合层；每个模块内部采用“参考 system 的经典分层”：
`controller / service / mapper / entity / domain(dto, vo) / convert / util / config`。

### 结果
- 正向：结构直观、上手快、实施成本低
- 风险：跨模块复用代码可能增多（需通过 starter 或公共模块治理）

