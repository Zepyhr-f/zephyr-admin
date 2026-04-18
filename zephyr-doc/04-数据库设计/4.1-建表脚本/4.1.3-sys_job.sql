-- ============================================================================
-- 任务调度模块建表脚本
-- ----------------------------
-- 1. 定时任务调度表
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id`              BIGINT        NOT NULL COMMENT '任务ID',
  `job_name`        VARCHAR(64)   DEFAULT '' COMMENT '任务名称',
  `job_group`       VARCHAR(64)   DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target`   VARCHAR(500)  NOT NULL COMMENT '调用目标字符串',
  `cron_expression` VARCHAR(255)  DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy`  TINYINT(1)    DEFAULT 3 COMMENT '计划执行策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent`      TINYINT(1)    DEFAULT 1 COMMENT '是否并发执行（0禁止 1允许）',
  `status`          TINYINT(1)    DEFAULT 0 COMMENT '状态（0正常 1暂停）',
  `remark`          VARCHAR(500)  DEFAULT '' COMMENT '备注信息',
  
  -- 基础审计字段
  `create_user`     BIGINT        DEFAULT NULL COMMENT '创建者',
  `create_time`     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user`     BIGINT        DEFAULT NULL COMMENT '更新者',
  `update_time`     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`        TINYINT(1)    DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度表';

-- ----------------------------
-- 2. 定时任务调度日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `id`              BIGINT        NOT NULL COMMENT '记录ID',
  `job_name`        VARCHAR(64)   NOT NULL COMMENT '任务名称',
  `job_group`       VARCHAR(64)   NOT NULL COMMENT '任务组名',
  `invoke_target`   VARCHAR(500)  NOT NULL COMMENT '调用目标字符串',
  `job_message`     VARCHAR(500)  COMMENT '日志信息',
  `status`          TINYINT(1)    DEFAULT 0 COMMENT '执行状态（0正常 1失败）',
  `exception_info`  VARCHAR(2000) DEFAULT '' COMMENT '异常信息',
  `create_time`     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `cost_time`       BIGINT        DEFAULT 0 COMMENT '消耗时间(ms)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务调度日志表';
