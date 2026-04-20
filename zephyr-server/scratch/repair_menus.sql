-- 修复 sys_menu 表中的乱码数据
UPDATE zephyr_db.sys_menu SET menu_name = '代码生成' WHERE id = 4100;
UPDATE zephyr_db.sys_menu SET menu_name = '服务监控' WHERE id = 3100;
UPDATE zephyr_db.sys_menu SET menu_name = '登录日志' WHERE id = 2100;
UPDATE zephyr_db.sys_menu SET menu_name = '安全审计' WHERE id = 2000;
UPDATE zephyr_db.sys_menu SET menu_name = '操作日志' WHERE id = 2101;
UPDATE zephyr_db.sys_menu SET menu_name = '定时任务' WHERE id = 3101;
UPDATE zephyr_db.sys_menu SET menu_name = '部门管理' WHERE id = 100;
UPDATE zephyr_db.sys_menu SET menu_name = '在线用户' WHERE id = 2102;
UPDATE zephyr_db.sys_menu SET menu_name = '系统监控' WHERE id = 3000;
UPDATE zephyr_db.sys_menu SET menu_name = '字典管理' WHERE id = 101;
UPDATE zephyr_db.sys_menu SET menu_name = '开发工具' WHERE id = 4000;
UPDATE zephyr_db.sys_menu SET menu_name = '参数配置' WHERE id = 102;
UPDATE zephyr_db.sys_menu SET menu_name = '通知公告' WHERE id = 103;
