

### 如何新增菜单 (How to Add a New Menu - 数据库驱动)

项目现已支持通过数据库 `sys_menu` 表动态配置菜单和路由。这种方式无需修改前端代码，即可实现菜单的增删改查。

#### 1. 了解 sys_menu 表结构
在数据库中找到 `sys_menu` 表，其关键字段说明如下：
- `menu_name`: 菜单名称（支持国际化 Key，如 `sys.nav.user.index`）。
- `menu_type`: 类型（`M`=目录/分组，`C`=菜单，`F`=按钮）。
- `path`: 路由地址（如 `/management/user`）。
- `component`: 组件路径（如 `/pages/management/user/index`）。
- `icon`: 图标名称（支持 Iconify 图标，如 `solar:user-bold`）。
- `parent_id`: 父菜单 ID（顶级菜单为 0）。

#### 2. SQL 配置示例
执行以下 SQL 语句即可新增一个菜单项：

```sql
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `order_num`)
VALUES (1000, 0, '示例页面', 'C', '/demo', '/pages/demo/index', 'solar:widget-5-bold-duotone', 1);
```

#### 3. 前端生效机制
1. **自动渲染**：前端会自动获取后端菜单数据，并通过 `src/routes/sections/dashboard/backend.tsx` 动态生成路由。
2. **路由模式**：确保 `src/global-config.ts` 中的 `routerMode` 设置为 `backend`。
3. **国际化**：如果 `menu_name` 使用了 Key（如 `sys.nav.demo`），请在 `src/locales/lang/zh_CN/sys.json` 中添加对应的翻译。
