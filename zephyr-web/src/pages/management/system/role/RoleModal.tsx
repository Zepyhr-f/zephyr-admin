import { Form, Input, InputNumber, Modal, Select } from "antd";
import { useEffect } from "react";
import { BasicStatus } from "#/enum";

interface RoleModalProps {
	visible: boolean;
	title: string;
	initialValues?: any;
	onOk: (values: any) => void;
	onCancel: () => void;
}

export default function RoleModal({ visible, title, initialValues, onOk, onCancel }: RoleModalProps) {
	const [form] = Form.useForm();

	useEffect(() => {
		if (visible) {
			if (initialValues) {
				form.setFieldsValue(initialValues);
			} else {
				form.resetFields();
				form.setFieldsValue({ status: BasicStatus.ENABLE, roleSort: 1 });
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
			<Form form={form} layout="vertical" name="role_modal">
				<Form.Item name="roleName" label="Role Name" rules={[{ required: true, message: "Please input role name" }]}>
					<Input />
				</Form.Item>
				<Form.Item name="roleKey" label="Role Key" rules={[{ required: true, message: "Please input role key" }]}>
					<Input />
				</Form.Item>
				<Form.Item name="roleSort" label="Sort Order" rules={[{ required: true }]}>
					<InputNumber min={0} style={{ width: "100%" }} />
				</Form.Item>
				<Form.Item name="status" label="Status" rules={[{ required: true }]}>
					<Select>
						<Select.Option value={BasicStatus.ENABLE}>Enable</Select.Option>
						<Select.Option value={BasicStatus.DISABLE}>Disable</Select.Option>
					</Select>
				</Form.Item>
				<Form.Item name="remark" label="Remark">
					<Input.TextArea rows={4} />
				</Form.Item>
			</Form>
		</Modal>
	);
}
