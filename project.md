# 项目整体说明

本文档主要旨在说明项目的工程模块划分以及前后端基础联调的启动方式。**具体的业务场景与逻辑说明已独立抽离，请统一前往 `zephyr-doc` 目录查看。**

---

## ⚠️ AI 助手强制预读指令

**在开始处理本项目的任何开发、设计或提交任务之前，AI 助手必须首先阅读以下文件：**

👉 **[开发规范总索引](./zephyr-doc/05-开发规范/README.md)**：了解本项目的规范体系全貌，根据当前任务类型找到并阅读对应的具体规范文件，所有方案与操作必须严格对标规范执行，不得绕过。

## 1. 项目核心结构

*   **[`zephyr-doc`](/Users/zephyr/z-code/z-manager/zephyr-doc)**: 项目文档存放目录。**所有**关于具体业务逻辑、功能说明和场景流程设计的文档请进入此目录查询。
*   **[`zephyr-server`](/Users/zephyr/z-code/z-manager/zephyr-server)**: 项目后端服务代码。核心采用 Java Spring Cloud / Spring Boot 微服务架构。
*   **[`zephyr-web`](/Users/zephyr/z-code/z-manager/zephyr-web)**: 项目前端网页应用代码。基于 Ant Design Pro (React / Umi) 现代前端框架构建。

---

## 2. 统一启动与管理指南（推荐）

为了提升我们本地全栈联调的效率，我们在项目根目录提供了整套环境的**一键统一启停脚本 `z-run.sh`**。它将会自动在后台管理并拉起后端的微服务容器群与前端的 Ant Design Pro 网页开发服务器。

### 2.1 快捷全栈联调启停
日常进行需求测试和前后端联调时，请直接在项目根目录执行以下自动化命令：

```bash
# ✨ 一键拉起全体前后端服务
sh /Users/zephyr/z-code/z-manager/z-run.sh start

# 🛑 一键下线并清理所有的前后端进程
sh /Users/zephyr/z-code/z-manager/z-run.sh stop

# ♻️ 一键重启全套服务
sh /Users/zephyr/z-code/z-manager/z-run.sh restart
```

*(备注：使用此脚本时，前端的 `pnpm dev` 会在后台静默运行并将报错与日志输出至 `zephyr-web/frontend.log`。后端容器也会根据您最新的代码依赖进行并行增量热构建。)*

### 2.2 独立与单步调试补充说明
根目录下的 `z-run.sh` 是团队联机和 AI 进行全托管联调的基准核心方式。但在某些局部代码出现无法定位的异常时，您依然可以单独调试：
- **后端独立 DEBUG**：利用 IDE（如 IDEA）依次手动运行 `zephyr-common/common-boot` 及业务服务、`zephyr-gateway` 等的标准 `main` 方法，以便使用 IDE 原生断点和代码步进功能。
- **前端独立启停**：进入 `zephyr-web` 目录，控制台手工执行 `pnpm install` 及 `pnpm run dev` 在前台暴露运行情况以排查依赖树或语法问题。

---
