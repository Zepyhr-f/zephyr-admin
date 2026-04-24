import { Routes, Route, Navigate } from "react-router";
import { ConfigProvider, theme as antdTheme } from "antd";
import { useThemeStore } from "@/store/use-theme-store";
import { antdLightToken } from "@/theme/antd-token";
import { antdDarkToken } from "@/theme/dark-token";
import AdminLayout from "@/layouts/admin-layout";
import BlankLayout from "@/layouts/blank-layout";
import LoginPage from "@/pages/login";
import DashboardPage from "@/pages/dashboard";
import AuthGuard from "@/components/auth-guard";

function App() {
  const { isDark } = useThemeStore();

  return (
    <ConfigProvider
      theme={{
        algorithm: isDark ? antdTheme.darkAlgorithm : antdTheme.defaultAlgorithm,
        ...(isDark ? antdDarkToken : antdLightToken),
      }}
    >
      <Routes>
        {/* 登录页（空白布局） */}
        <Route element={<BlankLayout />}>
          <Route path="/login" element={<LoginPage />} />
        </Route>

        {/* 后台管理布局 */}
        <Route
          element={
            <AuthGuard>
              <AdminLayout />
            </AuthGuard>
          }
        >
          <Route path="/" element={<DashboardPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
        </Route>

        {/* 通配符重定向 */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </ConfigProvider>
  );
}

export default App;
