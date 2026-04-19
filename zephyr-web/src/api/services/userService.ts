import apiClient from "../apiClient";

import type { UserInfo, UserToken } from "#/entity";

export interface SignInReq {
	username: string;
	password: string;
}

export interface SignUpReq extends SignInReq {
	email: string;
}
export type SignInRes = UserToken & { user: UserInfo };

export enum UserApi {
	SignIn = "/auth/signin",
	SignUp = "/auth/signup",
	Logout = "/auth/logout",
	Refresh = "/auth/refresh",
	User = "/user",
}

const signin = (data: SignInReq) => apiClient.post<SignInRes>({ url: UserApi.SignIn, data });
const signup = (data: SignUpReq) => apiClient.post<SignInRes>({ url: UserApi.SignUp, data });
const logout = () => apiClient.get({ url: UserApi.Logout });

const list = (params: any) => apiClient.get<UserInfo[]>({ url: `${UserApi.User}/list`, params });
const findById = (id: string) => apiClient.get<UserInfo>({ url: `${UserApi.User}/detail`, params: { id } });
const saveUser = (data: any) => apiClient.post({ url: `${UserApi.User}/save`, data });
const updateUser = (data: any) => apiClient.post({ url: `${UserApi.User}/update`, data });
const removeUser = (ids: string[]) => apiClient.post({ url: `${UserApi.User}/remove`, data: ids });
const updateStatus = (id: string, status: number) => apiClient.post({ url: `${UserApi.User}/updateStatus`, data: { id, status } });
const resetPassword = (id: string) => apiClient.post({ url: `${UserApi.User}/resetPassword`, data: { id } });
const assignRoles = (userId: string, roleIds: string[]) => apiClient.post({ url: `${UserApi.User}/assignRoles`, data: { userId, roleIds } });

export default {
	signin,
	signup,
	logout,
	list,
	findById,
	saveUser,
	updateUser,
	removeUser,
	updateStatus,
	resetPassword,
	assignRoles,
};

