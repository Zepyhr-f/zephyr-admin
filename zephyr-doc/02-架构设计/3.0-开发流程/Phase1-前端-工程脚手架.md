# Phase 1 · 前端工程脚手架

> **目标**：建立清晰的目录分层与设计系统基础，让后续所有页面开发在统一的视觉语言和工程规范下进行。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 1-FE-01 | 确认 `src/` 目录结构，建立各层目录并写入 `.gitkeep` | 🔴 P0 |
| 1-FE-02 | 配置 Vite 路径别名（`@/`、`@api/`、`@comp/` 等） | 🔴 P0 |
| 1-FE-03 | 配置 `tsconfig.json` 路径映射，与 Vite 别名保持同步 | 🔴 P0 |
| 1-FE-04 | 在 `theme/css-variables.css` 定义全局 CSS 变量（颜色 / 间距 / 圆角 / 阴影） | 🔴 P0 |
| 1-FE-05 | 在 `theme/antd-token.ts` 配置 Ant Design Design Token 覆盖 | 🔴 P0 |
| 1-FE-06 | 配置 `tailwind.config.ts`，将 CSS 变量映射为 Tailwind 工具类 | 🟡 P1 |
| 1-FE-07 | 建立 `global.css`：CSS Reset + 基础排版 + 滚动条美化 | 🟡 P1 |
| 1-FE-08 | 建立 `layouts/admin-layout/`：侧边栏 + 顶栏 + 内容区骨架 | 🟡 P1 |
| 1-FE-09 | 配置暗黑模式切换（`useThemeStore` + `data-theme` 切换） | 🟢 P2 |
| 1-FE-10 | 配置 i18n（中英文 JSON 语言包 + `react-i18next` 初始化） | 🟢 P2 |

---

## 2. 目录结构产出

```
src/
├── api/                    ← 接口层（Phase 2 填充）
├── assets/                 ← 静态资源
├── components/             ← 全局业务组件
├── hooks/                  ← 全局自定义 Hook
├── layouts/
│   ├── admin-layout/       ← 后台主布局骨架（本阶段完成）
│   ├── auth-layout/        ← 登录页布局（Phase 3 完成）
│   └── blank-layout/       ← 空白布局
├── locales/                ← i18n 语言包
│   ├── zh-CN.json
│   └── en-US.json
├── pages/                  ← 页面（Phase 3+ 填充）
├── routes/                 ← 路由（Phase 3 填充）
├── store/                  ← Zustand Store
│   └── use-theme-store.ts  ← 本阶段建立
├── theme/
│   ├── css-variables.css   ← 全局 CSS 变量（本阶段核心）
│   ├── antd-token.ts       ← Ant Design Token 覆盖
│   └── dark-token.ts       ← 暗黑模式 Token
├── types/                  ← 全局类型
│   ├── api.d.ts            ← R<T> 等接口通用类型
│   └── global.d.ts         ← 全局声明
├── ui/                     ← UI 原子组件
└── utils/                  ← 工具函数
```

---

## 3. 设计系统核心 Token

### 颜色系统

| Token | CSS 变量 | 默认值（亮色） |
|---|---|---|
| 主色 | `--color-primary` | `hsl(220, 94%, 56%)` |
| 成功 | `--color-success` | `hsl(150, 70%, 48%)` |
| 警告 | `--color-warning` | `hsl(45, 100%, 51%)` |
| 危险 | `--color-danger` | `hsl(0, 78%, 55%)` |
| 背景 | `--color-bg-base` | `hsl(0, 0%, 98%)` |
| 文字 | `--color-text-primary` | `hsl(0, 0%, 10%)` |

### 间距系统（4px 基准）

```
--spacing-1: 4px    --spacing-4: 16px   --spacing-7: 48px
--spacing-2: 8px    --spacing-5: 24px   --spacing-8: 64px
--spacing-3: 12px   --spacing-6: 32px
```

### 圆角 & 阴影

```css
--radius-sm: 4px;
--radius-md: 8px;
--radius-lg: 12px;
--shadow-card: 0 1px 3px rgba(0,0,0,.08), 0 4px 16px rgba(0,0,0,.04);
```

---

## 4. Ant Design Token 覆盖策略

在 `theme/antd-token.ts` 中配置 `ConfigProvider`，将 Ant Design 组件主题与 CSS 变量保持同步：

```ts
// 核心覆盖字段
{
  colorPrimary: 'hsl(220, 94%, 56%)',
  borderRadius: 8,
  colorBgContainer: 'var(--color-bg-container)',
  fontFamily: '"Inter Variable", system-ui, sans-serif',
}
```

---

## 5. 路径别名规范

| 别名 | 映射路径 | 使用场景 |
|---|---|---|
| `@/` | `src/` | 通用，可跨所有目录使用 |
| `@api/` | `src/api/` | 接口请求函数 |
| `@comp/` | `src/components/` | 全局业务组件 |
| `@store/` | `src/store/` | Zustand Store |
| `@theme/` | `src/theme/` | 主题配置 |

---

## 6. 阶段验收标准

- [ ] `src/` 目录结构完整建立，各子目录符合规范
- [ ] `pnpm dev` 启动后页面能看到 `AdminLayout` 骨架（带侧边栏轮廓）
- [ ] `--color-primary` CSS 变量生效，修改值后页面颜色同步变化
- [ ] 暗黑模式切换正常（点击切换按钮，页面配色切换）
- [ ] 中英文 i18n 切换正常
- [ ] 路径别名 `@/` 在 IDEA/VSCode 中均有类型提示与跳转
