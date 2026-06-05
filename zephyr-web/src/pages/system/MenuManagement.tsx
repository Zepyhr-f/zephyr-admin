import { PlaceholderPage } from "@/components/PlaceholderPage";

export function MenuManagement() {
  return (
    <PlaceholderPage
      title="菜单管理"
      description="配置系统菜单、按钮、权限标识、路由等（RBAC 核心）。"
      tips={[
        "建议字段：name/path/component/icon/parentId/order/hidden/keepAlive/permission",
        '按钮权限：在页面内做"操作点"控制（新增/编辑/删除/导出等）'
      ]}
    />
  );
}
