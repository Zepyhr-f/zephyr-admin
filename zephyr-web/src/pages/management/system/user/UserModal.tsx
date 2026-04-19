import { Form, Input, Modal, Select, TreeSelect } from "antd";
import { useEffect } from "react";
import type { UserInfo } from "#/entity";
import { BasicStatus } from "#/enum";

interface UserModalProps {
	visible: boolean;
	title: string;
	initialValues?: any;
	onOk: (values: any) => void;
	onCancel: () => void;
	deptTree: any[];
	roleList: any[];
}

export default function UserModal({ visible, title, initialValues, onOk, onCancel, deptTree, roleList }: UserModalProps) {
	const [form] = Form.useForm();

	useEffect(() => {
		if (visible) {
			if (initialValues) {
				form.setFieldsValue({
					...initialValues,
					status: initialValues.status ?? BasicStatus.ENABLE,
				});
			} else {
				form.resetFields();
				form.setFieldsValue({ status: BasicStatus.ENABLE });
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

	return (
		<Modal title={title} open={visible} onOk={handleOk} onCancel={onCancel} destroyOnClose>
			<Form form={form} layout="vertical" name="user_modal">
				<Form.Item name="username" label="Username" rules={[{ required: true, message: "Please input username" }]}>
					<Input disabled={!!initialValues?.id} />
				</Form.Item>
				<Form.Item name="realName" label="Real Name" rules={[{ required: true, message: "Please input real name" }]}>
					<Input />
				</Form.Item>
				<Form.Item name="email" label="Email" rules={[{ type: "email", message: "Please input valid email" }]}>
					<Input />
				</Form.Item>
				<Form.Item name="phone" label="Phone">
					<Input />
				</Form.Item>
				<Form.Item name="deptId" label="Department" rules={[{ required: true, message: "Please select department" }]}>
					<TreeSelect
						treeData={deptTree}
						fieldNames={{ label: "deptName", value: "id", children: "children" }}
						placeholder="Select department"
						allowClear
						treeDefaultExpandAll
					/>
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
