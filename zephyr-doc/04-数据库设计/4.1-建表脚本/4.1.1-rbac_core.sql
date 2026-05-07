-- ----------------------------
-- 删除表（按依赖关系逆序）
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_role_dept`;
DROP TABLE IF EXISTS `zephyr_sys_role_menu`;
DROP TABLE IF EXISTS `zephyr_sys_user_post`;
DROP TABLE IF EXISTS `zephyr_sys_user_role`;
DROP TABLE IF EXISTS `zephyr_sys_menu`;
DROP TABLE IF EXISTS `zephyr_sys_post`;
DROP TABLE IF EXISTS `zephyr_sys_role`;
DROP TABLE IF EXISTS `zephyr_sys_user`;
DROP TABLE IF EXISTS `zephyr_sys_dept`;

-- ----------------------------
-- 1. 部门表
-- ----------------------------
CREATE TABLE `zephyr_sys_dept` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code`        VARCHAR(64)  NOT NULL COMMENT '部门编码',
  `parent_code` VARCHAR(64)  DEFAULT '0' COMMENT '父部门编码',
  `dept_name`   VARCHAR(50)  NOT NULL COMMENT '部门名称',
  `full_name`   VARCHAR(255) NOT NULL COMMENT '部门全称',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '部门状态（1=正常 0=停用）',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户ID',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code_del` (`dept_code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 2. 岗位信息表
-- ----------------------------
CREATE TABLE `zephyr_sys_post` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code`        VARCHAR(64)  NOT NULL COMMENT '岗位编码',
  `post_name`   VARCHAR(50)  NOT NULL COMMENT '岗位名称',
  `order_num`   INT          NOT NULL COMMENT '显示顺序',
  `status`      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态（1=正常 0=停用）',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户ID',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志（0=正常 1=删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_code_del` (`post_code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

-- ----------------------------
-- 3. 用户表
-- ----------------------------
CREATE TABLE `zephyr_sys_user` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID',
  `code`        VARCHAR(64)  NOT NULL COMMENT '员工编码',
  `nick_name`   VARCHAR(50)  NOT NULL COMMENT '昵称',
  `real_name`   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
  `password`    VARCHAR(100) NOT NULL COMMENT '密码',
  `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `sex`         TINYINT(1)   DEFAULT 0 COMMENT '性别（0=男 1=女）',
  `birthday`    DATE         DEFAULT NULL COMMENT '出生日期',
  `user_type`   TINYINT(1)   DEFAULT 0 COMMENT '用户类型（0=员工 1=管理员 2=系统）',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '帐号状态（1=正常 0=停用）',
  `dept_code`   VARCHAR(64)  DEFAULT NULL COMMENT '所属部门编码',
  `post_code`   VARCHAR(64)  DEFAULT NULL COMMENT '所属岗位编码 (逻辑关联 sys_post.code)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username_del` (`user_code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 4. 角色表
-- ----------------------------
CREATE TABLE `zephyr_sys_role` (
  `id`          BIGINT       NOT NULL COMMENT '角色ID',
  `code`        VARCHAR(64)  NOT NULL COMMENT '角色编码',
  `role_name`   VARCHAR(30)  NOT NULL COMMENT '角色名称',
  `order_num`   INT          NOT NULL COMMENT '显示顺序',
  `status`      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '角色状态（1=正常 0=停用）',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code_del` (`role_code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 5. 菜单表
-- ----------------------------
CREATE TABLE `zephyr_sys_menu` (
  `id`          BIGINT       NOT NULL COMMENT '菜单ID',
  `code`        VARCHAR(64)  NOT NULL COMMENT '菜单编码',
  `parent_code` VARCHAR(64)  DEFAULT '-1' COMMENT '父菜单编码',
  `menu_name`   VARCHAR(30)  NOT NULL COMMENT '菜单名称',
  `menu_type`   CHAR(1)      DEFAULT 'M' COMMENT '菜单类型（M=目录 C=菜单 F=按钮/API）',
  `path`        VARCHAR(200) DEFAULT '' COMMENT '路由地址',
  `component`   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `perms`       VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `icon`        VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '菜单状态（1=正常 0=停用）',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code_del` (`menu_code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单与权限规则表';

-- ----------------------------
-- 6. 租户信息表
-- ----------------------------
CREATE TABLE `zephyr_sys_tenant` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code`            VARCHAR(12)  NOT NULL COMMENT '租户编号 (业务唯一标识)',
  `tenant_name`     VARCHAR(100) NOT NULL COMMENT '租户名称/公司名称',
  `contact_user`    VARCHAR(50)  DEFAULT NULL COMMENT '联系人',
  `contact_phone`   VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
  `status`          TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '租户状态（1=正常 0=停用）',
  `expire_time`     DATETIME     DEFAULT NULL COMMENT '授权过期时间',
  `account_count`   INT          DEFAULT -1 COMMENT '账号额度（-1表示无限制）',
  `remark`          VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_user`     BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`     BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code_del` (`tenant_code`, `if_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- ----------------------------
-- 7. 关联关系表 (增加索引，无物理外键)
-- ----------------------------
CREATE TABLE `zephyr_sys_user_role` (
  `user_code` VARCHAR(64) NOT NULL COMMENT '用户编码',
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`user_code`, `role_code`),
  INDEX `idx_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

CREATE TABLE `zephyr_sys_user_post` (
  `user_code` VARCHAR(64) NOT NULL COMMENT '用户编码',
  `post_code` VARCHAR(64) NOT NULL COMMENT '岗位编码',
  PRIMARY KEY (`user_code`, `post_code`),
  INDEX `idx_post_code` (`post_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

CREATE TABLE `zephyr_sys_role_menu` (
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
  `menu_code` VARCHAR(64) NOT NULL COMMENT '菜单编码',
  PRIMARY KEY (`role_code`, `menu_code`),
  INDEX `idx_menu_code` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

CREATE TABLE `zephyr_sys_role_dept` (
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
  `dept_code` VARCHAR(64) NOT NULL COMMENT '部门编码',
  PRIMARY KEY (`role_code`, `dept_code`),
  INDEX `idx_dept_code` (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';
