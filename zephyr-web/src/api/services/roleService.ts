import apiClient from "../apiClient";

export enum RoleApi {
	Role = "/zephyr-system/role",
}

const list = (params: any) => apiClient.get<any>({ url: `${RoleApi.Role}/list`, params });
const all = () => apiClient.get<any[]>({ url: `${RoleApi.Role}/all` });
const detail = (id: string) => apiClient.get<any>({ url: `${RoleApi.Role}/detail`, params: { id } });
const saveRole = (data: any) => apiClient.post({ url: `${RoleApi.Role}/save`, data });
const updateRole = (data: any) => apiClient.post({ url: `${RoleApi.Role}/update`, data });
const updateStatus = (id: string, status: number) => apiClient.post({ url: `${RoleApi.Role}/updateStatus`, data: { id, status } });
const removeRole = (ids: string[]) => apiClient.post({ url: `${RoleApi.Role}/remove`, data: ids });
const assignMenus = (roleId: string, menuIds: string[]) => apiClient.post({ url: `${RoleApi.Role}/assignMenus`, data: { roleId, menuIds } });
const getMenuIds = (roleId: string) => apiClient.get<string[]>({ url: `${RoleApi.Role}/menuIds/${roleId}` });

export default {
	list,
	all,
	detail,
	saveRole,
	updateRole,
	updateStatus,
	removeRole,
	assignMenus,
	getMenuIds,
};
