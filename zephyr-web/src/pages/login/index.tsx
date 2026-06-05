import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Input, Checkbox, message } from 'antd';
import { useNavigate, useLocation } from 'react-router';
import axios from 'axios';
import { useAuthStore } from '@/store/use-auth-store';
import client from '@/api/client';
import { GLOBAL_CONFIG } from '@/global-config.ts';
import './login.css';

const loginSchema = z.object({
    username: z.string().min(2, '用户名至少2位'),
    password: z.string().min(6, '密码至少6位'),
    remember: z.boolean().optional(),
});

type LoginForm = z.infer<typeof loginSchema>;

interface LoginResponse {
    token: string;
}

function BoltIcon() {
    return (
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M13 10V3L4 14h7v7l9-11h-7z" />
        </svg>
    );
}

function GoogleIcon() {
    return (
        <svg viewBox="0 0 24 24" fill="none">
            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4" />
            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853" />
            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05" />
            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335" />
        </svg>
    );
}

function WechatIcon() {
    return (
        <svg viewBox="0 0 24 24" fill="#07C160">
            <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 0 1 .213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 0 0 .167-.054l1.903-1.114a.864.864 0 0 1 .717-.098 10.16 10.16 0 0 0 2.837.403c4.801 0 8.692-3.287 8.692-7.342 0-4.054-3.89-7.34-8.692-7.34zm-2.3 6.442c.67 0 1.212.543 1.212 1.214 0 .67-.542 1.213-1.211 1.213-.67 0-1.213-.542-1.213-1.213 0-.671.543-1.214 1.213-1.214zm5.596 0c.67 0 1.213.543 1.213 1.214 0 .67-.542 1.213-1.213 1.213-.67 0-1.212-.542-1.212-1.213 0-.671.542-1.214 1.212-1.214z" />
            <path d="M23.705 14.543c0-3.578-3.124-6.48-7.074-6.674.255.553.398 1.17.398 1.824 0 2.505-2.185 4.537-4.88 4.537-.504 0-.99-.073-1.45-.203 1.148 2.573 3.922 4.39 7.136 4.39.65 0 1.28-.088 1.882-.25l1.47.86a.26.26 0 0 0 .13.034c.128 0 .232-.104.232-.232a.55.55 0 0 0-.037-.17l-.3-1.14a.46.46 0 0 1 .166-.51c1.52-1.1 2.55-2.7 2.55-4.52z" />
        </svg>
    );
}

