import { Form, Input, InputNumber, Modal, Select, TreeSelect } from "antd";
import { useEffect } from "react";
import { BasicStatus } from "#/enum";

interface DeptModalProps {
	visible: boolean;
	title: string;
	initialValues?: any;
	onOk: (values: any) => void;
	onCancel: () => void;
	deptTree: any[];
}

export default function DeptModal({ visible, title, initialValues, onOk, onCancel, deptTree }: DeptModalProps) {
	const [form] = Form.useForm();

	useEffect(() => {
		if (visible) {
			if (initialValues) {
				form.setFieldsValue(initialValues);
			} else {
				form.resetFields();
				form.setFieldsValue({ status: BasicStatus.ENABLE, orderNum: 1 });
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
			<Form form={form} layout="vertical" name="dept_modal">
				<Form.Item name="parentId" label="Parent Department">
					<TreeSelect
						treeData={deptTree}
						fieldNames={{ label: "deptName", value: "id", children: "children" }}
						placeholder="Select parent department"
						allowClear
						treeDefaultExpandAll
					/>
				</Form.Item>
				<Form.Item name="deptName" label="Department Name" rules={[{ required: true, message: "Please input department name" }]}>
					<Input />
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
