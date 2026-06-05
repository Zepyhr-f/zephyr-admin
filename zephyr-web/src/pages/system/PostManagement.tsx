import { PlaceholderPage } from "@/components/PlaceholderPage";

export function PostManagement() {
  return (
    <PlaceholderPage
      title="岗位管理"
      description="岗位/职级配置，常与部门、角色联动；用于人员归属与审批链路。"
      tips={["支持批量导入/导出", "岗位可绑定默认角色（可选）"]}
    />
  );
}
