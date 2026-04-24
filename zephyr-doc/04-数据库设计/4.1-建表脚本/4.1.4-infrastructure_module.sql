-- ----------------------------
-- 基础设施模块 (Infrastructure) 建表脚本
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 字典类型表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_dict_type`;
CREATE TABLE `zephyr_sys_dict_type` (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `dict_name`     VARCHAR(100) NOT NULL COMMENT '字典名称',
  `dict_type`     VARCHAR(100) NOT NULL COMMENT '字典类型 (编码)',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '状态 (0=正常, 1=停用)',
  `remark`        VARCHAR(500) DEFAULT NULL COMMENT '备注',
  
  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志 (0=正常, 1=已删除)',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type_del` (`dict_type`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- 2. 字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_dict_data`;
CREATE TABLE `zephyr_sys_dict_data` (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `dict_sort`     INT          DEFAULT 0 COMMENT '字典排序',
  `dict_label`    VARCHAR(100) NOT NULL COMMENT '字典标签',
  `dict_value`    VARCHAR(100) NOT NULL COMMENT '字典键值',
  `dict_type`     VARCHAR(100) NOT NULL COMMENT '字典类型',
  `css_class`     VARCHAR(100) DEFAULT NULL COMMENT '样式属性',
  `list_class`    VARCHAR(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default`    TINYINT(1)   DEFAULT 0 COMMENT '是否默认 (1=是, 0=否)',
  `status`        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '状态 (0=正常, 1=停用)',
  `remark`        VARCHAR(500) DEFAULT NULL COMMENT '备注',
  
  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志 (0=正常, 1=已删除)',
  
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- ----------------------------
-- 3. 参数配置表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_config`;
CREATE TABLE `zephyr_sys_config` (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `config_name`   VARCHAR(100) NOT NULL COMMENT '参数名称',
  `config_key`    VARCHAR(100) NOT NULL COMMENT '参数键名',
  `config_value`  VARCHAR(500) NOT NULL COMMENT '参数键值',
  `config_type`   TINYINT(1)   DEFAULT 0 COMMENT '系统内置 (1=是, 0=否)',
  `remark`        VARCHAR(500) DEFAULT NULL COMMENT '备注',
  
  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志 (0=正常, 1=已删除)',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key_del` (`config_key`, `if_deleted`, `tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

-- ----------------------------
-- 4. 文件表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_file`;
CREATE TABLE `zephyr_sys_file` (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `file_name`     VARCHAR(200) NOT NULL COMMENT '原始文件名',
  `file_path`     VARCHAR(500) NOT NULL COMMENT '存储相对路径',
  `file_url`      VARCHAR(500) NOT NULL COMMENT '访问全路径',
  `file_size`     BIGINT       DEFAULT 0 COMMENT '文件大小 (Byte)',
  `file_suffix`   VARCHAR(20)  DEFAULT NULL COMMENT '扩展名',
  `store_type`    VARCHAR(20)  DEFAULT 'local' COMMENT '存储方案',
  
  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志 (0=正常, 1=已删除)',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件记录表';

-- ----------------------------
-- 5. 通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_notice`;
CREATE TABLE `zephyr_sys_notice` (
  `id`             BIGINT       NOT NULL COMMENT '主键ID',
  `notice_title`   VARCHAR(200) NOT NULL COMMENT '公告标题',
  `notice_type`    TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '公告类型 (1=系统通知, 2=业务提醒)',
  `notice_content` LONGTEXT     NOT NULL COMMENT '公告内容',
  `status`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '公告状态 (0=草稿, 1=已发布, 2=已撤退)',
  `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注',
  
  -- 基础审计与租户字段
  `tenant_code`    VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`    BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`    BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标志 (0=正常, 1=已删除)',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

SET FOREIGN_KEY_CHECKS = 1;
