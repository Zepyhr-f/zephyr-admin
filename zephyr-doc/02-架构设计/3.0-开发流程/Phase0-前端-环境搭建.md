# Phase 0 · 前端环境搭建

> **目标**：前端开发者在本机以完全一致的方式启动 Zephyr Admin 前端项目，从拉代码到看到登录页不超过 10 分钟。

---

## 1. 必须安装的工具

| 工具 | 版本要求 | 安装方式 |
|---|---|---|
| **Node.js** | 20 LTS | 通过 `nvm` 管理：`nvm install 20` |
| **pnpm** | 10.x | `npm install -g pnpm` |
| **Git** | 任意新版 | macOS 自带或 Homebrew |
| **VS Code** | 最新版 | [code.visualstudio.com](https://code.visualstudio.com) |

### 必装 VS Code 插件

| 插件 | 用途 |
|---|---|
| **Biome** | 格式化 + Lint，替代 ESLint/Prettier |
| **Tailwind CSS IntelliSense** | TailwindCSS 类名提示 |
| **TypeScript** | TS 类型检查增强 |
| **Error Lens** | 行内错误提示 |
| **GitLens** | Git 历史与 Blame 可视化 |

---

## 2. 版本锁定

**Node 版本**：项目根目录 `.nvmrc` 文件写入 `20`，`nvm use` 自动切换。

**pnpm 版本**（`package.json`）：
```json
{
  "packageManager": "pnpm@10.8.0",
  "engines": { "node": "20.*" }
}
```

---

## 3. 项目初始化

```bash
git clone https://github.com/your-org/zephyr-admin.git
cd zephyr-admin/zephyr-web

# 切换 Node 版本
nvm use

# 安装依赖（精确还原 pnpm-lock.yaml）
pnpm install

# 配置本地环境变量
cp .env.example .env.local
# 编辑 .env.local，设置 VITE_API_BASE_URL 和 VITE_ENABLE_MOCK=true

# 启动开发服务器
pnpm dev
```

---

## 4. MSW Mock 开关

前端开发**不依赖后端启动**，通过 MSW（Mock Service Worker）在浏览器层拦截请求：

```bash
# .env.local
VITE_API_BASE_URL=http://localhost:9000
VITE_ENABLE_MOCK=true    # ← true = 纯 Mock，false = 请求真实后端
```

MSW 工作原理：
```
浏览器发起 API 请求
    ↓
Service Worker 拦截（浏览器层，不经过网络）
    ↓
MSW Handler 返回 Mock 数据（与真实接口格式完全一致）
```

---

## 5. 环境变量文件规范

| 文件 | 作用 | 是否提交 Git |
|---|---|---|
| `.env` | 通用默认值 | ✅ 提交 |
| `.env.development` | 开发环境专用 | ✅ 提交 |
| `.env.production` | 生产环境专用 | ✅ 提交 |
| `.env.local` | 个人本地覆盖（优先级最高） | ❌ 不提交 |

---

## 6. Git Hooks

`pnpm install` 时 `preinstall` 脚本自动执行 `lefthook install`：

| Hook | 触发 | 内容 |
|---|---|---|
| `pre-commit` | `git commit` 前 | Biome lint 检查，失败则阻止提交 |
| `commit-msg` | 提交信息后 | Commitlint 格式校验 |

---

## 7. 验收标准

- [ ] `pnpm dev` 启动无报错
- [ ] 浏览器访问 `http://localhost:5173` 看到登录页
- [ ] 使用 Mock 账号登录成功，进入 Dashboard
- [ ] 控制台无 TS 错误，无 Biome lint 错误
- [ ] `git commit` 时 Hooks 正常触发
