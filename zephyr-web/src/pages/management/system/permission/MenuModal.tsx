import { Form, Input, InputNumber, Modal, Radio, Select, TreeSelect } from "antd";
import { useEffect } from "react";
import { BasicStatus, PermissionType } from "#/enum";

interface MenuModalProps {
	visible: boolean;
	title: string;
	initialValues?: any;
	onOk: (values: any) => void;
	onCancel: () => void;
	menuTree: any[];
}

export default function MenuModal({ visible, title, initialValues, onOk, onCancel, menuTree }: MenuModalProps) {
	const [form] = Form.useForm();

	useEffect(() => {
		if (visible) {
			if (initialValues) {
				form.setFieldsValue(initialValues);
			} else {
				form.resetFields();
				form.setFieldsValue({ status: BasicStatus.ENABLE, type: PermissionType.CATALOGUE, orderNum: 1 });
			}
		}
	}, [visible, initialValues, form]);

	const handleOk = async () => {
		try {
			const values = await form.validateFields();
			onOk(values);
		} catch (error) {
			console.error("Validate Failed:", error);
		}
	};

	const menuType = Form.useWatch("type", form);

	return (
		<Modal title={title} open={visible} onOk={handleOk} onCancel={onCancel} destroyOnClose width={600}>
			<Form form={form} layout="vertical" name="menu_modal">
				<Form.Item name="parentId" label="Parent Menu">
					<TreeSelect
						treeData={menuTree}
						fieldNames={{ label: "menuName", value: "id", children: "children" }}
						placeholder="Select parent menu"
						allowClear
						treeDefaultExpandAll
					/>
				</Form.Item>
				<Form.Item name="type" label="Menu Type" rules={[{ required: true }]}>
					<Radio.Group>
						<Radio value={PermissionType.CATALOGUE}>Catalogue</Radio>
						<Radio value={PermissionType.MENU}>Menu</Radio>
						<Radio value={PermissionType.BUTTON}>Button</Radio>
					</Radio.Group>
				</Form.Item>
				<Form.Item name="menuName" label="Menu Name" rules={[{ required: true, message: "Please input menu name" }]}>
					<Input />
				</Form.Item>
				<Form.Item name="icon" label="Icon">
					<Input placeholder="Ant Design Icon name" />
				</Form.Item>
				{menuType !== PermissionType.BUTTON && (
					<>
						<Form.Item name="path" label="Route Path" rules={[{ required: true }]}>
							<Input placeholder="e.g. /user" />
						</Form.Item>
						{menuType === PermissionType.MENU && (
							<Form.Item name="component" label="Component Path">
								<Input placeholder="e.g. /management/system/user/index" />
							</Form.Item>
						)}
					</>
				)}
				<Form.Item name="perms" label="Permission Identifier">
					<Input placeholder="e.g. sys:user:list" />
				</Form.Item>
				<Form.Item name="orderNum" label="Sort Order" rules={[{ required: true }]}>
					<InputNumber min={0} style={{ width: "100%" }} />
				</Form.Item>
				<Form.Item name="status" label="Status" rules={[{ required: true }]}>
					<Select>
						<Select.Option value={BasicStatus.ENABLE}>Enable</Select.Option>
						<Select.Option value={BasicStatus.DISABLE}>Disable</Select.Option>
					</Select>
				</Form.Item>
			</Form>
		</Modal>
	);
}
