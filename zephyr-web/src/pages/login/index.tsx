import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button, Input, Checkbox, message } from 'antd';
import { useNavigate, useLocation } from 'react-router';
import { useAuthStore } from "@/store/use-auth-store";
import client from "@/api/client";
import { GLOBAL_CONFIG } from "@/global-config.ts";
import Logo from "@/components/logo";

// 1. 扩展 Schema，加入记住我功能
const loginSchema = z.object({
    username: z.string().min(2, '用户名至少2位'),
    password: z.string().min(6, '密码至少6位'),
    remember: z.boolean().optional(),
});

type LoginForm = z.infer<typeof loginSchema>;

// 定义接口，移除 any
interface LoginResponse {
    token: string;
    refreshToken: string;
}

// 2. 将静态数据提取到组件外部，避免重复渲染时重新分配内存
const BARS_DATA = [
    { height: "60%", delay: 0 },
    { height: "80%", delay: 0.1 },
    { height: "45%", delay: 0.2 },
    { height: "90%", delay: 0.3 },
    { height: "70%", delay: 0.4 },
    { height: "55%", delay: 0.5 },
    { height: "85%", delay: 0.6 },
];

const POINTS_DATA = [
    { left: "10%", top: "20%", size: 6, delay: 0 },
    { left: "25%", top: "60%", size: 4, delay: 0.2 },
    { left: "40%", top: "35%", size: 8, delay: 0.4 },
    { left: "60%", top: "70%", size: 5, delay: 0.6 },
    { left: "75%", top: "25%", size: 6, delay: 0.8 },
    { left: "85%", top: "55%", size: 4, delay: 1 },
];

// 装饰性柱状图组件
function BarChart({ className = "" }: { className?: string }) {
    return (
        <div className={`flex items-end justify-center gap-2 ${className}`}>
            {BARS_DATA.map((bar, i) => (
                <div
                    key={i}
                    className="w-3 rounded-sm animate-pulse"
                    style={{
                        height: bar.height,
                        background: `linear-gradient(180deg, rgba(56, 189, 248, 0.8) 0%, rgba(56, 189, 248, 0.2) 100%)`,
                        animationDelay: `${bar.delay}s`,
                        boxShadow: "0 0 20px rgba(56, 189, 248, 0.3)",
                    }}
                />
            ))}
        </div>
    );
}

// 装饰性圆形元素
function CircleOrb({ className = "" }: { className?: string }) {
    return <div className={`absolute rounded-full blur-3xl ${className}`} />;
}

// 装饰性网格线
function GridPattern() {
    return (
        <div
            className="absolute inset-0 opacity-10"
            style={{
                backgroundImage: `
                    linear-gradient(rgba(56, 189, 248, 0.3) 1px, transparent 1px),
                    linear-gradient(90deg, rgba(56, 189, 248, 0.3) 1px, transparent 1px)
                `,
                backgroundSize: '60px 60px',
            }}
        />
    );
}

// 数据点装饰
function DataPoints() {
    return (
        <>
            {POINTS_DATA.map((point, i) => (
                <div
                    key={i}
                    className="absolute rounded-full bg-sky-400 animate-pulse"
                    style={{
                        left: point.left,
                        top: point.top,
                        width: point.size,
                        height: point.size,
                        animationDelay: `${point.delay}s`,
                        boxShadow: "0 0 10px rgba(56, 189, 248, 0.6)",
                    }}
                />
            ))}
        </>
    );
}

// 装饰性折线
function TrendLine() {
    return (
        <svg
            className="absolute inset-0 w-full h-full opacity-30 pointer-events-none"
            viewBox="0 0 400 200"
            preserveAspectRatio="none"
        >
            <defs>
                <linearGradient id="lineGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="rgba(56, 189, 248, 0)" />
                    <stop offset="50%" stopColor="rgba(56, 189, 248, 1)" />
                    <stop offset="100%" stopColor="rgba(56, 189, 248, 0)" />
                </linearGradient>
            </defs>
            <path
                d="M 0 150 Q 80 100, 120 120 T 200 80 T 280 100 T 360 60 L 400 40 L 400 200 L 0 200 Z"
                fill="url(#lineGradient)"
                className="animate-pulse"
            />
            <path
                d="M 0 150 Q 80 100, 120 120 T 200 80 T 280 100 T 360 60"
                fill="none"
                stroke="rgba(56, 189, 248, 0.8)"
                strokeWidth="2"
            />
        </svg>
    );
}

