import { PlaceholderPage } from "@/components/PlaceholderPage";

export function DepartmentManagement() {
  return (
    <PlaceholderPage
      title="部门管理"
      description="树形组织结构（公司/部门/小组），支持拖拽排序、成员统计、数据范围继承。"
      tips={[
        "左侧：组织树；右侧：部门信息与成员列表",
        "部门变更需要审计日志（谁在何时调整了结构）"
      ]}
    />
  );
}
