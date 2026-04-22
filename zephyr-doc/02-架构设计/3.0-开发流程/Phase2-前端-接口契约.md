# Phase 2 · 前端接口契约

> **目标**：基于后端输出的 OpenAPI 文档，完成前端 HTTP 层封装和 MSW Mock 体系搭建，让前端在后端接口未完成前即可独立开发所有页面。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 2-FE-01 | 封装 Axios 实例：`baseURL`、超时、请求/响应拦截器 | 🔴 P0 |
| 2-FE-02 | 响应拦截器：统一解包 `R<T>`，错误自动弹 Toast | 🔴 P0 |
| 2-FE-03 | 请求拦截器：自动注入 `Authorization: Bearer <token>` | 🔴 P0 |
| 2-FE-04 | 定义 `types/api.d.ts`：`R<T>`、`PageResult<T>`、`PageQuery` 等通用类型 | 🔴 P0 |
| 2-FE-05 | 建立 `api/` 目录结构，按业务域拆分文件 | 🔴 P0 |
| 2-FE-06 | 从 OpenAPI JSON 导入至 Apifox / Postman，供接口文档查阅 | 🟡 P1 |
| 2-FE-07 | 编写 MSW Handler，覆盖所有核心接口 Mock | 🟡 P1 |
| 2-FE-08 | 建立 TanStack Query `QueryKey` 命名规范与 `QueryClient` 配置 | 🟡 P1 |
| 2-FE-09 | 配置 401 自动跳登录页（响应拦截 + Zustand logout） | 🟢 P2 |

---

## 2. Axios 实例设计

```
src/api/
├── client.ts           ← Axios 实例（拦截器在此配置）
├── index.ts            ← 统一导出入口
├── auth/               ← 认证接口（登录、刷新、登出、用户信息）
├── system/             ← 系统管理接口
│   ├── user.ts
│   ├── role.ts
│   ├── menu.ts
│   ├── dept.ts
│   └── dict.ts
└── biz/                ← 业务模块接口（Phase 4+ 填充）
```

---

## 3. 通用类型定义（`types/api.d.ts`）

后端 `R<T>` 对应前端类型：

```ts
// 统一响应体
interface R<T = unknown> {
  code: number
  success: boolean
  msg: string
  data: T
}

// 分页请求
interface PageQuery {
  page: number
  size: number
  sortField?: string
  sortOrder?: 'ASC' | 'DESC'
}

// 分页结果
interface PageResult<T> {
  records: T[]
  total: number
  page: number
  size: number
}
```

---

## 4. 响应拦截器逻辑

```
响应到达
  └─ HTTP 状态码 2xx？
       ├─ 是 → 取 response.data（即 R<T>）
       │         └─ R.success === true？
       │              ├─ 是 → 返回 R.data（直接透传业务数据）
       │              └─ 否 → toast.error(R.msg)，throw Error
       └─ 否 → HTTP 401 → useAuthStore.logout() + 跳转 /login
              → HTTP 其他 → toast.error('网络错误')，throw Error
```

---

## 5. TanStack Query 规范

**QueryKey 命名规范**：

| 场景 | Key 格式 | 示例 |
|---|---|---|
| 列表（含过滤条件） | `['entity', params]` | `['user', { page:1, size:20 }]` |
| 单条详情 | `['entity', id]` | `['user', 123]` |
| 全量数据（无分页） | `['entity', 'all']` | `['role', 'all']` |

**全局 QueryClient 配置**：

```ts
// 关键配置（src/main.tsx）
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,      // 5分钟内不重新请求
      retry: 1,                        // 失败重试1次
      refetchOnWindowFocus: false,     // 切换标签不自动刷新
    },
  },
})
```

---

## 6. MSW Handler 编写规范

Handler 文件按业务域组织，与 `api/` 目录一一对应：

```
src/_mock/
├── index.ts            ← 汇总所有 Handler，setupWorker 初始化
├── auth.ts             ← /auth/** 接口 Mock
├── system/
│   ├── user.ts         ← /api/system/users/** Mock
│   ├── role.ts
│   └── menu.ts
└── data/               ← Mock 数据工厂（@faker-js/faker 生成）
    └── user-factory.ts
```

Mock 数据格式必须与后端 `R<T>` 完全一致：

```ts
// 示例：用户列表 Mock
http.get('/api/system/users', () => {
  return HttpResponse.json({
    code: 200,
    success: true,
    msg: '操作成功',
    data: {
      records: Array.from({ length: 10 }, generateFakeUser),
      total: 100,
      page: 1,
      size: 20,
    },
  })
})
```

---

## 7. 阶段验收标准

- [ ] `VITE_ENABLE_MOCK=true` 时，`GET /api/system/users` 返回正确 Mock 数据
- [ ] `VITE_ENABLE_MOCK=false` 时，请求能正确带上 Token 发往后端
- [ ] 401 响应自动触发登出并跳转登录页
- [ ] `useUserList()` Hook 可在 React DevTools 中看到正确缓存状态
- [ ] 所有核心接口 MSW Handler 编写完成（认证 + 系统模块）
