import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button, Input, message } from 'antd';
import { useAuthStore} from "@/store/use-auth-store";
import client from "@/api/client";

const loginSchema = z.object({
    username: z.string().min(2, '用户名至少2位'),
    password: z.string().min(6, '密码至少6位'),
})

type LoginForm = z.infer<typeof loginSchema>

export default function LoginPage() {
    const setTokens = useAuthStore((state) => state.setToken);

    const { register, handleSubmit, formState: { errors, isLoading } } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
    });

    const onSubmit = async (data: LoginForm) => {
        try {
            const res: any = await client.post('/auth/login', data);

            setTokens(res.token, res.refreshToken);
            message.success('登录成功！');

            // eslint-disable-next-line @typescript-eslint/no-unused-vars
        } catch (error) {
            // 拦截器已经处理了大部分错误提示
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <div className="w-full max-w-md rounded-lg bg-white p-8 shadow-md">
                <h2 className="mb-6 text-center text-2xl font-bold">Zephyr Admin</h2>

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">用户名</label>
                        <Input
                            {...register('username')}
                            status={errors?.username ? 'error' : ''}
                            placeholder="请输入admin"
                        />
                        {errors.username && <p className="mt-1 text-xs text-red-500">{errors.username.message}</p>}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">密码</label>
                        <Input.Password
                            {...register('password')}
                            status={errors.password ? 'error' : ''}
                            placeholder="请输入 123456"
                        />
                        {errors.password && <p className="mt-1 text-xs text-red-500">{errors.password.message}</p>}
                    </div>

                    <Button
                        type="primary"
                        htmlType="submit"
                        block
                        loading={isLoading}
                        className="mt-4"
                    >
                        登录
                    </Button>
                </form>
            </div>
        </div>
    );
}