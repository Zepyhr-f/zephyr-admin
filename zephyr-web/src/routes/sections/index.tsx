import { Navigate, type RouteObject } from "react-router";
import { authRoutes } from "./auth";
import { getDashboardRoutes } from "./dashboard";
import { mainRoutes } from "./main";

/**
 * 聚合所有路由分段
 * 将 Auth 放在最前面，确保登录页始终可达
 */
export const getRoutesSection = (): RouteObject[] => [
	// Auth
	...authRoutes,
	// Dashboard Layout & Children
	...getDashboardRoutes(),
	// System/Error Paths
	...mainRoutes,
	// Catch-all 404 Redirect
	{ path: "*", element: <Navigate to="/404" replace /> },
];
