import { useMemo, useState, useEffect } from "react";
import { Link, Outlet, useLocation, useNavigate } from "react-router";
import {
  Breadcrumb,
  Dropdown,
  Layout,
  Menu,
  Space,
  Typography,
  Button,
  Tabs,
} from "antd";
import type { MenuProps } from "antd";
import {
  ReloadOutlined,
  SettingOutlined,
  LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  MoonOutlined,
  SunOutlined,
  UserOutlined,
  CloseOutlined,
} from "@ant-design/icons";
import { useAuthStore } from "@/store/use-auth-store";
import { useThemeStore } from "@/store/use-theme-store";
import { routes, type AppRoute } from "@/routes/route-config";
import client from "@/api/client";

const { Header, Sider, Content } = Layout;

/* ── helpers ─────────────────────────────────────────────── */

export type TabItem = {
  key: string;
  title: string;
  closable?: boolean;
};

const TABS_CACHE_KEY = "zephyr_tabs";

function loadTabs(): TabItem[] {
  try {
    const raw = localStorage.getItem(TABS_CACHE_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch {
    return [];
  }
}

function saveTabs(tabs: TabItem[]) {
  try {
    localStorage.setItem(TABS_CACHE_KEY, JSON.stringify(tabs));
  } catch {
    // ignore
  }
}

function flattenRoutes(rs: AppRoute[]): AppRoute[] {
  const out: AppRoute[] = [];
  for (const r of rs) {
    out.push(r);
    if (r.children) out.push(...flattenRoutes(r.children));
  }
  return out;
}

function buildMenuItems(rs: AppRoute[]): MenuProps["items"] {
  return rs
    .filter((r) => r.children?.length || r.element)
    .map((r) => {
      if (r.children?.length) {
        return {
          key: r.path,
          icon: r.icon,
          label: r.label,
          children: r.children.map((c) => ({
            key: c.path,
            icon: c.icon,
            label: c.label,
          })),
        };
      }
      return { key: r.path, icon: r.icon, label: r.label };
    });
}

function calcOpenKeys(pathname: string) {
  const seg = pathname.split("/").filter(Boolean)[0];
  return seg ? [`/${seg}`] : [];
}

/* ── component ───────────────────────────────────────────── */

export default function AdminLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { logout, user } = useAuthStore();
  const { isDark, toggleTheme } = useThemeStore();

  const all = useMemo(() => flattenRoutes(routes), []);
  const menuItems = useMemo(() => buildMenuItems(routes), []);

  const homeTab: TabItem = useMemo(
    () => ({ key: "/", title: "概览", closable: false }),
    []
  );
  const [tabs, setTabs] = useState<TabItem[]>(() => {
    const initial = loadTabs();
    const withHome = initial.length ? initial : [homeTab];
    // 兜底：保证有首页
    if (!withHome.some((t) => t.key === "/")) return [homeTab, ...withHome];
    return withHome;
  });

  const selectedKeys = useMemo(
    () => [location.pathname === "/" ? "/" : location.pathname],
    [location.pathname]
  );

  const openKeys = useMemo(
    () => calcOpenKeys(location.pathname),
    [location.pathname]
  );

  const activeTabKey = useMemo(
    () => (location.pathname === "/" ? "/" : location.pathname),
    [location.pathname]
  );

  // 每次进入一个页面，都确保“开一个标签”（若已存在则激活，不重复添加）
  useEffect(() => {
    const pathname = activeTabKey;
    const hit = all.find((r) => r.path === pathname || (pathname === "/" && r.path === "/"));
    const title = hit?.label || (pathname === "/" ? "概览" : pathname);
    const tab: TabItem = {
      key: pathname,
      title,
      closable: pathname !== "/"
    };

    setTabs((prev) => {
      if (prev.some((t) => t.key === tab.key)) return prev;
      const next = [...prev, tab];
      saveTabs(next);
      return next;
    });
  }, [activeTabKey, all]);

  const breadcrumbItems = useMemo(() => {
    const hit = all.find(
      (r) => r.path === location.pathname || (location.pathname === "/" && r.path === "/")
    );
    if (!hit) return [{ title: "Zephyr" }];
    const seg = location.pathname.split("/").filter(Boolean)[0];
    const group = seg ? routes.find((r) => r.path === `/${seg}`) : undefined;
    const list: { title: React.ReactNode }[] = [{ title: <Link to="/">概览</Link> }];
    if (group && group.path !== "/") list.push({ title: group.label });
    if (hit.path !== "/" && hit.label) list.push({ title: hit.label });
    return list;
  }, [all, location.pathname]);

  const handleLogout = async () => {
    try {
      await client.post("zephyr-auth/logout");
    } catch {
      // ignore
    } finally {
      logout();
      window.location.href = "/login";
    }
  };

  const userMenu: MenuProps = {
    items: [
      { key: "profile", label: "个人信息", icon: <UserOutlined /> },
      { type: "divider" },
      { key: "logout", label: "退出登录", icon: <LogoutOutlined />, danger: true },
    ],
    onClick: ({ key }) => {
      if (key === "logout") handleLogout();
    },
  };

  return (
    <Layout className="z-app">
      {/* ── 侧边栏 ─────────────────────────────── */}
      <Sider
        width={260}
        collapsible
        collapsed={collapsed}
        trigger={null}
        style={{
          borderRight: "1px solid var(--z-border)",
          background: "var(--z-surface)",
        }}
      >
        {/* Logo */}
        <div style={{ padding: 16, display: "flex", alignItems: "center", gap: 10 }}>
          <div
            style={{
              width: 28,
              height: 28,
              borderRadius: 10,
              background: "linear-gradient(135deg, var(--z-primary), #3B82F6)",
              boxShadow: "0 8px 18px rgba(30,64,175,.18)",
              flexShrink: 0,
            }}
          />
          {!collapsed && (
            <div style={{ minWidth: 0 }}>
              <Typography.Text strong style={{ display: "block" }}>
                Zephyr Admin
              </Typography.Text>
              <Typography.Text type="secondary" style={{ fontSize: 12 }}>
                后台管理平台
              </Typography.Text>
            </div>
          )}
        </div>

        {/* 菜单 */}
        <Menu
          mode="inline"
          items={menuItems}
          selectedKeys={selectedKeys}
          defaultOpenKeys={openKeys}
          onClick={({ key }) => navigate(String(key))}
          style={{
            background: "transparent",
            borderInlineEnd: "none",
            padding: "0 8px 12px",
          }}
        />
      </Sider>

      <Layout>
        {/* ── 顶栏 ───────────────────────────────── */}
        <Header
          style={{
            padding: "0 16px",
            background: "var(--z-surface)",
            borderBottom: "1px solid var(--z-border)",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <Space size={12}>
            <Typography.Link
              onClick={() => setCollapsed((v) => !v)}
              style={{ fontSize: 16 }}
              aria-label={collapsed ? "展开侧边栏" : "收起侧边栏"}
            >
              {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            </Typography.Link>
            <Button
              type="text"
              icon={<ReloadOutlined />}
              title="刷新当前页面"
              onClick={() => window.location.reload()}
            />
            <Breadcrumb items={breadcrumbItems} />
          </Space>

          <Space size={8}>
            <Button
              type="text"
              icon={isDark ? <SunOutlined /> : <MoonOutlined />}
              onClick={toggleTheme}
              title={isDark ? "切换亮色" : "切换暗色"}
            />
            <Button
              type="text"
              icon={<SettingOutlined />}
              title="系统设置"
              onClick={() => navigate('/todo')}
            />
            <Dropdown menu={userMenu} placement="bottomRight" trigger={["click"]}>
              <Typography.Link>
                <Space>
                  <UserOutlined />
                  {user?.username || "admin"}
                </Space>
              </Typography.Link>
            </Dropdown>
          </Space>
        </Header>

        {/* ── 页面标签栏 ─────────────────────────────── */}
        <div
          style={{
            background: "var(--z-surface)",
            borderBottom: "1px solid var(--z-border)",
            padding: "8px 12px",
            display: "flex",
            alignItems: "center",
            gap: "8px",
            overflowX: "auto"
          }}
        >
          {tabs.map((tab) => {
            const isActive = tab.key === activeTabKey;
            return (
              <div
                key={tab.key}
                onClick={() => navigate(tab.key)}
                style={{
                  height: "28px",
                  padding: "0 12px",
                  borderRadius: "6px",
                  display: "flex",
                  alignItems: "center",
                  gap: "6px",
                  cursor: "pointer",
                  fontSize: "13px",
                  background: isActive ? "var(--z-primary)" : "var(--z-bg)",
                  color: isActive ? "#fff" : "var(--z-text)",
                  border: "1px solid",
                  borderColor: isActive ? "var(--z-primary)" : "var(--z-border)",
                  transition: "all 0.2s",
                  whiteSpace: "nowrap"
                }}
              >
                <span>{tab.title}</span>
                {tab.closable && (
                  <CloseOutlined
                    style={{ fontSize: 10, opacity: 0.6 }}
                    onClick={(e) => {
                      e.stopPropagation();
                      if (tab.key === "/") return;
                      setTabs((prev) => {
                        const idx = prev.findIndex((t) => t.key === tab.key);
                        const next = prev.filter((t) => t.key !== tab.key);
                        saveTabs(next);
                        // 如果关的是当前页，则跳到相邻的那个
                        if (tab.key === activeTabKey) {
                          const fallback = next[Math.max(0, idx - 1)] || next[0] || homeTab;
                          navigate(fallback.key, { replace: true });
                        }
                        return next.length ? next : [homeTab];
                      });
                    }}
                  />
                )}
              </div>
            );
          })}
        </div>

        {/* ── 内容区 ─────────────────────────────── */}
        <Content style={{ overflow: "auto" }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
