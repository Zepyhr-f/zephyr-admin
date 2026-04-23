import { Button } from 'antd';
import { useAuthStore } from '@/store/use-auth-store';

export default function DashboardPage() {
    const { logout } = useAuthStore();

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">欢迎来到 Zephyr Admin</h1>
            <p className="mb-4">这是一个受保护的页面，只有登录后才能看到。</p>
            <Button onClick={logout} danger>退出登录</Button>
        </div>
    );
}
