import { Button, Card, Space, Table, Tag } from "antd";
import type { ColumnsType } from "antd/es/table";
import { PageShell } from "@/components/PageShell";

type RoleRow = {
  id: string;
  name: string;
  key: string;
  dataScope: "全部" | "本部门" | "本部门及以下" | "仅本人";
  status: "启用" | "停用";
};

const data: RoleRow[] = [
  { id: "r1", name: "管理员", key: "admin", dataScope: "全部", status: "启用" },
  { id: "r2", name: "审计员", key: "auditor", dataScope: "本部门", status: "启用" }
];

const columns: ColumnsType<RoleRow> = [
  { title: "角色名称", dataIndex: "name", width: 180 },
  { title: "标识", dataIndex: "key", width: 160 },
  { title: "数据范围", dataIndex: "dataScope" },
  {
    title: "状态",
    dataIndex: "status",
    width: 120,
    render: (v) => (v === "启用" ? <Tag color="green">启用</Tag> : <Tag>停用</Tag>)
  },
  {
    title: "操作",
    key: "actions",
    width: 220,
    render: () => (
      <Space>
        <Button type="link">权限分配</Button>
        <Button type="link">编辑</Button>
        <Button type="link" danger>
          删除
        </Button>
      </Space>
    )
  }
];

export function RoleManagement() {
  return (
    <PageShell
      title="角色管理"
      description="关键能力：菜单/按钮级权限 + 数据范围（Data Scope）。"
      extra={
        <Space>
          <Button>导出</Button>
          <Button type="primary">新增角色</Button>
        </Space>
      }
    >
      <Card>
        <Table
          rowKey="id"
          columns={columns}
          dataSource={data}
          pagination={{ pageSize: 10 }}
        />
      </Card>
    </PageShell>
  );
}
