import { Checkbox, Modal } from "antd";
import { useEffect, useState } from "react";

interface RoleAssignModalProps {
	visible: boolean;
	userId?: string;
	onOk: (roleIds: string[]) => void;
	onCancel: () => void;
	allRoles: any[];
	initialRoleIds?: string[];
}

export default function RoleAssignModal({ visible, onOk, onCancel, allRoles, initialRoleIds }: RoleAssignModalProps) {
	const [selectedRoleIds, setSelectedRoleIds] = useState<string[]>([]);

	useEffect(() => {
		if (visible && initialRoleIds) {
			setSelectedRoleIds(initialRoleIds.map(String));
		} else if (visible) {
			setSelectedRoleIds([]);
		}
	}, [visible, initialRoleIds]);

	return (
		<Modal title="Assign Roles" open={visible} onOk={() => onOk(selectedRoleIds)} onCancel={onCancel} destroyOnClose>
			<div className="py-4">
				<Checkbox.Group
					options={allRoles.map((r) => ({ label: r.roleName, value: String(r.id) }))}
					value={selectedRoleIds}
					onChange={(checkedValues) => setSelectedRoleIds(checkedValues as string[])}
				/>
			</div>
		</Modal>
	);
}
