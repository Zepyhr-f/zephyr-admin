import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Table, Button, Card, Popconfirm, message, Space, Tag } from "antd";
import type { ColumnsType } from "antd/es/table";
import { useState } from "react";
import { Icon } from "@/components/icon";
import menuService from "@/api/services/menuService";
import { BasicStatus, PermissionType } from "#/enum";
import MenuModal from "./MenuModal";

export default function PermissionPage() {
	const queryClient = useQueryClient();
	const [menuModalVisible, setMenuModalVisible] = useState(false);
	const [currentMenu, setCurrentMenu] = useState<any>(null);

	// Queries
	const { data: menuTree = [], isLoading } = useQuery({
		queryKey: ["menuTree"],
		queryFn: () => menuService.getMenuList(),
	});

	// Mutations
	const saveMutation = useMutation({
		mutationFn: (values: any) => (values.id ? menuService.updateMenu(values) : menuService.saveMenu(values)),
		onSuccess: () => {
			message.success("Saved successfully");
			setMenuModalVisible(false);
			queryClient.invalidateQueries({ queryKey: ["menuTree"] });
		},
	});

	const deleteMutation = useMutation({
		mutationFn: (ids: string[]) => menuService.removeMenu(ids),
		onSuccess: () => {
			message.success("Deleted successfully");
			queryClient.invalidateQueries({ queryKey: ["menuTree"] });
		},
	});

	const columns: ColumnsType<any> = [
		{
			title: "Menu Name",
			dataIndex: "menuName",
			key: "menuName",
			width: 250,
		},
		{
			title: "Icon",
			dataIndex: "icon",
			key: "icon",
			width: 80,
			align: "center",
			render: (icon) => (icon ? <Icon icon={icon} size={18} /> : "-"),
		},
		{
			title: "Type",
			dataIndex: "type",
			key: "type",
			width: 100,
			render: (type) => {
				const color = type === PermissionType.CATALOGUE ? "blue" : type === PermissionType.MENU ? "green" : "orange";
				const label = type === PermissionType.CATALOGUE ? "Catalogue" : type === PermissionType.MENU ? "Menu" : "Button";
				return <Tag color={color}>{label}</Tag>;
			},
		},
		{
			title: "Route Path",
			dataIndex: "path",
			key: "path",
		},
		{
			title: "Component",
			dataIndex: "component",
			key: "component",
		},
		{
			title: "Permission",
			dataIndex: "perms",
			key: "perms",
		},
		{
			title: "Sort",
			dataIndex: "orderNum",
			key: "orderNum",
			width: 80,
			align: "center",
		},
		{
			title: "Status",
			dataIndex: "status",
			key: "status",
			width: 100,
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
							setCurrentMenu({ ...record, parentId: record.parentId === "0" ? null : record.parentId });
							setMenuModalVisible(true);
						}}
					>
						Edit
					</Button>
					<Button
						type="link"
						size="small"
						onClick={() => {
							setCurrentMenu({ parentId: record.id });
							setMenuModalVisible(true);
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
			title="Menu Management"
			extra={
				<Button
					type="primary"
					onClick={() => {
						setCurrentMenu(null);
						setMenuModalVisible(true);
					}}
				>
					New
				</Button>
			}
		>
			<Table
				rowKey="id"
				columns={columns}
				dataSource={menuTree}
				loading={isLoading}
				pagination={false}
				size="small"
				scroll={{ x: "max-content" }}
			/>

			<MenuModal
				visible={menuModalVisible}
				title={currentMenu?.id ? "Edit Menu" : "New Menu"}
				initialValues={currentMenu}
				onOk={(values) => saveMutation.mutate({ ...currentMenu, ...values })}
				onCancel={() => setMenuModalVisible(false)}
				menuTree={menuTree}
			/>
		</Card>
	);
}

