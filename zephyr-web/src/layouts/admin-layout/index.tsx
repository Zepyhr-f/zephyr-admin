import { useState } from "react";
import { Outlet, useNavigate, useLocation } from "react-router";
import {
  Layout,
  Menu,
  Button,
  Avatar,
  Dropdown,
  Badge,
  Breadcrumb,
} from "antd";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  HomeOutlined,
  DashboardOutlined,
  SettingOutlined,
  UserOutlined,
  LogoutOutlined,
  BellOutlined,
  MoonOutlined,
  SunOutlined,
  FolderOutlined,
  FileOutlined,
} from "@ant-design/icons";
import { useAuthStore, type MenuItem } from "@/store/use-auth-store";
import { useThemeStore } from "@/store/use-theme-store";
import Logo from "@/components/logo";
import { GLOBAL_CONFIG } from "@/global-config";
import client from "@/api/client";

const { Header, Sider, Content } = Layout;

// 图标映射表（简化版，可根据需要扩展）
const iconMap: Record<string, React.ReactNode> = {
  home: <HomeOutlined />,
  dashboard: <DashboardOutlined />,
  setting: <SettingOutlined />,
  user: <UserOutlined />,
  folder: <FolderOutlined />,
  file: <FileOutlined />,
};

function getIcon(icon?: string): React.ReactNode {
  if (!icon) return null;
  return iconMap[icon] || <FolderOutlined />;
}

// 递归转换后端菜单为 Ant Design Menu 格式
function convertMenus(menus?: MenuItem[], parentPath = ""): any[] {
  if (!menus) return [];
  return menus.map((menu) => {
    const fullPath = parentPath + (menu.path.startsWith("/") ? menu.path : `/${menu.path}`);
    const item: any = {
      key: fullPath,
      icon: getIcon(menu.meta?.icon),
      label: menu.meta?.title || menu.name || menu.path,
    };
    if (menu.children && menu.children.length > 0) {
      item.children = convertMenus(menu.children, fullPath);
    }
    return item;
  });
}

function breadcrumbFromPath(pathname: string, menus?: MenuItem[]): any[] {
  if (pathname === "/") return [{ title: "首页" }];
  if (pathname === "/dashboard") return [{ title: "数据看板" }];

  // 尝试从菜单中匹配
  if (menus) {
    for (const menu of menus) {
      const parentTitle = menu.meta?.title || menu.name;
      if (menu.children) {
        for (const child of menu.children) {
          const childPath = (menu.path + (child.path.startsWith("/") ? child.path : `/${child.path}`)).replace(/\/+/g, "/");
          if (pathname === childPath || pathname.startsWith(childPath + "/")) {
            return [
              { title: parentTitle || "系统管理" },
              { title: child.meta?.title || child.name || child.path },
            ];
          }
        }
      }
      if (pathname === menu.path || pathname.startsWith(menu.path + "/")) {
        return [{ title: parentTitle || menu.path }];
      }
    }
  }

  return [{ title: "首页" }];
}

export default function AdminLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { logout, user, menus } = useAuthStore();
  const { isDark, toggleTheme } = useThemeStore();

  const menuItems = [
    {
      key: "/",
      icon: <HomeOutlined />,
      label: "首页",
    },
    {
      key: "/dashboard",
      icon: <DashboardOutlined />,
      label: "数据看板",
    },
    ...convertMenus(menus),
  ];

  const userMenuItems = [
    {
      key: "profile",
      icon: <UserOutlined />,
      label: "个人中心",
    },
    {
      key: "settings",
      icon: <SettingOutlined />,
      label: "账号设置",
    },
    { type: "divider" as const },
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "退出登录",
      danger: true,
    },
  ];

  const handleLogout = async () => {
    try {
      await client.post('zephyr-auth/logout');
    } catch (e) {
      console.error('Logout request failed:', e);
    } finally {
      logout();
      window.location.href = '/login';
    }
  };

  return (
    <Layout className="min-h-screen">
      {/* 侧边栏 */}
      <Sider
        trigger={null}
        collapsible
        collapsed={collapsed}
        theme="light"
        className="!bg-[var(--color-bg-container)] border-r border-[var(--color-border-secondary)]"
        style={{
          boxShadow: "var(--shadow-sidebar)",
        }}
      >
        <div className="h-16 flex items-center justify-center border-b border-[var(--color-border-secondary)]">
          <div className="flex items-center gap-2">
            <Logo size={collapsed ? 32 : 28} />
            {!collapsed && (
              <span className="text-lg font-semibold text-[var(--color-text-primary)] truncate">
                {GLOBAL_CONFIG.appName}
              </span>
            )}
          </div>
        </div>
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          defaultOpenKeys={menus?.map((m) => m.path) || ["/system"]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
          className="!border-r-0"
        />
      </Sider>

      <Layout>
        {/* 顶栏 */}
        <Header
          className="!bg-[var(--color-bg-container)] !px-6 flex items-center justify-between border-b border-[var(--color-border-secondary)]"
          style={{ height: 64 }}
        >
          <div className="flex items-center gap-4">
            <Button
              type="text"
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              className="text-[var(--color-text-secondary)]"
            />
            <Breadcrumb
              items={breadcrumbFromPath(location.pathname, menus)}
              className="hidden sm:flex"
            />
          </div>

          <div className="flex items-center gap-3">
            <Button
              type="text"
              icon={isDark ? <SunOutlined /> : <MoonOutlined />}
              onClick={toggleTheme}
              className="text-[var(--color-text-secondary)]"
              title={isDark ? "切换亮色" : "切换暗色"}
            />
            <Badge count={5} size="small">
              <Button
                type="text"
                icon={<BellOutlined />}
                className="text-[var(--color-text-secondary)]"
              />
            </Badge>
            <Dropdown
              menu={{
                items: userMenuItems,
                onClick: ({ key }) => {
                  if (key === "logout") handleLogout();
                },
              }}
              placement="bottomRight"
            >
              <div className="flex items-center gap-2 cursor-pointer hover:bg-[var(--color-bg-layout)] px-2 py-1 rounded-lg transition-colors">
                <Avatar size="small" icon={<UserOutlined />} />
                <span className="text-sm text-[var(--color-text-primary)] hidden md:inline">
                  {user?.username || "Admin"}
                </span>
              </div>
            </Dropdown>
          </div>
        </Header>

        {/* 内容区 */}
        <Content className="m-4 p-6 bg-[var(--color-bg-container)] rounded-xl shadow-[var(--shadow-card)] min-h-[calc(100vh-112px)]">
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
