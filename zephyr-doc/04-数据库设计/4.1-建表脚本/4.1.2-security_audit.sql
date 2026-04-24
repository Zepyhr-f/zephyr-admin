-- ============================================================================
-- 安全审计日志建表脚本
-- 遵循规范：zephyr-doc/05-开发规范/00-建表规范.md
-- ============================================================================

-- ----------------------------
-- 1. 登录日志表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_login_log`;
CREATE TABLE `zephyr_sys_login_log` (
  -- 主键
  `id`             BIGINT       NOT NULL COMMENT '主键ID',

  -- 业务字段
  `username`       VARCHAR(50)  DEFAULT '' COMMENT '登录账号',
  `ipaddr`         VARCHAR(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` VARCHAR(255) DEFAULT '' COMMENT '登录地点（IP解析）',
  `browser`        VARCHAR(50)  DEFAULT '' COMMENT '浏览器类型',
  `os`             VARCHAR(50)  DEFAULT '' COMMENT '操作系统',
  `status`         TINYINT(1)   DEFAULT 0 COMMENT '登录状态（0=成功 1=失败）',
  `msg`            VARCHAR(255) DEFAULT '' COMMENT '提示消息',
  `login_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',

  -- 基础审计与租户字段
  `tenant_code`    VARCHAR(12)  DEFAULT '000000' COMMENT '租户编码',
  `create_user`    BIGINT       DEFAULT NULL COMMENT '创建人ID',
  `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`    BIGINT       DEFAULT NULL COMMENT '更新人ID',
  `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',

  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录表';

-- ----------------------------
-- 2. 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `zephyr_sys_oper_log`;
CREATE TABLE `zephyr_sys_oper_log` (
  -- 主键
  `id`             BIGINT        NOT NULL COMMENT '主键ID',

  -- 业务字段
  `title`          VARCHAR(50)   DEFAULT '' COMMENT '系统模块名称',
  `business_type`  TINYINT(1)    DEFAULT 0 COMMENT '业务类型（0=其它 1=新增 2=修改 3=删除 4=授权 5=导出 6=导入 7=强退 8=生成代码 9=清空数据）',
  `method`         VARCHAR(100)  DEFAULT '' COMMENT '方法名称',
  `request_method` VARCHAR(10)   DEFAULT '' COMMENT '请求方式（POST/PUT/DELETE等）',
  `operator_type`  TINYINT(1)    DEFAULT 0 COMMENT '操作类别（0=其它 1=后台用户 2=手机端用户）',
  `oper_name`      VARCHAR(50)   DEFAULT '' COMMENT '操作人员账号',
  `dept_name`      VARCHAR(50)   DEFAULT '' COMMENT '部门名称',
  `oper_url`       VARCHAR(255)  DEFAULT '' COMMENT '请求URL',
  `oper_ip`        VARCHAR(128)  DEFAULT '' COMMENT '主机地址',
  `oper_location`  VARCHAR(255)  DEFAULT '' COMMENT '操作地点（IP解析）',
  `oper_param`     VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
  `json_result`    VARCHAR(2000) DEFAULT '' COMMENT '返回参数',
  `status`         TINYINT(1)    DEFAULT 0 COMMENT '操作状态（0=正常 1=异常）',
  `error_msg`      VARCHAR(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time`      DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `cost_time`      BIGINT        DEFAULT 0 COMMENT '消耗时间（ms）',

  -- 基础审计与租户字段
  `tenant_code`    VARCHAR(12)   DEFAULT '000000' COMMENT '租户编码',
  `create_user`    BIGINT        DEFAULT NULL COMMENT '创建人ID',
  `create_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`    BIGINT        DEFAULT NULL COMMENT '更新人ID',
  `update_time`    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `if_deleted`     TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '删除标识（0=正常 1=已删除）',

  PRIMARY KEY (`id`),
  KEY `idx_oper_name` (`oper_name`),
  KEY `idx_oper_time` (`oper_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录表';