export default function LoginPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const setTokens = useAuthStore((state) => state.setToken);

    // 更安全的路由解构
    const from = (location.state as { from?: { pathname: string } })?.from?.pathname || '/';

    const { control, handleSubmit, formState: { errors, isSubmitting } } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            username: '',
            password: '',
            remember: false,
        }
    });

    const onSubmit = async (data: LoginForm) => {
        try {
            // 解构并排除 remember，后端可能不需要它
            const { remember, ...loginPayload } = data;

            const res = await client.post<any, LoginResponse>('auth/login', loginPayload);
            setTokens(res.token, res.refreshToken);

            // 如果用户勾选了记住我，可在此处处理持久化逻辑，如写入 localStorage 标记
            if (remember) {
                localStorage.setItem('remember_user', data.username);
            } else {
                localStorage.removeItem('remember_user');
            }

            message.success('登录成功！');
            navigate(from, { replace: true });
        } catch (error) {
            // 拦截器已经处理了大部分错误提示，react-hook-form 会自动解除 isSubmitting 状态
            console.error("Login failed:", error);
        }
    };

    // 统一定义输入框的基础暗黑系样式
    const inputClasses = "h-12 bg-white/5 border-white/10 text-white placeholder:text-white/30 hover:border-sky-400 focus:border-sky-400 focus:bg-white/10 rounded-xl transition-colors";

    return (
        <div className="relative grid grid-cols-1 lg:grid-cols-2 min-h-svh bg-[#0a0f1a] overflow-hidden">
            {/* 左侧装饰区域 - 商务数据科技风背景 */}
            <div className="relative hidden lg:flex flex-col items-center justify-center overflow-hidden">
                {/* 背景渐变 */}
                <div className="absolute inset-0 bg-gradient-to-br from-sky-900/40 via-transparent to-cyan-900/30" />

                {/* 模糊圆形装饰 */}
                <CircleOrb className="w-[500px] h-[500px] -top-48 -left-48 bg-gradient-to-br from-sky-500/30 to-cyan-400/20" />
                <CircleOrb className="w-[400px] h-[400px] top-1/2 -right-32 bg-gradient-to-br from-cyan-500/25 to-sky-400/15" />
                <CircleOrb className="w-[300px] h-[300px] -bottom-32 left-1/4 bg-gradient-to-br from-blue-500/20 to-sky-300/10" />

                {/* 背景元素 */}
                <GridPattern />
                <DataPoints />
                <TrendLine />

                {/* 柱状图 */}
                <div className="absolute bottom-20 left-10 right-10">
                    <BarChart />
                </div>

                {/* 底部装饰遮罩 */}
                <div className="absolute bottom-0 left-0 right-0 h-32 bg-gradient-to-t from-[#0a0f1a] to-transparent" />

                {/* Logo 和标题 */}
                <div className="relative z-10 flex flex-col items-center gap-4 animate-fade-in">
                    <div className="flex items-center gap-3">
                        <Logo size={48} />
                        <span className="text-3xl font-semibold text-white/90 tracking-wide">
                            {GLOBAL_CONFIG.appName}
                        </span>
                    </div>
                    <p className="text-sky-400/70 text-sm tracking-widest uppercase">
                        Business Intelligence Platform
                    </p>
                </div>
            </div>

            {/* 右侧登录表单区域 */}
            <div className="relative flex flex-col items-center justify-center px-6 md:px-16 py-12">
                {/* 背景虚化效果 */}
                <div className="absolute inset-0 bg-gradient-to-br from-[#0a0f1a]/50 via-transparent to-sky-900/20 lg:hidden" />
                <CircleOrb className="w-[300px] h-[300px] top-0 right-0 bg-sky-500/10 lg:hidden" />

                {/* 移动端 Logo */}
                <div className="lg:hidden relative z-10 flex flex-col items-center gap-3 mb-8">
                    <Logo size={40} />
                    <span className="text-xl font-medium text-white/80">
                        {GLOBAL_CONFIG.appName}
                    </span>
                </div>

                {/* 登录卡片 */}
                <div className="relative z-10 w-full max-w-md">
                    <div className="absolute inset-0 bg-gradient-to-b from-sky-500/10 to-cyan-500/5 rounded-3xl blur-xl" />
                    <div className="relative bg-[#0f172a]/80 backdrop-blur-xl border border-white/10 rounded-3xl p-8 shadow-2xl">
                        {/* 标题 */}
                        <div className="mb-8 text-center">
                            <h2 className="text-2xl font-semibold text-white mb-2">欢迎回来</h2>
                            <p className="text-white/50 text-sm">请登录您的账户以继续</p>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
                            <div>
                                <label className="block text-sm text-white/70 mb-2">用户名</label>
                                <Controller
                                    name="username"
                                    control={control}
                                    render={({ field }) => (
                                        <Input
                                            {...field}
                                            placeholder="请输入用户名"
                                            className={inputClasses}
                                            autoComplete="username"
                                        />
                                    )}
                                />
                                {errors.username && (
                                    <p className="mt-1 text-sm text-red-400 animate-fade-in">{errors.username.message}</p>
                                )}
                            </div>

                            <div>
                                <label className="block text-sm text-white/70 mb-2">密码</label>
                                <Controller
                                    name="password"
                                    control={control}
                                    render={({ field }) => (
                                        <Input.Password
                                            {...field}
                                            placeholder="请输入密码"
                                            className={`!bg-white/5 !border-white/10 hover:!border-sky-400 focus-within:!border-sky-400 focus-within:!bg-white/10 rounded-xl h-12 [&>input]:!bg-transparent [&>input]:!text-white [&>input::placeholder]:!text-white/30 [&_.ant-input-password-icon]:!text-white/50 hover:[&_.ant-input-password-icon]:!text-white transition-colors`}
                                            autoComplete="current-password"
                                        />
                                    )}
                                />
                                {errors.password && (
                                    <p className="mt-1 text-sm text-red-400 animate-fade-in">{errors.password.message}</p>
                                )}
                            </div>

                            <div className="flex items-center justify-between text-sm">
                                <Controller
                                    name="remember"
                                    control={control}
                                    render={({ field }) => (
                                        <Checkbox
                                            checked={field.value}
                                            onChange={field.onChange}
                                            className="text-white/50 hover:text-white/70 [&_.ant-checkbox-inner]:bg-white/5 [&_.ant-checkbox-inner]:border-white/20"
                                        >
                                            记住我
                                        </Checkbox>
                                    )}
                                />
                                <a href="#" className="text-sky-400 hover:text-sky-300 transition-colors">
                                    忘记密码？
                                </a>
                            </div>

                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={isSubmitting}
                                className="w-full h-12 mt-2 !bg-gradient-to-r from-sky-500 to-cyan-400 !border-none !text-white font-medium rounded-xl shadow-lg shadow-sky-500/25 hover:shadow-sky-500/40 hover:!from-sky-400 hover:!to-cyan-300 transition-all duration-300"
                            >
                                登 录
                            </Button>
                        </form>

                        {/* 底部提示 */}
                        <p className="mt-6 text-center text-white/30 text-xs">
                            登录即表示您同意我们的服务条款和隐私政策
                        </p>
                    </div>
                </div>

                {/* 底部版权 */}
                <p className="relative z-10 mt-8 text-white/30 text-xs">
                    © {new Date().getFullYear()} {GLOBAL_CONFIG.appName}. All rights reserved.
                </p>
            </div>

            {/* 全局装饰 - 边角虚化 */}
            <div className="absolute inset-0 pointer-events-none">
                <div className="absolute top-0 left-0 w-64 h-64 bg-gradient-to-br from-sky-500/20 to-transparent blur-3xl" />
                <div className="absolute bottom-0 right-0 w-64 h-64 bg-gradient-to-tl from-cyan-500/20 to-transparent blur-3xl" />
            </div>
        </div>
    );
}