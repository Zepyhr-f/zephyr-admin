-- ----------------------------
-- 禁用外键检查
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 清空数据（按依赖关系逆序删除）
-- ----------------------------
DELETE FROM `sys_role_conflict`;
DELETE FROM `sys_role_hierarchy`;
DELETE FROM `sys_role_dept`;
DELETE FROM `sys_role_menu`;
DELETE FROM `sys_user_role`;
DELETE FROM `sys_menu`;
DELETE FROM `sys_role`;
DELETE FROM `sys_user`;
DELETE FROM `sys_dept`;

-- ----------------------------
-- 启用外键检查
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================
-- 1. 部门
-- ===============================
INSERT INTO `sys_dept` (id, parent_id, dept_name, order_num, status, create_user) VALUES
(100, 0, 'Zephyr集团', 1, 1, 1),
(101, 100, '总经办', 1, 1, 1),
(102, 100, '研发部', 2, 1, 1);

-- ===============================
-- 2. 用户
-- ===============================
-- 密码均为: 123456 (BCrypt加密)
INSERT INTO `sys_user` (id, dept_id, username, real_name, password, email, phone, sex, status, create_user) VALUES
(1, 101, 'admin', '超级管理员', '$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin@zephyr.com', '15800000000', 1, 1, 1),
(2, 102, 'ry', '若依', '$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'ry@zephyr.com', '15800000001', 2, 1, 1);

-- ===============================
-- 3. 角色
-- ===============================
INSERT INTO `sys_role` (id, role_name, role_code, role_sort, data_scope, status, create_user) VALUES
(1, '超级管理员', 'admin', 1, 1, 1, 1),
(2, '普通角色', 'common', 2, 2, 1, 1);

-- ===============================
-- 4. 用户-角色关联
-- ===============================
INSERT INTO `sys_user_role` (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- ===============================
-- 5. 菜单/权限
-- ===============================
INSERT INTO `sys_menu` (id, parent_id, menu_name, menu_type, path, perms, order_num, status, create_user) VALUES
(1, 0, '系统管理', 'M', 'system', NULL, 1, 1, 1),
(2, 1, '用户管理', 'C', 'user', 'sys:user:list', 1, 1, 1),
(3, 1, '角色管理', 'C', 'role', 'sys:role:list', 2, 1, 1),
(4, 2, '用户查询', 'F', NULL, 'sys:user:query', 1, 1, 1),
(5, 2, '用户新增', 'F', NULL, 'sys:user:add', 2, 1, 1),
(6, 2, '用户修改', 'F', NULL, 'sys:user:edit', 3, 1, 1);

-- ===============================
-- 6. 角色-菜单关联
-- ===============================
INSERT INTO `sys_role_menu` (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(2, 1), (2, 2), (2, 4);

-- ===============================
-- 7. 角色继承 (高级特性展示)
-- ===============================
-- admin 继承 common 角色权限
INSERT INTO `sys_role_hierarchy` (parent_role_id, child_role_id) VALUES
(1, 2);

-- ===============================
-- 8. 角色冲突 (高级特性展示)
-- ===============================
-- 假设存在一个临时审计角色 ID=3，与管理员冲突 (此处仅作占位示意)
-- INSERT INTO `sys_role_conflict` (role_id_a, role_id_b) VALUES (1, 3);