# 项目整体说明

本文档主要旨在说明项目的工程模块划分以及前后端基础联调的启动方式。**具体的业务场景与逻辑说明已独立抽离，请统一前往 `zephyr-doc` 目录查看。**

---

## ⚠️ AI 助手强制预读指令

**在开始处理本项目的任何开发、设计或提交任务之前，AI 助手必须首先阅读以下文件：**

👉 **[开发规范总索引](./zephyr-doc/05-开发规范/README.md)**：了解本项目的规范体系全貌。所有方案与操作必须严格对标规范执行（特别是 **[代码提交流程 SOP](./zephyr-doc/05-开发规范/03-其他规范/01-代码提交与发版规范.md)**）。

## 1. 项目核心结构

*   **[`zephyr-doc`](./zephyr-doc)**: 项目文档存放目录。**所有**关于具体业务逻辑、功能说明和场景流程设计的文档请进入此目录查询。
*   **[`zephyr-server`](./zephyr-server)**: 项目后端服务代码。核心采用 Java Spring Cloud / Spring Boot 微服务架构。
*   **[`zephyr-web`](./zephyr-web)**: 项目前端网页应用代码。基于 Ant Design Pro (React / Vite) 现代前端框架构建。

---

## 2. 启动与管理指南

为了提高开发灵活性，项目在根目录提供了解耦的启动脚本，方便独立管理前端和后端服务。

### 2.1 环境准备
在运行启动脚本前，请确保本地环境已安装以下工具：
- **Docker & Docker Compose**: 用于拉起后端微服务容器群。
- **Node.js (v20+) & pnpm (v10+)**: 用于前端 Vite 服务器运行。
- **Java 17**: 用于本地后端调试。

### 2.2 解耦启动方案 (推荐)
通过独立脚本管理各端服务，可以避免在只修改一端代码时重启整个全栈环境。

| 目标服务 | 启动命令 | 停止命令 | 重启命令 | 日志查看 |
| :--- | :--- | :--- | :--- | :--- |
| **前端 (Web)** | `sh z-web.sh start` | `sh z-web.sh stop` | `sh z-web.sh restart` | `tail -f logs/frontend.log` |
| **后端 (Server)** | `sh z-server.sh start` | `sh z-server.sh stop` | `sh z-server.sh restart` | `tail -f logs/backend.log` |

> [!TIP]
> 所有的日志文件均保存在根目录的 `logs/` 文件夹下。

---

## 3. 开发规范与提交

作为项目成员，你必须严格遵守 **[代码提交 SOP](./zephyr-doc/05-开发规范/03-其他规范/01-代码提交与发版规范.md)**。
每次提交前，必须确保修改后的端（Web 或 Server）在本地运行无误，并完成必要的 Lint 和单元测试。

---

## 4. 故障排查
- **前端启动失败**：检查 `logs/frontend.log`，确认依赖已安装（`pnpm install`）。
- **后端容器无法拉起**：检查 `logs/backend.log`，确认 Docker 引擎已启动且 8080 等核心端口未被占用。

---
