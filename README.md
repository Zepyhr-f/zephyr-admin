# Zephyr Admin - 部署指南

Zephyr Admin 是一个面向企业级的后台管理系统，具备按钮级权限控制、审计追踪和监控面板等核心能力。

> **为什么使用 Go？** 本项目后端采用 Go 语言开发，主要原因是 Go 编译出的可执行文件体积小，且运行时内存占用极低。这非常方便个人开发者在资源有限的服务器（如轻量级云主机）上进行低成本的部署。中间件也使用内存占用更小的方案。

本项目分为三个主要部分：
1. **zephyr-opsdock**：基础中间件运行环境（Nginx, PostgreSQL, Redis, Consul 等）。
2. **zephyr-go**：基于 Go-Zero 的微服务后端（Gateway, Auth, Identity, System）。
3. **zephyr-web**：基于 React + Vite 的前端应用。

---

## 1. 部署前置要求
- **操作系统**：Linux (Ubuntu/CentOS) 或 macOS
- **基础依赖**：
  - [Docker](https://docs.docker.com/engine/install/) & [Docker Compose](https://docs.docker.com/compose/install/)
  - Node.js (推荐 v20+) & pnpm (用于编译前端)
- **网络说明**：所有 Docker 容器需处于同一个网桥网络中，默认使用 `zephyr-opsdock` 启动时创建的 `opsdock_default` 网络。

---

## 2. 部署步骤

### 步骤一：启动基础中间件 (opsdock)
中间件包含了数据库、缓存、服务发现及网关代理。
```bash
cd zephyr-opsdock

# 根据需要修改环境配置（如设置挂载目录 DATA_DIR）
cp .env.example .env 

# 启动中间件
docker compose up -d
```
> **注意**：首次启动后，请连接到 PostgreSQL 数据库（端口 5432），并执行位于 `zephyr-doc/04-数据库设计/` 目录下的 SQL 脚本：
> 1. 执行 `4.1-建表脚本` 初始化表结构
> 2. 执行 `4.2-初始化脚本` 导入基础数据及初始管理员账号。

### 步骤二：启动后端微服务 (zephyr-go)
后端采用 Go-Zero 微服务架构，各服务之间通过 Consul 进行服务发现。

```bash
cd zephyr-go

# 编译并启动所有微服务（Gateway、Auth、Identity、System）
docker compose up -d --build
```
> **工作原理**：
> 所有的 Go 服务通过 `Dockerfile` 采用多阶段构建，编译出极小的 Alpine 镜像。它们会自动连接到 `opsdock_default` 网络中，并向 Consul 注册自己的 RPC 服务。Gateway 则负责对外暴露 `8888` 端口。

### 步骤三：编译并部署前端 (zephyr-web)
前端是一个标准的 Vite SPA 单页应用。在生产环境下，所有 API 请求通过同源的 Nginx 代理转发，解决跨域问题。

1. **修改配置**
   确保 `zephyr-web/.env.production` 代理路径正确：
   ```env
   VITE_API_BASE_URL=/api/
   ```

2. **编译产物**
   ```bash
   cd zephyr-web
   pnpm install
   pnpm run build
   ```

3. **部署静态文件**
   将编译好的 `dist` 目录下的所有文件，放置到服务器上 Nginx 的 HTML 挂载目录中。例如，如果 `zephyr-opsdock` 中配置的 `DATA_DIR` 为 `/home/ubuntu/opsdock-data`：
   ```bash
   rsync -az --delete dist/ root@your_server_ip:/home/ubuntu/opsdock-data/nginx/html/
   ```

---

## 3. Nginx 配置说明
Nginx 承担了**前端静态资源托管**和**后端接口反向代理**的双重职责。
配置文件通常位于 `${DATA_DIR}/nginx/conf.d/default.conf`。

关键的反向代理配置示例：
```nginx
server {
  listen 80;
  server_name _;
  
  root /usr/share/nginx/html;
  index index.html;

  # 代理 Auth 服务
  location ^~ /api/zephyr-auth/ {
    resolver 127.0.0.11 valid=10s;
    set $target http://zephyr-gateway:8888;
    rewrite ^/api/zephyr-auth/(.*)$ /api/v1/auth/$1 break;
    proxy_pass $target;
    # ... 其他 proxy headers
  }

  # 代理 System 服务
  location ^~ /api/zephyr-system/ {
    resolver 127.0.0.11 valid=10s;
    set $target http://zephyr-gateway:8888;
    rewrite ^/api/zephyr-system/(.*)$ /api/v1/system/$1 break;
    proxy_pass $target;
    # ... 其他 proxy headers
  }

  # 前端路由回退
  location / {
    try_files $uri $uri/ /index.html;
  }
}
```
> **为什么要用 `resolver` 和 `set` 变量？**
> 这是为了避免 Nginx 在启动时因 `zephyr-gateway` 尚未就绪而导致的 DNS 解析失败（Nginx 会 Crash）。通过变量代理，Nginx 会在运行时动态解析域名。

---

## 4. 常见问题排查

1. **前端报错“网络连接异常”**
   - **检查是否处于 HTTP 环境**：如果使用公网 IP 直接访问（无 HTTPS），现代浏览器会禁用 `crypto.randomUUID()`，导致前端发起请求前直接报错。我们在前端网络请求拦截器中已加入降级方案，请确保部署的是最新代码。
   - **Nginx 405 Not Allowed**：通常是由于代理路由未匹配上，导致 POST 请求被打到了静态 `index.html`。请检查 Nginx 中的 `location` 路径前缀（如是否有尾部斜杠等）。

2. **登录提示“密码错误”**
   - 确认数据库的 BCrypt 密码是否正确。系统默认的 `admin` 密码为 `admin123`，如果连不上，可以使用 SQL 手动更新密码的 Hash：
     ```sql
     UPDATE zephyr_sys_user SET password='$2a$10$VzIrbHNAytRznVEmRiRF5esCWGpaJYPerKxt.vsfGP77J5IH56tYu' WHERE nick_name='admin';
     ```

3. **微服务无法互相调用**
   - 检查 `zephyr-opsdock` 里的 `consul` 容器是否正常运行，可以通过 `http://your_server_ip:8500` 查看 Consul UI 注册中心（确保安全组或防火墙已放通，或者使用 SSH 隧道访问）。
   - 确保 `zephyr-go` 服务和 `consul` 在同一个 Docker Network 中（默认均加入外部网络 `opsdock_default`）。
