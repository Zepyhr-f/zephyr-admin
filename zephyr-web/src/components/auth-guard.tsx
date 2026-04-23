import { Navigate, useLocation } from 'react-router';
import { useAuthStore } from '@/store/use-auth-store';
import { type ReactNode } from 'react';

interface AuthGuardProps {
    children: ReactNode;
}

export default function AuthGuard({ children }: AuthGuardProps) {
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
    const location = useLocation();

    if (!isAuthenticated) {
        // 保存尝试访问的路径，登录后可以跳回
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <>{children}</>;
}
