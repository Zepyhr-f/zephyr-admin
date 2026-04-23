import axios, {type AxiosResponse, type InternalAxiosRequestConfig} from "axios";
import {useAuthStore} from "../store/use-auth-store.ts";
import {message, notification} from "antd";

const client = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 5000,
    headers: {'Content-Type': 'application/json'}
});

client.interceptors.request.use(
    (config: InternalAxiosRequestConfig)=> {
        const { token } = useAuthStore.getState();
        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error),
);

client.interceptors.response.use(
    (response: AxiosResponse) => {
        const { data } = response;
        const { code, msg, data: payload } = data;

        if (code === 200) {
            return payload;
        }
        message.error(msg || `业务系统异常`);
        return Promise.reject(new Error(msg || 'Error'));
    },
    async (error) => {
        const { response } = error;

        if (response) {
            const { status, data } = response;

            // 优先使用后端返回的错误消息
            const errorMsg = data?.msg || data?.message || '网络连接异常，请检查网络设置';

            if (status === 401) {
                useAuthStore.getState().logout();
                window.location.href = '/login';
            } else if (status === 403) {
                notification.error({ message: '无权访问', description: errorMsg });
            } else if (status === 500) {
                notification.error({ message: '服务器错误', description: '后台服务开小差了，请稍后再试' });
            } else {
                message.error(errorMsg);
            }
        } else {
            message.error('网络连接异常，请检查网络设置');
        }

        return Promise.reject(error);
    }
);

export default client;