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
          Button: {
            // 默认按钮：白底（暗黑模式下透明或深灰），悬浮变深色
            defaultBg: isDark ? "rgba(255,255,255,0.04)" : "#FFFFFF",
            defaultHoverBg: isDark ? "rgba(255,255,255,0.12)" : "#F1F5F9",
            defaultActiveBg: isDark ? "rgba(255,255,255,0.16)" : "#E2E8F0",
            defaultHoverBorderColor: isDark ? "#475569" : "#CBD5E1",
            defaultHoverColor: isDark ? "#E2E8F0" : "#334155",
            
            // 无底色按钮 (text/link)：选中/悬浮变成极淡的浅蓝色 (跟随独立的按钮配置色)
            textHoverBg: isDark ? "rgba(255,255,255,0.06)" : "color-mix(in srgb, var(--z-button-hover) 6%, transparent)",
            textActiveBg: isDark ? "rgba(255,255,255,0.12)" : "color-mix(in srgb, var(--z-button-hover) 12%, transparent)",
          },
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
