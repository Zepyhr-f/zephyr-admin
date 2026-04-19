import apiClient from "../apiClient";

export enum DeptApi {
	Dept = "/dept",
}

const tree = () => apiClient.get<any[]>({ url: `${DeptApi.Dept}/tree` });
const detail = (id: string) => apiClient.get<any>({ url: `${DeptApi.Dept}/detail`, params: { id } });
const saveDept = (data: any) => apiClient.post({ url: `${DeptApi.Dept}/save`, data });
const updateDept = (data: any) => apiClient.post({ url: `${DeptApi.Dept}/update`, data });
const removeDept = (ids: string[]) => apiClient.post({ url: `${DeptApi.Dept}/remove`, data: ids });

export default {
	tree,
	detail,
	saveDept,
	updateDept,
	removeDept,
};
