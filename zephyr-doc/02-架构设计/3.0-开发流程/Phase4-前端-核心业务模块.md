# Phase 4 · 前端核心业务模块

> **目标**：实现系统管理所有页面（用户/角色/菜单/部门/字典），建立标准的列表页 → 表单弹窗 → 详情页开发模式，为后续业务模块提供可复用的开发范式。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 4-FE-01 | 用户管理页：列表（分页+搜索）+ 新增/编辑弹窗 + 删除确认 | 🔴 P0 |
| 4-FE-02 | 角色管理页：列表 + 新增/编辑弹窗 + 菜单权限树分配 | 🔴 P0 |
| 4-FE-03 | 菜单管理页：树形表格展示 + 新增/编辑弹窗 + 图标选择器 | 🔴 P0 |
| 4-FE-04 | 部门管理页：树形结构 + 新增/编辑弹窗 | 🟡 P1 |
| 4-FE-05 | 字典管理页：字典类型列表 + 字典数据列表（主从布局） | 🟡 P1 |
| 4-FE-06 | Dashboard 首页：统计卡片 + 折线图/柱状图（ApexCharts） | 🟡 P1 |
| 4-FE-07 | 个人中心：当前用户信息展示 + 修改密码 + 头像上传 | 🟢 P2 |
| 4-FE-08 | 多标签页（Tab Bar）：路由变化时自动新增标签，支持关闭/刷新 | 🟢 P2 |

---

## 2. 页面开发标准范式

每个业务页面遵循以下固定结构，提升可维护性：

```
pages/system/user/
├── index.tsx              ← 页面主组件（列表页）
├── components/
│   ├── UserSearchBar.tsx  ← 搜索表单区域
│   ├── UserTable.tsx      ← 表格组件
│   └── UserFormModal.tsx  ← 新增/编辑弹窗
├── hooks/
│   ├── use-user-list.ts   ← 封装列表查询（useQuery）
│   └── use-user-form.ts   ← 封装表单提交（useMutation）
└── types.ts               ← 页面私有类型定义
```

**开发顺序**：搜索表单 → 列表表格 → 新增弹窗 → 编辑弹窗 → 删除确认 → 权限控制

---

## 3. 列表页标准结构

```tsx
// pages/system/user/index.tsx 骨架
<>
  <UserSearchBar onSearch={setParams} />   {/* 搜索区 */}

  <div className="toolbar">                 {/* 操作按钮区 */}
    <Permission code="sys:user:create">
      <Button onClick={() => setModalOpen(true)}>新增</Button>
    </Permission>
  </div>

  <UserTable                               {/* 表格区 */}
    data={userList}
    loading={isLoading}
    onEdit={handleEdit}
    onDelete={handleDelete}
  />

  <Pagination ... />                       {/* 分页区 */}

  <UserFormModal                           {/* 弹窗区 */}
    open={modalOpen}
    editId={editId}
    onSuccess={() => invalidateQueries(['user'])}
  />
</>
```

---

## 4. 表单弹窗设计规范

- 使用 `react-hook-form` + `zod` 做表单验证
- 新增和编辑共用同一个弹窗组件，通过 `editId` 是否有值区分
- 弹窗打开时，若是编辑模式，通过 `useQuery(['user', editId])` 查询详情并 `reset()` 填入表单
- 提交成功后调用 `onSuccess`，触发列表刷新（`queryClient.invalidateQueries`）

---

## 5. 数据查询 Hook 规范

```ts
// hooks/use-user-list.ts
export const useUserList = (params: UserListParams) => {
  return useQuery({
    queryKey: ['user', params],
    queryFn: () => getUserList(params),
    staleTime: 30_000,  // 30秒内不重新请求
  })
}

// hooks/use-user-form.ts
export const useCreateUser = () => {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: createUser,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['user'] }),
  })
}
```

---

## 6. 常见 UI 模式

### 树形菜单权限分配（角色管理）

- 使用 Ant Design `Tree` 组件，`checkable` 模式
- 初始 `checkedKeys` 来自 `GET /api/system/roles/{id}/menus`
- 提交时将选中的 `menuIds` 数组 `PUT /api/system/roles/{id}/menus`

### 字典数据展示（DictTag 组件）

状态类字段通过字典 code 渲染标签，避免硬编码：
```tsx
<DictTag dictType="sys_user_status" value={row.status} />
// 自动从字典缓存中查找 label 并渲染对应颜色的 Tag
```

### 图标选择器（菜单管理）

- 基于 Iconify 图标库，提供搜索 + 分类浏览
- 选中后存储图标 name（如 `mdi:user`）
- 渲染时使用 `<Icon icon={menu.icon} />`

---

## 7. 阶段验收标准

- [ ] 用户管理：新增/编辑/删除/搜索/分页均正常
- [ ] 角色管理：权限树分配后，权限立即生效（前端 Store 更新）
- [ ] 菜单管理：新增菜单后，侧边栏自动出现新菜单项（需重新登录）
- [ ] 所有按钮级别权限控制正确（无权限时按钮不可见）
- [ ] 表单验证错误信息展示符合设计规范
- [ ] 列表加载状态（Skeleton / Spin）体验正常
- [ ] Dashboard 图表数据正确渲染
