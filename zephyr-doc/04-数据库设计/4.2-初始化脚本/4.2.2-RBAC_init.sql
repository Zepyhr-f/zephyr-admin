-- ----------------------------
-- 禁用外键检查
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 清空数据（按依赖关系逆序删除）
-- ----------------------------
DELETE FROM `zephyr_sys_role_dept`;
DELETE FROM `zephyr_sys_role_menu`;
DELETE FROM `zephyr_sys_user_role`;
DELETE FROM `zephyr_sys_menu`;
DELETE FROM `zephyr_sys_role`;
DELETE FROM `zephyr_sys_user`;
DELETE FROM `zephyr_sys_dept`;
DELETE FROM `zephyr_sys_tenant`;

-- ----------------------------
-- 启用外键检查
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================
-- 0. 租户信息
-- ===============================
INSERT INTO `zephyr_sys_tenant` (id, code, tenant_name, status, create_user) VALUES
(1, '000000', '默认租户', 1, 1);

-- ===============================
-- 1. 部门
-- ===============================
INSERT INTO `zephyr_sys_dept` (id, code, parent_code, dept_name, full_name, order_num, status, tenant_code, create_user) VALUES
(100, 'ZEPHYR', '0', 'Zephyr集团', 'Zephyr集团总部', 1, 1, '000000', 1),
(101, 'ZEPHYR_CEO', 'ZEPHYR', '总经办', 'Zephyr集团总经办', 1, 1, '000000', 1),
(102, 'ZEPHYR_RD', 'ZEPHYR', '研发部', 'Zephyr集团研发部', 2, 1, '000000', 1);

-- ===============================
-- 2. 用户
-- ===============================
-- 密码均为: 123456 (BCrypt加密)
INSERT INTO `zephyr_sys_user` (id, code, nick_name, real_name, password, email, phone, sex, status, dept_code, tenant_code, create_user) VALUES
(1, 'admin', 'admin', '超级管理员', '$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin@zephyr.com', '15800000000', 0, 1, 'ZEPHYR_CEO', '000000', 1),
(2, 'ry', 'ry', '若依', '$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'ry@zephyr.com', '15800000001', 1, 1, 'ZEPHYR_RD', '000000', 1);

-- ===============================
-- 3. 角色
-- ===============================
INSERT INTO `zephyr_sys_role` (id, code, role_name, order_num, status, tenant_code, create_user) VALUES
(1, 'admin', '超级管理员', 1, 1, '000000', 1),
(2, 'common', '普通角色', 2, 1, '000000', 1);

-- ===============================
-- 4. 用户-角色关联 (使用编码关联)
-- ===============================
INSERT INTO `zephyr_sys_user_role` (user_code, role_code) VALUES
('admin', 'admin'),
('ry', 'common');

-- ===============================
-- 5. 菜单/权限
-- ===============================
INSERT INTO `zephyr_sys_menu` (id, code, parent_code, menu_name, menu_type, path, perms, order_num, status, tenant_code, create_user) VALUES
(1, 'system', '-1', '系统管理', 'M', 'system', NULL, 1, 1, '000000', 1),
(2, 'user', 'system', '用户管理', 'C', 'user', 'sys:user:list', 1, 1, '000000', 1),
(3, 'role', 'system', '角色管理', 'C', 'role', 'sys:role:list', 2, 1, '000000', 1),
(4, 'user_query', 'user', '用户查询', 'F', NULL, 'sys:user:query', 1, 1, '000000', 1),
(5, 'user_add', 'user', '用户新增', 'F', NULL, 'sys:user:add', 2, 1, '000000', 1),
(6, 'user_edit', 'user', '用户修改', 'F', NULL, 'sys:user:edit', 3, 1, '000000', 1);

-- ===============================
-- 6. 角色-菜单关联 (使用编码关联)
-- ===============================
INSERT INTO `zephyr_sys_role_menu` (role_code, menu_code) VALUES
('admin', 'system'), ('admin', 'user'), ('admin', 'role'), ('admin', 'user_query'), ('admin', 'user_add'), ('admin', 'user_edit'),
('common', 'system'), ('common', 'user'), ('common', 'user_query');