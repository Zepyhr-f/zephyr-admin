import apiClient from "../apiClient";

import type { Menu } from "#/entity";

export enum MenuApi {
	Menu = "/menu",
}

const getMenuList = () => apiClient.get<Menu[]>({ url: `${MenuApi.Menu}/tree` });
const getMenuDetail = (id: string) => apiClient.get<Menu>({ url: `${MenuApi.Menu}/detail`, params: { id } });
const saveMenu = (data: any) => apiClient.post({ url: `${MenuApi.Menu}/save`, data });
const updateMenu = (data: any) => apiClient.post({ url: `${MenuApi.Menu}/update`, data });
const removeMenu = (ids: string[]) => apiClient.post({ url: `${MenuApi.Menu}/remove`, data: ids });

export default {
	getMenuList,
	getMenuDetail,
	saveMenu,
	updateMenu,
	removeMenu,
};

