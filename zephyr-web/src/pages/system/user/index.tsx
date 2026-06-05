import { useState } from 'react';
import { Card, Table, Button, Input, Space, Tag, Modal, Form, Select, message } from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';

interface User {
  id: string;
  username: string;
  nickname: string;
  email: string;
  phone: string;
  status: 'active' | 'inactive';
  dept: string;
  createTime: string;
}

export default function SystemUserPage() {
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [editingUser, setEditingUser] = useState<User | null>(null);

  // 模拟数据
  const data: User[] = [
    {
      id: '1',
      username: 'admin',
      nickname: '管理员',
      email: 'admin@zephyr.com',
      phone: '13800138000',
      status: 'active',
      dept: '技术部',
      createTime: '2024-01-15'
    },
    {
      id: '2',
      username: 'zhangsan',
      nickname: '张三',
      email: 'zhangsan@zephyr.com',
      phone: '13800138001',
      status: 'active',
      dept: '产品部',
      createTime: '2024-02-20'
    }
  ];

  const columns = [
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: '昵称',
      dataIndex: 'nickname',
      key: 'nickname',
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: '手机',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: '部门',
      dataIndex: 'dept',
      key: 'dept',
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={status === 'active' ? 'success' : 'default'}>
          {status === 'active' ? '启用' : '禁用'}
        </Tag>
      ),
    },
    {
      title: '操作',
      key: 'action',
      render: (_: any, record: User) => (
        <Space>
          <Button
            type="text"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          <Button
            type="text"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record)}
          >
            删除
          </Button>
        </Space>
      ),
    },
  ];

  const handleAdd = () => {
    setEditingUser(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (record: User) => {
    setEditingUser(record);
    form.setFieldsValue(record);
    setModalVisible(true);
  };

  const handleDelete = (record: User) => {
    Modal.confirm({
      title: '确认删除',
      content: `确定要删除用户 "${record.username}" 吗？`,
      onOk() {
        message.success('删除成功');
      },
    });
  };

  const handleModalOk = async () => {
    try {
      const values = await form.validateFields();
      console.log('Form values:', values);
      message.success(editingUser ? '更新成功' : '创建成功');
      setModalVisible(false);
    } catch (error) {
      console.error('Validate failed:', error);
    }
  };

  return (
    <div className="system-user-page">
      <Card
        title="用户管理"
        extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增用户
          </Button>
        }
      >
        <div className="mb-4">
          <Input.Search
            placeholder="搜索用户名/昵称/邮箱"
            allowClear
            enterButton={<><SearchOutlined /> 搜索</>}
            style={{ width: 400 }}
          />
        </div>
        <Table
          columns={columns}
          dataSource={data}
          rowKey="id"
          loading={loading}
          pagination={{
            total: data.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total) => `共 ${total} 条`,
          }}
        />
      </Card>

      <Modal
        title={editingUser ? '编辑用户' : '新增用户'}
        open={modalVisible}
        onOk={handleModalOk}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
        >
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            name="nickname"
            label="昵称"
            rules={[{ required: true, message: '请输入昵称' }]}
          >
            <Input placeholder="请输入昵称" />
          </Form.Item>
          <Form.Item
            name="email"
            label="邮箱"
            rules={[
              { required: true, message: '请输入邮箱' },
              { type: 'email', message: '请输入有效的邮箱地址' }
            ]}
          >
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item
            name="phone"
            label="手机号"
          >
            <Input placeholder="请输入手机号" />
          </Form.Item>
          <Form.Item
            name="dept"
            label="部门"
          >
            <Select placeholder="请选择部门">
              <Select.Option value="技术部">技术部</Select.Option>
              <Select.Option value="产品部">产品部</Select.Option>
              <Select.Option value="运营部">运营部</Select.Option>
              <Select.Option value="财务部">财务部</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="status"
            label="状态"
            initialValue="active"
          >
            <Select>
              <Select.Option value="active">启用</Select.Option>
              <Select.Option value="inactive">禁用</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
</tool>

工具调用已完成，我正在继续创建其他页面文件。我需要创建系统管理模块的其他页面，包括部门管理、岗位管理、菜单管理和角色管理。每个页面都需要包含基本的表格展示、搜索功能和操作按钮。我会按照功能设计文档的要求来实现这些页面。部门管理页面需要展示部门树形结构，岗位管理页面需要展示岗位列表，菜单管理页面需要展示菜单树，角色管理页面需要展示角色列表并支持权限分配。这些页面都会遵循统一的UI设计风格，使用Ant Design组件库，并保持与整体设计系统的一致性。我会继续完成这些页面的创建，确保它们都能正常运行并提供良好的用户体验。我需要为菜单系统创建完整的页面结构。目前只有用户管理页面，还需要添加部门管理、岗位管理、菜单管理和角色管理页面。每个页面都应该有基本的布局和功能。让我继续创建这些缺失的页面组件，确保整个系统管理模块的完整性。这些页面应该遵循统一的样式和功能模式，提供一致的用户体验。我将按照设计文档的要求，为每个功能模块创建相应的管理页面。</think>我来继续创建其他页面。<|tool_calls_section_begin|><|tool_call_begin|>functions.Write:69<|tool_call_argument_begin|>{