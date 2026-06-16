# OpsDock

在 Linux 上快速启动一组常用基础服务（仅 Docker 部署）。

## 目录

- [服务列表](#服务列表)
- [启动](#启动)
- [常用命令](#常用命令)
- [配置说明](#配置说明)
- [项目结构](#项目结构)

---

## 服务列表

| 服务 | 端口 | 说明 |
|------|------|------|
| PostgreSQL (pgvector, PG16) | 5432 | 数据库，启用 `vector` 扩展 |
| Redis | 6379 | 缓存服务（启用密码） |
| NATS | 4222 / 8222 | 消息队列（启用 JetStream 持久化） |
| Consul | 8500 / 8600 | 服务发现与配置中心 |
| Nginx | 80 | 反向代理（转发到 Consul UI） |

---

## 启动

### 1. 准备环境变量

```bash
cp .env.example .env
```

### 2. 启动服务

```bash
make mkdir
make up
```

如果你的 Linux 环境只有 `docker-compose`（v1），可以这样启动：

```bash
make mkdir
make up COMPOSE=docker-compose
```

也可以直接运行初始化脚本（会自动选择 `docker compose` 或 `docker-compose`）：

```bash
./init.sh
```

### 3. 验证与访问

```bash
make ps
make health
```

- Consul UI：`http://localhost:${CONSUL_HTTP_PORT:-8500}/ui/`
- NATS：`nats://localhost:${NATS_CLIENT_PORT:-4222}`
- NATS 监控：`http://localhost:${NATS_MONITOR_PORT:-8222}`

---

## 常用命令

```bash
make up
make down
make restart
make logs
make ps
make health
make clean
```

---

## 配置说明

编辑 `.env`：

```bash
DATA_DIR=./data
NGINX_HTTP_PORT=80
REDIS_PASSWORD=change_me
NATS_CLIENT_PORT=4222
NATS_MONITOR_PORT=8222
CONSUL_HTTP_PORT=8500
CONSUL_DNS_PORT=8600
```

修改后执行：

```bash
make mkdir
make up
```

---

## 项目结构

```text
opsdock/
├── docker-compose.yml
├── .env.example
├── Makefile
├── init.sh
├── README.md
├── pgsql/
│   └── 01-extensions.sql
└── nginx/
    └── conf.d/
        └── default.conf
```
