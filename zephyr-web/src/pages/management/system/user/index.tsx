import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Table, Button, Card, Form, Input, Select, Popconfirm, message, Switch, Space } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { Icon } from "@/components/icon";
import userService from "@/api/services/userService";
import deptService from "@/api/services/deptService";
import roleService from "@/api/services/roleService";
import { BasicStatus } from "#/enum";
import UserModal from "./UserModal";
import RoleAssignModal from "./RoleAssignModal";

export default function UserPage() {
	const queryClient = useQueryClient();
	const [searchForm] = Form.useForm();
	const [queryParams, setQueryParams] = useState<any>({});
	const [userModalVisible, setUserModalVisible] = useState(false);
	const [roleModalVisible, setRoleModalVisible] = useState(false);
	const [currentUser, setCurrentUser] = useState<any>(null);

	// Queries
	const { data: userList = [], isLoading } = useQuery({
		queryKey: ["users", queryParams],
		queryKeyHashFn: (queryKey) => JSON.stringify(queryKey),
		queryFn: () => userService.list(queryParams),
	});

	const { data: deptTree = [] } = useQuery({
		queryKey: ["deptTree"],
		queryFn: () => deptService.tree(),
	});

	const { data: roleList = [] } = useQuery({
		queryKey: ["roleAll"],
		queryFn: () => roleService.all(),
	});

	// Mutations
	const deleteMutation = useMutation({
		mutationFn: (ids: string[]) => userService.removeUser(ids),
		onSuccess: () => {
			message.success("Deleted successfully");
			queryClient.invalidateQueries({ queryKey: ["users"] });
		},
	});

	const statusMutation = useMutation({
		mutationFn: ({ id, status }: { id: string; status: number }) => userService.updateStatus(id, status),
		onSuccess: () => {
			message.success("Status updated");
			queryClient.invalidateQueries({ queryKey: ["users"] });
		},
	});

	const resetPwdMutation = useMutation({
		mutationFn: (id: string) => userService.resetPassword(id),
		onSuccess: () => message.success("Password reset to 123456"),
	});

	const saveMutation = useMutation({
		mutationFn: (values: any) => (values.id ? userService.updateUser(values) : userService.saveUser(values)),
		onSuccess: () => {
			message.success("Saved successfully");
			setUserModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["users"] });
		},
	});

	const assignRolesMutation = useMutation({
		mutationFn: ({ userId, roleIds }: { userId: string; roleIds: string[] }) => userService.assignRoles(userId, roleIds),
		onSuccess: () => {
			message.success("Roles assigned");
			setRoleModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["users"] });
		},
	});

	const columns: ColumnsType<any> = [
		{
			title: "Username",
			dataIndex: "username",
			key: "username",
		},
		{
			title: "Real Name",
			dataIndex: "realName",
			key: "realName",
		},
		{
			title: "Department",
			dataIndex: "deptName",
			key: "deptName",
		},
		{
			title: "Email",
			dataIndex: "email",
			key: "email",
		},
		{
			title: "Phone",
			dataIndex: "phone",
			key: "phone",
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
			title: "Action",
			key: "action",
			render: (_, record) => (
				<Space>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentUser(record);
							setUserModalVisible(true);
						}}
					>
						Edit
					</Button>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentUser(record);
							setRoleModalVisible(true);
						}}
					>
						Assign Roles
					</Button>
					<Popconfirm title="Reset password?" onConfirm={() => resetPwdMutation.mutate(record.id)}>
						<Button type="link" size="small">
							Reset Pwd
						</Button>
					</Popconfirm>
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
					<Form.Item name="username" label="Username">
						<Input placeholder="Username" allowClear />
					</Form.Item>
					<Form.Item name="phone" label="Phone">
						<Input placeholder="Phone" allowClear />
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
				title="User Management"
				extra={
					<Button
						type="primary"
						onClick={() => {
							setCurrentUser(null);
							setUserModalVisible(true);
						}}
					>
						New
					</Button>
				}
			>
				<Table rowKey="id" columns={columns} dataSource={userList} loading={isLoading} size="small" />
			</Card>

			<UserModal
				visible={userModalVisible}
				title={currentUser ? "Edit User" : "New User"}
				initialValues={currentUser}
				onOk={(values) => saveMutation.mutate({ ...currentUser, ...values })}
				onCancel={() => setUserModalVisible(false)}
				deptTree={deptTree}
				roleList={roleList}
			/>

			<RoleAssignModal
				visible={roleModalVisible}
				initialRoleIds={currentUser?.roleIds}
				allRoles={roleList}
				onOk={(roleIds) => assignRolesMutation.mutate({ userId: currentUser.id, roleIds })}
				onCancel={() => setRoleModalVisible(false)}
			/>
		</Space>
	);
}

