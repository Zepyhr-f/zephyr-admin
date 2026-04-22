# Phase 5 · 前端测试与上线

> **目标**：确保核心交互有测试保障，完成生产构建优化，配置 Nginx 静态托管与 CI/CD 自动化发布。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 5-FE-01 | 关键组件单元测试（Vitest + Testing Library）：登录表单、Permission 组件 | 🔴 P0 |
| 5-FE-02 | E2E 测试（Playwright）：登录流程、权限页面访问控制 | 🔴 P0 |
| 5-FE-03 | 生产构建：`pnpm build`，分析 Bundle（rollup-plugin-visualizer） | 🔴 P0 |
| 5-FE-04 | 编写 `nginx.conf`：静态文件服务 + API 反向代理 + gzip 压缩 | 🔴 P0 |
| 5-FE-05 | 配置 GitHub Actions 流水线：test → build → deploy | 🔴 P0 |
| 5-FE-06 | 路由懒加载验证：Chrome DevTools 网络面板确认各路由独立 chunk | 🟡 P1 |
| 5-FE-07 | 性能优化：大组件 `React.memo` + `useCallback`，避免不必要渲染 | 🟡 P1 |
| 5-FE-08 | 配置 CSP（Content Security Policy）Header | 🟢 P2 |

---

## 2. 单元测试规范

**目标覆盖率**：关键交互组件覆盖率 ≥ 70%

```ts
// LoginForm.test.tsx 示例结构
describe('LoginForm', () => {
  it('空用户名时显示校验错误')
  it('密码少于6位时显示校验错误')
  it('提交成功后跳转到 Dashboard')
  it('登录失败后显示后端错误信息')
})

// Permission.test.tsx
describe('Permission', () => {
  it('有权限时渲染子节点')
  it('无权限时渲染 null')
})
```

---

## 3. E2E 测试（Playwright）

关键场景：

```ts
// e2e/auth.spec.ts
test('完整登录流程', async ({ page }) => {
  await page.goto('/login')
  await page.fill('#username', 'admin')
  await page.fill('#password', '123456')
  await page.click('#submit-btn')
  await expect(page).toHaveURL('/dashboard')
  await expect(page.locator('.sidebar')).toBeVisible()
})

test('未登录访问受保护页面，跳转到登录页', async ({ page }) => {
  await page.goto('/system/user')
  await expect(page).toHaveURL('/login')
})

test('无权限菜单不在侧边栏中显示', async ({ page }) => {
  // 使用受限角色账号登录
  // 验证对应菜单不可见
})
```

---

## 4. 生产构建优化

**Bundle 分割策略**（`vite.config.ts`）：
```ts
build: {
  rollupOptions: {
    output: {
      manualChunks: {
        'vendor-react': ['react', 'react-dom', 'react-router'],
        'vendor-antd': ['antd', '@ant-design/cssinjs'],
        'vendor-query': ['@tanstack/react-query'],
        'vendor-charts': ['apexcharts', 'react-apexcharts'],
      },
    },
  },
}
```

**目标指标**：
| 指标 | 目标值 |
|---|---|
| 首屏 JS（gzip） | < 200KB |
| 首屏 TTI | < 3s（3G 网络） |
| Lighthouse 性能分 | ≥ 85 |

---

## 5. Nginx 配置

```nginx
server {
    listen 80;
    root /usr/share/nginx/html;
    index index.html;

    # 静态资源缓存（hash 文件名，长效缓存）
    location /assets/ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://zephyr-gateway:9000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # SPA 路由：所有非文件请求返回 index.html
    location / {
        try_files $uri $uri/ /index.html;
    }

    # gzip 压缩
    gzip on;
    gzip_types text/javascript application/javascript application/json text/css;
    gzip_min_length 1024;
}
```

---

## 6. GitHub Actions 流水线

```yaml
# .github/workflows/frontend-ci.yml
name: Frontend CI/CD

on:
  push:
    branches: [main, dev]
    paths: ['zephyr-web/**']

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: pnpm/action-setup@v4
        with: { version: 10 }
      - run: pnpm install --frozen-lockfile
        working-directory: zephyr-web
      - run: pnpm test:run        # Vitest 单元测试
      - run: pnpm test:e2e        # Playwright E2E

  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: pnpm install --frozen-lockfile
      - run: pnpm build
      - name: Deploy to Server
        run: rsync -avz dist/ user@server:/var/www/zephyr-admin/
```

---

## 7. 阶段验收标准

- [ ] `pnpm test:run` 单元测试全部通过
- [ ] `pnpm test:e2e` E2E 关键场景全部通过
- [ ] `pnpm build` 无报错，`dist/` 目录生成正常
- [ ] Bundle 分析显示各 chunk 体积符合目标
- [ ] Nginx 部署后，直接访问子路由（如 `/system/user`）不 404
- [ ] API 请求通过 Nginx 正常代理到后端
- [ ] GitHub Actions 在 `push` 后自动触发并通过
