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
DROP TABLE IF EXISTS `zephyr_sys_tenant`;

-- ----------------------------
-- 1. 租户信息表（系统级，tenant_code 固定为 000000）
--    继承基类：BaseEntity + code（业务唯一标识）
-- ----------------------------
CREATE TABLE `zephyr_sys_tenant` (
  `id`              BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`            VARCHAR(12)  NOT NULL COMMENT '租户编号 (业务唯一标识)',
  `tenant_name`     VARCHAR(100) NOT NULL COMMENT '租户名称/公司名称',
  `contact_user`    VARCHAR(50)  DEFAULT NULL COMMENT '联系人',
  `contact_phone`   VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
  `status`          TINYINT(1)   DEFAULT 1 COMMENT '租户状态 (1=正常 0=停用)',
  `expire_time`     DATETIME     DEFAULT NULL COMMENT '授权过期时间',
  `account_count`   INT          DEFAULT -1 COMMENT '账号额度 (-1表示无限制)',
  `remark`          VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `tenant_code`     VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码 (系统级固定000000)',
  `create_user`     VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`     VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- ----------------------------
-- 2. 部门表
--    继承基类：TreeEntity (BaseEntity + code + parent_code + leaf)
-- ----------------------------
CREATE TABLE `zephyr_sys_dept` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`        VARCHAR(64)  NOT NULL COMMENT '部门编码 (业务唯一标识)',
  `parent_code` VARCHAR(64)  DEFAULT NULL COMMENT '父部门编码 (NULL表示根节点)',
  `leaf`        TINYINT(1)   DEFAULT 1 COMMENT '是否叶子节点 (1=是 0=否)',
  `dept_name`   VARCHAR(50)  NOT NULL COMMENT '部门名称',
  `full_name`   VARCHAR(255) DEFAULT NULL COMMENT '部门全称',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '部门状态 (1=正常 0=停用)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`, `tenant_code`),
  INDEX `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 3. 岗位信息表
--    继承基类：CodeEntity (BaseEntity + code)
-- ----------------------------
CREATE TABLE `zephyr_sys_post` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`        VARCHAR(64)  NOT NULL COMMENT '岗位编码 (业务唯一标识)',
  `post_name`   VARCHAR(50)  NOT NULL COMMENT '岗位名称',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '状态 (1=正常 0=停用)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

-- ----------------------------
-- 4. 用户表
--    继承基类：CodeEntity (BaseEntity + code)
-- ----------------------------
CREATE TABLE `zephyr_sys_user` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`        VARCHAR(64)  NOT NULL COMMENT '员工编码 (业务唯一标识)',
  `nick_name`   VARCHAR(50)  NOT NULL COMMENT '昵称',
  `real_name`   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
  `password`    VARCHAR(100) NOT NULL COMMENT '密码',
  `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `sex`         TINYINT(1)   DEFAULT 0 COMMENT '性别 (0=男 1=女)',
  `birthday`    DATE         DEFAULT NULL COMMENT '出生日期',
  `user_type`   TINYINT(1)   DEFAULT 0 COMMENT '用户类型 (0=员工 1=管理员 2=系统)',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '帐号状态 (1=正常 0=停用)',
  `dept_code`   VARCHAR(64)  DEFAULT NULL COMMENT '所属部门编码 (逻辑关联 sys_dept.code)',
  `post_code`   VARCHAR(64)  DEFAULT NULL COMMENT '所属岗位编码 (逻辑关联 sys_post.code)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 5. 角色表
--    继承基类：CodeEntity (BaseEntity + code)
-- ----------------------------
CREATE TABLE `zephyr_sys_role` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`        VARCHAR(64)  NOT NULL COMMENT '角色编码 (业务唯一标识)',
  `role_name`   VARCHAR(30)  NOT NULL COMMENT '角色名称',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '角色状态 (1=正常 0=停用)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 6. 菜单表
--    继承基类：TreeEntity (BaseEntity + code + parent_code + leaf)
-- ----------------------------
CREATE TABLE `zephyr_sys_menu` (
  `id`          BIGINT       NOT NULL COMMENT '主键ID (雪花算法)',
  `code`        VARCHAR(64)  NOT NULL COMMENT '菜单编码 (业务唯一标识)',
  `parent_code` VARCHAR(64)  DEFAULT NULL COMMENT '父菜单编码 (NULL表示根节点)',
  `leaf`        TINYINT(1)   DEFAULT 1 COMMENT '是否叶子节点 (1=是 0=否)',
  `menu_name`   VARCHAR(30)  NOT NULL COMMENT '菜单名称',
  `menu_type`   CHAR(1)      DEFAULT 'M' COMMENT '菜单类型 (M=目录 C=菜单 F=按钮/API)',
  `path`        VARCHAR(200) DEFAULT '' COMMENT '路由地址',
  `component`   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `perms`       VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `icon`        VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
  `order_num`   INT          DEFAULT 0 COMMENT '显示顺序',
  `status`      TINYINT(1)   DEFAULT 1 COMMENT '菜单状态 (1=正常 0=停用)',
  `tenant_code` VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user` VARCHAR(64)  DEFAULT NULL COMMENT '创建人编码',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` VARCHAR(64)  DEFAULT NULL COMMENT '更新人编码',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识 (0=正常 1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code_del` (`code`, `if_deleted`, `tenant_code`),
  INDEX `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单与权限规则表';

-- ----------------------------
-- 7. 关联关系表 (增加索引，无物理外键)
--    中间表不继承 BaseEntity，使用 xxx_code 关联业务表的 code 字段
-- ----------------------------
CREATE TABLE `zephyr_sys_user_role` (
  `user_code` VARCHAR(64) NOT NULL COMMENT '用户编码 (关联 sys_user.code)',
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码 (关联 sys_role.code)',
  PRIMARY KEY (`user_code`, `role_code`),
  INDEX `idx_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

CREATE TABLE `zephyr_sys_user_post` (
  `user_code` VARCHAR(64) NOT NULL COMMENT '用户编码 (关联 sys_user.code)',
  `post_code` VARCHAR(64) NOT NULL COMMENT '岗位编码 (关联 sys_post.code)',
  PRIMARY KEY (`user_code`, `post_code`),
  INDEX `idx_post_code` (`post_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

CREATE TABLE `zephyr_sys_role_menu` (
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码 (关联 sys_role.code)',
  `menu_code` VARCHAR(64) NOT NULL COMMENT '菜单编码 (关联 sys_menu.code)',
  PRIMARY KEY (`role_code`, `menu_code`),
  INDEX `idx_menu_code` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

CREATE TABLE `zephyr_sys_role_dept` (
  `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码 (关联 sys_role.code)',
  `dept_code` VARCHAR(64) NOT NULL COMMENT '部门编码 (关联 sys_dept.code)',
  PRIMARY KEY (`role_code`, `dept_code`),
  INDEX `idx_dept_code` (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';
