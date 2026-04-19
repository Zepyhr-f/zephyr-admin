import { Modal, Tree } from "antd";
import { useEffect, useState } from "react";

interface PermissionModalProps {
	visible: boolean;
	roleId?: string;
	onOk: (menuIds: string[]) => void;
	onCancel: () => void;
	menuTree: any[];
	initialMenuIds?: string[];
}

export default function PermissionModal({ visible, onOk, onCancel, menuTree, initialMenuIds }: PermissionModalProps) {
	const [checkedKeys, setCheckedKeys] = useState<any[]>([]);

	useEffect(() => {
		if (visible && initialMenuIds) {
			setCheckedKeys(initialMenuIds.map(String));
		} else if (visible) {
			setCheckedKeys([]);
		}
	}, [visible, initialMenuIds]);

	return (
		<Modal title="Assign Permissions" open={visible} onOk={() => onOk(checkedKeys as string[])} onCancel={onCancel} destroyOnClose>
			<div className="max-h-[500px] overflow-auto py-4">
				<Tree
					checkable
					treeData={menuTree}
					fieldNames={{ title: "menuName", key: "id", children: "children" }}
					checkedKeys={checkedKeys}
					onCheck={(keys) => setCheckedKeys(keys as any[])}
					defaultExpandAll
				/>
			</div>
		</Modal>
	);
}
