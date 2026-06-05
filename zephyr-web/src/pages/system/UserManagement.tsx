import { useMemo, useState } from "react";
import {
  Button,
  Card,
  Form,
  Input,
  Modal,
  Select,
  Space,
  Table,
  Tag
} from "antd";
import type { ColumnsType } from "antd/es/table";
import { PageShell } from "@/components/PageShell";

type UserRow = {
  id: string;
  username: string;
  name: string;
  dept: string;
  role: string;
  status: "正常" | "禁用";
};

const mockUsers: UserRow[] = [
  {
    id: "u_1001",
    username: "admin",
    name: "系统管理员",
    dept: "总部/IT",
    role: "管理员",
    status: "正常"
  },
  {
    id: "u_1002",
    username: "auditor",
    name: "审计专员",
    dept: "总部/合规",
    role: "审计员",
    status: "正常"
  }
];

export function UserManagement() {
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();

  const columns: ColumnsType<UserRow> = useMemo(
    () => [
      { title: "账号", dataIndex: "username", width: 160 },
      { title: "姓名", dataIndex: "name", width: 160 },
      { title: "部门", dataIndex: "dept" },
      { title: "角色", dataIndex: "role", width: 140 },
      {
        title: "状态",
        dataIndex: "status",
        width: 120,
        render: (v) =>
          v === "正常" ? <Tag color="green">正常</Tag> : <Tag color="red">禁用</Tag>
      },
      {
        title: "操作",
        key: "actions",
        width: 180,
        render: () => (
          <Space>
            <Button type="link">编辑</Button>
            <Button type="link" danger>
              禁用
            </Button>
          </Space>
        )
      }
    ],
    []
  );

  return (
    <PageShell
      title="用户管理"
      description="账号维护、分配角色；典型模式：筛选区 + 表格 + 抽屉/弹窗编辑。"
      extra={
        <Space>
          <Button onClick={() => form.resetFields()}>重置</Button>
          <Button type="primary" onClick={() => setOpen(true)}>
            新增用户
          </Button>
        </Space>
      }
    >
      <Card>
        <Form layout="inline" style={{ marginBottom: 12 }}>
          <Form.Item label="关键词">
            <Input placeholder="账号/姓名" allowClear style={{ width: 220 }} />
          </Form.Item>
          <Form.Item label="部门">
            <Select
              allowClear
              placeholder="选择部门"
              style={{ width: 200 }}
              options={[
                { label: "总部/IT", value: "总部/IT" },
                { label: "总部/合规", value: "总部/合规" }
              ]}
            />
          </Form.Item>
          <Form.Item label="状态">
            <Select
              allowClear
              placeholder="全部"
              style={{ width: 140 }}
              options={[
                { label: "正常", value: "正常" },
                { label: "禁用", value: "禁用" }
              ]}
            />
          </Form.Item>
          <Form.Item>
            <Button type="primary">查询</Button>
          </Form.Item>
        </Form>

        <Table
          rowKey="id"
          size="middle"
          columns={columns}
          dataSource={mockUsers}
          pagination={{ pageSize: 10, showSizeChanger: true }}
        />
      </Card>

      <Modal
        title="新增用户"
        open={open}
        onCancel={() => setOpen(false)}
        onOk={() => setOpen(false)}
        okText="保存"
      >
        <Form form={form} layout="vertical" requiredMark="optional">
          <Form.Item
            name="username"
            label="账号"
            rules={[{ required: true, message: "请输入账号" }]}
          >
            <Input placeholder="如：admin01" autoComplete="off" />
          </Form.Item>
          <Form.Item
            name="name"
            label="姓名"
            rules={[{ required: true, message: "请输入姓名" }]}
          >
            <Input placeholder="如：张三" />
          </Form.Item>
          <Form.Item name="dept" label="部门" rules={[{ required: true }]}>
            <Select
              placeholder="选择部门"
              options={[
                { label: "总部/IT", value: "总部/IT" },
                { label: "总部/合规", value: "总部/合规" }
              ]}
            />
          </Form.Item>
          <Form.Item name="role" label="角色" rules={[{ required: true }]}>
            <Select
              placeholder="选择角色"
              options={[
                { label: "管理员", value: "管理员" },
                { label: "审计员", value: "审计员" }
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>
    </PageShell>
  );
}
