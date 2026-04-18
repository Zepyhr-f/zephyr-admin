-- ----------------------------
-- 1. 安全审计顶级菜单 (ID=200)
-- ----------------------------
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (200, 0, '安全审计', 'M', '/audit', NULL, NULL, 'safetyCertificate', 5, 1, 1, NOW());

-- 2.1 登录日志 (ID=201)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (201, 200, '登录日志', 'C', 'login', 'system/login-log', 'sys:loginlog:list', 'login', 1, 1, 1, NOW());

-- 2.2 操作日志 (ID=202)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (202, 200, '操作日志', 'C', 'oper', 'system/oper-log', 'sys:operlog:list', 'file-text', 2, 1, 1, NOW());

-- 2.3 在线用户 (ID=203)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (203, 200, '在线用户', 'C', 'online', 'system/online', 'sys:online:list', 'user', 3, 1, 1, NOW());

-- ----------------------------
-- 2. 系统监控顶级菜单 (ID=300)
-- ----------------------------
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (300, 0, '系统监控', 'M', '/monitor', NULL, NULL, 'dashboard', 6, 1, 1, NOW());

-- 3.1 服务监控 (ID=301)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (301, 300, '服务监控', 'C', 'server', 'monitor/server', 'monitor:server:list', 'chart', 1, 1, 1, NOW());

-- 3.2 缓存监控 (ID=302)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (302, 300, '缓存监控', 'C', 'cache', 'monitor/cache', 'monitor:cache:list', 'database', 2, 1, 1, NOW());

-- 3.3 数据源监控 (ID=303)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (303, 300, '数据源监控', 'C', 'druid', 'monitor/druid', 'monitor:druid:list', 'table', 3, 1, 1, NOW());

-- 3.4 定时任务 (ID=304)
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, status, create_user, create_time)
VALUES (304, 300, '定时任务', 'C', 'job', 'monitor/job', 'monitor:job:list', 'history', 4, 1, 1, NOW());

-- ----------------------------
-- 3. 系统内置管理员授权 (Role ID=1)
-- ----------------------------
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 200), (1, 201), (1, 202), (1, 203);
INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, 300), (1, 301), (1, 302), (1, 303), (1, 304);
