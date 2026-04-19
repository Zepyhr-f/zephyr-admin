import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Table, Button, Card, Form, Input, Select, Popconfirm, message, Switch, Space } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useState } from "react";
import roleService from "@/api/services/roleService";
import menuService from "@/api/services/menuService";
import { BasicStatus } from "#/enum";
import RoleModal from "./RoleModal";
import PermissionModal from "./PermissionModal";

export default function RolePage() {
	const queryClient = useQueryClient();
	const [searchForm] = Form.useForm();
	const [queryParams, setQueryParams] = useState<any>({});
	const [roleModalVisible, setRoleModalVisible] = useState(false);
	const [permissionModalVisible, setPermissionModalVisible] = useState(false);
	const [currentRole, setCurrentRole] = useState<any>(null);
	const [currentRoleMenuIds, setCurrentRoleMenuIds] = useState<string[]>([]);

	// Queries
	const { data, isLoading } = useQuery({
		queryKey: ["roles", queryParams],
		queryFn: () => roleService.list(queryParams),
	});

	const { data: menuTree = [] } = useQuery({
		queryKey: ["menuTree"],
		queryFn: () => menuService.getMenuList(),
	});

	// Mutations
	const saveMutation = useMutation({
		mutationFn: (values: any) => (values.id ? roleService.updateRole(values) : roleService.saveRole(values)),
		onSuccess: () => {
			message.success("Saved successfully");
			setRoleModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["roles"] });
		},
	});

	const deleteMutation = useMutation({
		mutationFn: (ids: string[]) => roleService.removeRole(ids),
		onSuccess: () => {
			message.success("Deleted successfully");
			queryClient.invalidateQueries({ queryKey: ["roles"] });
		},
	});

	const statusMutation = useMutation({
		mutationFn: ({ id, status }: { id: string; status: number }) => roleService.updateStatus(id, status),
		onSuccess: () => {
			message.success("Status updated");
			queryClient.invalidateQueries({ queryKey: ["roles"] });
		},
	});

	const assignMenusMutation = useMutation({
		mutationFn: ({ roleId, menuIds }: { roleId: string; menuIds: string[] }) => roleService.assignMenus(roleId, menuIds),
		onSuccess: () => {
			message.success("Permissions assigned");
			setPermissionModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["roles"] });
		},
	});

	const handleOpenPermission = async (role: any) => {
		setCurrentRole(role);
		const ids = await roleService.getMenuIds(role.id);
		setCurrentRoleMenuIds(ids);
		setPermissionModalVisible(true);
	};

	const columns: ColumnsType<any> = [
		{
			title: "Role Name",
			dataIndex: "roleName",
			key: "roleName",
		},
		{
			title: "Role Key",
			dataIndex: "roleKey",
			key: "roleKey",
		},
		{
			title: "Sort Order",
			dataIndex: "roleSort",
			key: "roleSort",
		},
		{
			title: "Status",
			dataIndex: "status",
			key: "status",
			render: (status, record) => (
				<Switch
					checked={status === BasicStatus.ENABLE}
					onChange={(checked) => statusMutation.mutate({ id: record.id, status: checked ? BasicStatus.ENABLE : BasicStatus.DISABLE })}
				/>
			),
		},
		{
			title: "Remark",
			dataIndex: "remark",
			key: "remark",
			ellipsis: true,
		},
		{
			title: "Action",
			key: "action",
			render: (_, record) => (
				<Space>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentRole(record);
							setRoleModalVisible(true);
						}}
					>
						Edit
					</Button>
					<Button type="link" size="small" onClick={() => handleOpenPermission(record)}>
						Permissions
					</Button>
					<Popconfirm title="Sure to delete?" onConfirm={() => deleteMutation.mutate([record.id])}>
						<Button type="link" size="small" danger>
							Delete
						</Button>
					</Popconfirm>
				</Space>
			),
		},
	];

	return (
		<Space direction="vertical" style={{ width: "100%" }} size="middle">
			<Card size="small">
				<Form
					form={searchForm}
					layout="inline"
					onFinish={(values) => setQueryParams(values)}
					onReset={() => {
						setQueryParams({});
						searchForm.resetFields();
					}}
				>
					<Form.Item name="roleName" label="Role Name">
						<Input placeholder="Role Name" allowClear />
					</Form.Item>
					<Form.Item name="status" label="Status">
						<Select placeholder="Status" allowClear style={{ width: 120 }}>
							<Select.Option value={BasicStatus.ENABLE}>Enable</Select.Option>
							<Select.Option value={BasicStatus.DISABLE}>Disable</Select.Option>
						</Select>
					</Form.Item>
					<Form.Item>
						<Button type="primary" htmlType="submit">
							Search
						</Button>
						<Button style={{ marginLeft: 8 }} htmlType="reset">
							Reset
						</Button>
					</Form.Item>
				</Form>
			</Card>

			<Card
				title="Role Management"
				extra={
					<Button
						type="primary"
						onClick={() => {
							setCurrentRole(null);
							setRoleModalVisible(true);
						}}
					>
						New
					</Button>
				}
			>
				<Table rowKey="id" columns={columns} dataSource={data?.records || []} pagination={{ total: data?.total || 0 }} loading={isLoading} size="small" />
			</Card>

			<RoleModal
				visible={roleModalVisible}
				title={currentRole ? "Edit Role" : "New Role"}
				initialValues={currentRole}
				onOk={(values) => saveMutation.mutate({ ...currentRole, ...values })}
				onCancel={() => setRoleModalVisible(false)}
			/>

			<PermissionModal
				visible={permissionModalVisible}
				initialMenuIds={currentRoleMenuIds}
				menuTree={menuTree}
				onOk={(menuIds) => assignMenusMutation.mutate({ roleId: currentRole.id, menuIds })}
				onCancel={() => setPermissionModalVisible(false)}
			/>
		</Space>
	);
}

