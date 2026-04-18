-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `zephyr_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `zephyr_db`;

-- 创建用户并授权（允许从任何 IP 连接）
CREATE USER IF NOT EXISTS 'zephyr'@'%' IDENTIFIED BY 'Zr#db#0830';
GRANT ALL PRIVILEGES ON `zephyr_db`.* TO 'zephyr'@'%';

-- 创建用户并授权（允许从 localhost 连接）
CREATE USER IF NOT EXISTS 'zephyr'@'localhost' IDENTIFIED BY 'Zr#db#0830';
GRANT ALL PRIVILEGES ON `zephyr_db`.* TO 'zephyr'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;
