-- ----------------------------
-- 删除表（按依赖关系逆序）
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_conflict`;
DROP TABLE IF EXISTS `sys_role_hierarchy`;
DROP TABLE IF EXISTS `sys_role_dept`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_dept`;

-- ----------------------------
-- 1. 部门表
-- ----------------------------
CREATE TABLE `sys_dept` (
  `id`          BIGINT       NOT NULL COMMENT '部门ID',
  `parent_id`   BIGINT       DEFAULT 0 COMMENT '父部门ID',
  `dept_name`   VARCHAR(50)  NOT NULL COMMENT '部门名称',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '部门状态（1=正常 0=停用）',
  `create_user` BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志（0=正常 1=删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 2. 用户表
-- ----------------------------
CREATE TABLE `sys_user` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID',
  `dept_id`     BIGINT       DEFAULT NULL COMMENT '部门ID',
  `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
  `real_name`   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
  `password`    VARCHAR(100) NOT NULL COMMENT '密码',
  `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `sex`         TINYINT(1)   DEFAULT 0 COMMENT '性别（0=未知 1=男 2=女）',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '帐号状态（1=正常 0=停用）',
  `create_user` BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 3. 角色表
-- ----------------------------
CREATE TABLE `sys_role` (
  `id`          BIGINT       NOT NULL COMMENT '角色ID',
  `role_name`   VARCHAR(30)  NOT NULL COMMENT '角色名称',
  `role_code`   VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort`   INT          NOT NULL COMMENT '显示顺序',
  `data_scope`  TINYINT(1)   DEFAULT 1 COMMENT '数据范围（1=全部 2=自定 3=本部门 4=本部门及下级 5=仅本人）',
  `status`      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '角色状态（1=正常 0=停用）',
  `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_user` BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志（0=正常 1=删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 4. 菜单与权限表
-- ----------------------------
CREATE TABLE `sys_menu` (
  `id`          BIGINT       NOT NULL COMMENT '菜单ID',
  `parent_id`   BIGINT       DEFAULT 0 COMMENT '父菜单ID',
  `menu_name`   VARCHAR(50)  NOT NULL COMMENT '菜单名称',
  `menu_type`   CHAR(1)      DEFAULT '' COMMENT '菜单类型（M=目录 C=菜单 F=按钮/API）',
  `path`        VARCHAR(200) DEFAULT '' COMMENT '路由地址',
  `component`   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `perms`       VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `icon`        VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '菜单状态（1=正常 0=停用）',
  `create_user` BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志（0=正常 1=删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单与权限规则表';

-- ----------------------------
-- 5. 关联关系表（无需审计字段）
-- ----------------------------
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

CREATE TABLE `sys_role_dept` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `dept_id` BIGINT NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

-- ----------------------------
-- 6. 高级特性表
-- ----------------------------
CREATE TABLE `sys_role_hierarchy` (
  `parent_role_id` BIGINT NOT NULL COMMENT '父角色ID',
  `child_role_id`  BIGINT NOT NULL COMMENT '子角色ID',
  PRIMARY KEY (`parent_role_id`, `child_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色继承关系表';

CREATE TABLE `sys_role_conflict` (
  `role_id_a` BIGINT NOT NULL COMMENT '冲突角色A',
  `role_id_b` BIGINT NOT NULL COMMENT '冲突角色B',
  PRIMARY KEY (`role_id_a`, `role_id_b`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色互斥约束表';
