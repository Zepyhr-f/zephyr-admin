import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Table, Button, Card, Popconfirm, message, Space, Tag } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useState } from "react";
import deptService from "@/api/services/deptService";
import { BasicStatus } from "#/enum";
import DeptModal from "./DeptModal";

export default function DeptPage() {
	const queryClient = useQueryClient();
	const [deptModalVisible, setDeptModalVisible] = useState(false);
	const [currentDept, setCurrentDept] = useState<any>(null);

	// Queries
	const { data: deptTree = [], isLoading } = useQuery({
		queryKey: ["deptTree"],
		queryFn: () => deptService.tree(),
	});

	// Mutations
	const saveMutation = useMutation({
		mutationFn: (values: any) => (values.id ? deptService.updateDept(values) : deptService.saveDept(values)),
		onSuccess: () => {
			message.success("Saved successfully");
			setDeptModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["deptTree"] });
		},
	});

	const deleteMutation = useMutation({
		mutationFn: (ids: string[]) => deptService.removeDept(ids),
		onSuccess: () => {
			message.success("Deleted successfully");
			queryClient.invalidateQueries({ queryKey: ["deptTree"] });
		},
	});

	const columns: ColumnsType<any> = [
		{
			title: "Department Name",
			dataIndex: "deptName",
			key: "deptName",
		},
		{
			title: "Sort Order",
			dataIndex: "orderNum",
			key: "orderNum",
			align: "center",
			width: 100,
		},
		{
			title: "Status",
			dataIndex: "status",
			key: "status",
			width: 120,
			align: "center",
			render: (status) => (
				<Tag color={status === BasicStatus.ENABLE ? "success" : "error"}>{status === BasicStatus.ENABLE ? "Enable" : "Disable"}</Tag>
			),
		},
		{
			title: "Action",
			key: "action",
			width: 150,
			render: (_, record) => (
				<Space>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentDept({ ...record, parentId: record.parentId === "0" ? null : record.parentId });
							setDeptModalVisible(true);
						}}
					>
						Edit
					</Button>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentDept({ parentId: record.id });
							setDeptModalVisible(true);
						}}
					>
						Add Sub
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
		<Card
			title="Dept Management"
			extra={
				<Button
					type="primary"
					onClick={() => {
						setCurrentDept(null);
						setDeptModalVisible(true);
					}}
				>
					New
				</Button>
			}
		>
			<Table
				rowKey="id"
				columns={columns}
				dataSource={deptTree}
				loading={isLoading}
				pagination={false}
				size="small"
			/>

			<DeptModal
				visible={deptModalVisible}
				title={currentDept?.id ? "Edit Dept" : "New Dept"}
				initialValues={currentDept}
				onOk={(values) => saveMutation.mutate({ ...currentDept, ...values })}
				onCancel={() => setDeptModalVisible(false)}
				deptTree={deptTree}
			/>
		</Card>
	);
}
