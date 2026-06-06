import { EditOutlined, DeleteOutlined, KeyOutlined } from "@ant-design/icons";
import { Button, Form, Input, Select, Space, Tag, Tooltip } from "antd";
import type { ColumnsType } from "antd/es/table";
import { PageShell } from "@/components/PageShell";
import { QueryForm } from "@/components/QueryForm";
import { DataTable } from "@/components/DataTable";

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
      <Space size={12}>
        <Tooltip title="权限分配">
          <Button shape="circle" icon={<KeyOutlined />} className="btn-action-edit" />
        </Tooltip>
        <Tooltip title="编辑角色">
          <Button shape="circle" icon={<EditOutlined />} className="btn-action-edit" />
        </Tooltip>
        <Tooltip title="删除角色">
          <Button shape="circle" icon={<DeleteOutlined />} className="btn-action-delete" />
        </Tooltip>
      </Space>
    )
  }
];

export function RoleManagement() {
  const [form] = Form.useForm();

  return (
    <PageShell
      title="角色管理"
      description="关键能力：菜单/按钮级权限 + 数据范围（Data Scope）。"
    >
      <QueryForm
        form={form}
        onSearch={(values) => console.log("查询:", values)}
      >
        <Form.Item label="角色名称" name="name">
          <Input placeholder="输入角色名称" allowClear style={{ width: 200 }} />
        </Form.Item>
        <Form.Item label="角色标识" name="key">
          <Input placeholder="输入角色标识" allowClear style={{ width: 200 }} />
        </Form.Item>
        <Form.Item label="状态" name="status">
          <Select
            allowClear
            placeholder="全部"
            style={{ width: 200 }}
            options={[
              { label: "启用", value: "启用" },
              { label: "停用", value: "停用" }
            ]}
          />
        </Form.Item>
      </QueryForm>

      <DataTable
        rowKey="id"
        columns={columns}
        dataSource={data}
        pagination={{ pageSize: 10, total: 50 }}
        extraActions={
          <Space>
            <Button>导出</Button>
            <Button type="primary">新增角色</Button>
          </Space>
        }
      />
    </PageShell>
  );
}
