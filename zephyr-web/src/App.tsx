import { BrowserRouter, Routes, Route, Navigate } from 'react-router';
import LoginPage from '@/pages/login';
import DashboardPage from '@/pages/dashboard';
import AuthGuard from '@/components/auth-guard';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 登录页面 */}
        <Route path="/login" element={<LoginPage />} />

        {/* 受保护的路由 */}
        <Route
          path="/"
          element={
            <AuthGuard>
              <DashboardPage />
            </AuthGuard>
          }
        />

        {/* 通配符重定向 */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
