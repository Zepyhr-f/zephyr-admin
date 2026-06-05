import type { ReactNode } from "react";
import {
  ApartmentOutlined,
  CodeOutlined,
  DashboardOutlined,
  DatabaseOutlined,
  FileSearchOutlined,
  FileTextOutlined,
  FolderOpenOutlined,
  IdcardOutlined,
  MenuOutlined,
  NotificationOutlined,
  SafetyCertificateOutlined,
  ScheduleOutlined,
  SettingOutlined,
  TeamOutlined,
} from "@ant-design/icons";

import { DashboardOverview } from "@/pages/dashboard/Overview";
import { UserManagement } from "@/pages/system/UserManagement";
import { DepartmentManagement } from "@/pages/system/DepartmentManagement";
import { PostManagement } from "@/pages/system/PostManagement";
import { MenuManagement } from "@/pages/system/MenuManagement";
import { RoleManagement } from "@/pages/system/RoleManagement";

import { LoginLog } from "@/pages/security/LoginLog";
import { OperationLog } from "@/pages/security/OperationLog";
import { OnlineUsers } from "@/pages/security/OnlineUsers";

import { ServiceMonitoring } from "@/pages/monitor/ServiceMonitoring";
import { CacheMonitoring } from "@/pages/monitor/CacheMonitoring";
import { DataSourceMonitoring } from "@/pages/monitor/DataSourceMonitoring";
import { CronJobs } from "@/pages/monitor/CronJobs";

import { Dictionary } from "@/pages/infrastructure/Dictionary";
import { Params } from "@/pages/infrastructure/Params";
import { FileCenter } from "@/pages/infrastructure/FileCenter";
import { Notices } from "@/pages/infrastructure/Notices";

import { Codegen } from "@/pages/devtools/Codegen";
import { ApiDoc } from "@/pages/devtools/ApiDoc";
import { SqlTerminal } from "@/pages/devtools/SqlTerminal";

export type AppRoute = {
  key: string;
  path: string;
  label: string;
  icon?: ReactNode;
  element?: ReactNode;
  children?: AppRoute[];
};

export const routes: AppRoute[] = [
  {
    key: "dashboard",
    path: "/dashboard",
    label: "概览",
    icon: <DashboardOutlined />,
    element: <DashboardOverview />,
  },
  {
    key: "core-admin",
    path: "/system",
    label: "系统管理",
    icon: <SettingOutlined />,
    children: [
      { key: "user", path: "/system/users", label: "用户管理", icon: <TeamOutlined />, element: <UserManagement /> },
      { key: "dept", path: "/system/depts", label: "部门管理", icon: <ApartmentOutlined />, element: <DepartmentManagement /> },
      { key: "post", path: "/system/posts", label: "岗位管理", icon: <IdcardOutlined />, element: <PostManagement /> },
      { key: "menu", path: "/system/menus", label: "菜单管理", icon: <MenuOutlined />, element: <MenuManagement /> },
      { key: "role", path: "/system/roles", label: "角色管理", icon: <SafetyCertificateOutlined />, element: <RoleManagement /> },
    ],
  },
  {
    key: "security-audit",
    path: "/security",
    label: "安全审计",
    icon: <FileSearchOutlined />,
    children: [
      { key: "login-log", path: "/security/login-log", label: "登录日志", icon: <FileTextOutlined />, element: <LoginLog /> },
      { key: "op-log", path: "/security/op-log", label: "操作日志", icon: <FileTextOutlined />, element: <OperationLog /> },
      { key: "online", path: "/security/online", label: "在线用户", icon: <TeamOutlined />, element: <OnlineUsers /> },
    ],
  },
  {
    key: "monitor",
    path: "/monitor",
    label: "系统监控",
    icon: <DashboardOutlined />,
    children: [
      { key: "server", path: "/monitor/server", label: "服务监控", icon: <DashboardOutlined />, element: <ServiceMonitoring /> },
      { key: "cache", path: "/monitor/cache", label: "缓存监控", icon: <DatabaseOutlined />, element: <CacheMonitoring /> },
      { key: "datasource", path: "/monitor/datasource", label: "数据源监控", icon: <DatabaseOutlined />, element: <DataSourceMonitoring /> },
      { key: "cron", path: "/monitor/cron", label: "任务调度", icon: <ScheduleOutlined />, element: <CronJobs /> },
    ],
  },
  {
    key: "infra",
    path: "/infrastructure",
    label: "基础设施",
    icon: <FolderOpenOutlined />,
    children: [
      { key: "dict", path: "/infrastructure/dict", label: "字典管理", icon: <SettingOutlined />, element: <Dictionary /> },
      { key: "param", path: "/infrastructure/params", label: "参数配置", icon: <SettingOutlined />, element: <Params /> },
      { key: "file", path: "/infrastructure/files", label: "文件管理", icon: <FolderOpenOutlined />, element: <FileCenter /> },
      { key: "notice", path: "/infrastructure/notices", label: "通知公告", icon: <NotificationOutlined />, element: <Notices /> },
    ],
  },
  {
    key: "devtools",
    path: "/devtools",
    label: "开发工具",
    icon: <CodeOutlined />,
    children: [
      { key: "codegen", path: "/devtools/codegen", label: "代码生成", icon: <CodeOutlined />, element: <Codegen /> },
      { key: "api-doc", path: "/devtools/api-doc", label: "接口文档", icon: <FileSearchOutlined />, element: <ApiDoc /> },
      { key: "sql", path: "/devtools/sql", label: "SQL 终端", icon: <DatabaseOutlined />, element: <SqlTerminal /> },
    ],
  },
];
