import { Routes, Route, Navigate } from "react-router";
import { ConfigProvider, theme as antdTheme } from "antd";
import zhCN from "antd/locale/zh_CN";
import { useThemeStore } from "@/store/use-theme-store";
import { antdLightToken } from "@/theme/antd-token";
import { antdDarkToken } from "@/theme/dark-token";
import AdminLayout from "@/layouts/admin-layout";
import BlankLayout from "@/layouts/blank-layout";
import LoginPage from "@/pages/login";
import AuthGuard from "@/components/auth-guard";
import { routes, type AppRoute } from "@/routes/route-config";

/* 递归把 route-config 转为 <Route> 节点 */
function renderRoutes(rs: AppRoute[]): React.ReactNode {
  return rs.flatMap((r) => {
    if (r.children?.length) {
      return renderRoutes(r.children);
    }
    if (!r.element) return [];
    return (
      <Route
        key={r.path}
        path={r.path === "/" ? "/" : r.path}
        element={r.element}
      />
    );
  });
}

function App() {
  const { isDark } = useThemeStore();

  return (
    <ConfigProvider
      locale={zhCN}
      theme={{
        algorithm: isDark ? antdTheme.darkAlgorithm : antdTheme.defaultAlgorithm,
        ...(isDark ? antdDarkToken : antdLightToken),
        token: {
          ...(isDark ? antdDarkToken?.token : antdLightToken?.token),
          colorPrimary: "var(--z-primary)",
          colorInfo: "var(--z-primary)",
          colorWarning: "var(--z-accent)",
          borderRadius: 10,
        },
        components: {
          Layout: {
            bodyBg: "var(--z-bg)",
            headerBg: "var(--z-surface)",
            siderBg: "var(--z-surface)",
          },
          Menu: { itemBorderRadius: 10 },
          Card: { borderRadiusLG: 14 },
        },
      }}
    >
      <Routes>
        {/* 登录页（空白布局） */}
        <Route element={<BlankLayout />}>
          <Route path="/login" element={<LoginPage />} />
        </Route>

        {/* 后台管理布局（受保护） */}
        <Route
          element={
            <AuthGuard>
              <AdminLayout />
            </AuthGuard>
          }
        >
          {renderRoutes(routes)}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </ConfigProvider>
  );
}

export default App;
