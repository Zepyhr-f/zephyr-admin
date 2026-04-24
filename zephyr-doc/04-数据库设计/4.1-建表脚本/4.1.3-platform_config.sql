-- ----------------------------
-- 1. 字典表 (旧版，建议参考 infrastructure_module.sql 中的双表设计)
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_dict`;
CREATE TABLE `zephyr_sys_dict`  (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `parent_id`     BIGINT       DEFAULT 0 COMMENT '父主键',
  `code`          VARCHAR(255) DEFAULT NULL COMMENT '字典码',
  `dict_key`      VARCHAR(255) DEFAULT NULL COMMENT '字典值',
  `dict_value`    VARCHAR(255) DEFAULT NULL COMMENT '字典名称',
  `sort`          INT          DEFAULT 0 COMMENT '排序',
  `remark`        VARCHAR(255) DEFAULT NULL COMMENT '字典备注',
  `status`        TINYINT(1)   DEFAULT 1 COMMENT '状态 (1-正常, 0-禁用)',
  
  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',
  
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '字典表';

-- ----------------------------
-- 2. 岗位表 (已在 rbac_core.sql 中定义，此处保留并对齐规范)
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_post_bak`;
CREATE TABLE `zephyr_sys_post_bak`  (
  `id`            BIGINT       NOT NULL COMMENT '主键ID',
  `category`      INT          DEFAULT NULL COMMENT '岗位类型',
  `post_code`     VARCHAR(64)  NOT NULL COMMENT '岗位编号',
  `post_name`     VARCHAR(64)  NOT NULL COMMENT '岗位名称',
  `sort`          INT          DEFAULT 0 COMMENT '岗位排序',
  `remark`        VARCHAR(255) DEFAULT NULL COMMENT '岗位描述',
  `status`        TINYINT(1)   DEFAULT 1 COMMENT '状态 (1-正常, 0-禁用)',

  -- 基础审计与租户字段
  `tenant_code`   VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`   BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`   BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_code_del` (`post_code`, `if_deleted`, `tenant_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '岗位表(备用)';
