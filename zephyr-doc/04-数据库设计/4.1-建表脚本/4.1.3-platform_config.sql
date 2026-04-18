-- ----------------------------
-- 1. 字典表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `id` bigint(20) NOT NULL COMMENT '主键',
                             `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父主键',
                             `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
                             `dict_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
                             `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
                             `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典备注',
                             `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 (1-启用, 0-禁用)',
                             `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `is_deleted` int(2) NULL DEFAULT 0 COMMENT '是否已删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表';

-- ----------------------------
-- 2. 岗位表
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
                             `id` bigint(20) NOT NULL COMMENT '主键',
                             `category` int(11) NULL DEFAULT NULL COMMENT '岗位类型',
                             `post_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位编号',
                             `post_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位名称',
                             `sort` int(2) NULL DEFAULT NULL COMMENT '岗位排序',
                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位描述',
                             `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
                             `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `status` int(2) NULL DEFAULT NULL COMMENT '状态',
                             `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 (1-启用, 0-禁用)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位表';