export default function LoginPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const setToken = useAuthStore((state) => state.setToken);
    const setUserInfo = useAuthStore((state) => state.setUserInfo);
    const setMenus = useAuthStore((state) => state.setMenus);

    const from = (location.state as { from?: { pathname: string } })?.from?.pathname || '/';

    const { control, handleSubmit, formState: { errors, isSubmitting } } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
        defaultValues: { username: '', password: '', remember: false },
    });

    const onSubmit = async (data: LoginForm) => {
        try {
            const { remember, ...loginPayload } = data;
            const res = await client.post<any, LoginResponse>('zephyr-auth/login', loginPayload);
            const token = res.token;

            // 1. 保存 token，设置 isAuthenticated = true
            setToken(token);

            // 2. 立即拉取用户信息 & 菜单，确保 AuthGuard 放行
            const headers = { Authorization: `Bearer ${token}` };
            const baseURL = import.meta.env.VITE_API_BASE_URL;
            const [infoRes, menuRes] = await Promise.all([
                axios.get(`${baseURL}zephyr-auth/info`, { headers, withCredentials: true }),
                axios.get(`${baseURL}zephyr-system/menu/tree`, { headers, withCredentials: true }),
            ]);
            const infoData = infoRes.data?.data;
            const menuData = menuRes.data?.data;
            if (infoData) {
                setUserInfo(infoData.user, infoData.roles, infoData.permissions);
            }
            setMenus(menuData || []);

            // 3. 持久化"记住我"
            if (remember) {
                localStorage.setItem('remember_user', data.username);
            } else {
                localStorage.removeItem('remember_user');
            }

            message.success('登录成功！');
            navigate(from, { replace: true });
        } catch (error) {
            console.error('Login failed:', error);
            message.error('登录失败，请检查用户名或密码');
        }
    };


    return (
        <div className="login-page">
            {/* Animated background orbs */}
            <div className="login-orb login-orb-1" />
            <div className="login-orb login-orb-2" />
            <div className="login-orb login-orb-3" />
            <div className="login-orb login-orb-4" />

            <div className="login-card">
                {/* ── Left: Brand Panel ── */}
                <div className="login-brand">
                    <div className="login-logo">
                        <div className="login-logo-icon">
                            <BoltIcon />
                        </div>
                        <span className="login-logo-name">{GLOBAL_CONFIG.appName}</span>
                    </div>

                    <div className="login-brand-body">
                        <p className="login-brand-tagline">数据管理平台</p>
                        <h1 className="login-brand-title">
                            掌控您的<br />
                            <span>核心数据</span>
                        </h1>
                        <p className="login-brand-desc">
                            为现代团队打造的企业级管理平台，安全、极速、赏心悦目。
                        </p>
                        <div className="login-brand-stats">
                            <div className="login-stat">
                                <div className="login-stat-value">2.4k+</div>
                                <div className="login-stat-label">活跃用户</div>
                            </div>
                            <div className="login-stat">
                                <div className="login-stat-value">99.9%</div>
                                <div className="login-stat-label">系统稳定性</div>
                            </div>
                            <div className="login-stat">
                                <div className="login-stat-value">168ms</div>
                                <div className="login-stat-label">平均响应</div>
                            </div>
                            <div className="login-stat">
                                <div className="login-stat-value">256位</div>
                                <div className="login-stat-label">数据加密</div>
                            </div>
                        </div>
                    </div>

                    <div className="login-brand-footer">
                        <div className="login-user-avatars">
                            <div className="login-user-count">+99</div>
                            <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Jocelyn" alt="user" />
                            <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Aneka" alt="user" />
                            <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" alt="user" />
                        </div>
                        <p>全球数千名开发者的共同选择</p>
                    </div>
                </div>

                {/* ── Right: Form Panel ── */}
                <div className="login-form-panel">
                    <div className="login-form-inner">
                        <div className="login-form-header">
                            <h2 className="login-form-title">欢迎回来</h2>
                            <p className="login-form-subtitle">请登录您的账户以继续使用</p>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)}>
                            <div className="login-field">
                                <label className="login-label" htmlFor="login-username">用户名</label>
                                <Controller
                                    name="username"
                                    control={control}
                                    render={({ field }) => (
                                        <Input
                                            id="login-username"
                                            {...field}
                                            placeholder="请输入用户名"
                                            autoComplete="username"
                                        />
                                    )}
                                />
                                {errors.username && (
                                    <p className="login-field-error">{errors.username.message}</p>
                                )}
                            </div>

                            <div className="login-field">
                                <label className="login-label" htmlFor="login-password">密码</label>
                                <Controller
                                    name="password"
                                    control={control}
                                    render={({ field }) => (
                                        <Input.Password
                                            id="login-password"
                                            {...field}
                                            placeholder="请输入密码"
                                            autoComplete="current-password"
                                        />
                                    )}
                                />
                                {errors.password && (
                                    <p className="login-field-error">{errors.password.message}</p>
                                )}
                            </div>

                            <div className="login-field-row">
                                <Controller
                                    name="remember"
                                    control={control}
                                    render={({ field }) => (
                                        <Checkbox checked={field.value} onChange={field.onChange}>
                                            记住我
                                        </Checkbox>
                                    )}
                                />
                                <a href="#" className="login-forgot">忘记密码？</a>
                            </div>

                            <button
                                type="submit"
                                className="login-btn-primary"
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? '登录中...' : '登 录'}
                            </button>
                        </form>

                        <div className="login-divider">
                            <div className="login-divider-line" />
                            <span className="login-divider-text">其他登录方式</span>
                            <div className="login-divider-line" />
                        </div>

                        <div className="login-social-grid">
                            <button type="button" className="login-social-btn">
                                <GoogleIcon />
                                Google
                            </button>
                            <button type="button" className="login-social-btn">
                                <WechatIcon />
                                微信
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
