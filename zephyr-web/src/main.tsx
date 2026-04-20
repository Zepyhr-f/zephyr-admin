import "./global.css";
import "./theme/theme.css";
import "./locales/i18n";

import React, { useMemo } from "react";
import ReactDOM from "react-dom/client";
import { Outlet, RouterProvider, createBrowserRouter } from "react-router";

import App from "./App";
import menuService from "@/api/services/menuService";
import { registerLocalIcons } from "@/components/icon";
import { GLOBAL_CONFIG } from "@/global-config";
import ErrorBoundary from "@/routes/components/error-boundary";
import { getRoutesSection } from "@/routes/sections";
import useUserStore, { useUserMenus } from "@/store/userStore";

/**
 * 响应式路由组件
 * 只有在菜单数据深度变化时才重建路由实例，避免无限重绘
 */
function MainRouter() {
	const userMenus = useUserMenus();

	const router = useMemo(() => {
		console.log("[Router] Stable initialization with menus:", userMenus?.length || 0);
		return createBrowserRouter(
			[
				{
					element: (
						<App>
							<Outlet />
						</App>
					),
					errorElement: <ErrorBoundary />,
					children: getRoutesSection(),
				},
			],
			{
				basename: GLOBAL_CONFIG.publicPath,
			},
		);
	}, [userMenus]);

	return <RouterProvider router={router} />;
}

// 单例模式挂载应用
const bootstrap = () => {
	const container = document.getElementById("root");
	if (!container) return;

	// @ts-ignore
	if (!window.__ZEPHYR_ROOT__) {
		// 仅初始化一次的资源
		registerLocalIcons();
		
		// @ts-ignore
		window.__ZEPHYR_ROOT__ = ReactDOM.createRoot(container);
		
		// 启动预拉取菜单（仅有 Token 时）
		const { accessToken } = useUserStore.getState().userToken;
		if (accessToken && GLOBAL_CONFIG.routerMode === "backend") {
			menuService.getMenuList()
				.then((menus) => {
					const { userMenus } = useUserStore.getState();
					// 使用序列化比较，防止引用变化导致死循环
					if (JSON.stringify(userMenus) !== JSON.stringify(menus)) {
						useUserStore.getState().actions.setUserMenus(menus || []);
					}
				})
				.catch((error) => {
					console.error("Bootstrap menu fetch failed:", error);
				});
		}
	}

	// @ts-ignore
	window.__ZEPHYR_ROOT__.render(
		<React.StrictMode>
			<MainRouter />
		</React.StrictMode>
	);
};

bootstrap();
