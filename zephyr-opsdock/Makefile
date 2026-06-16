.PHONY: up down restart logs ps status clean mkdir help

ifneq (,$(wildcard .env))
include .env
export
endif

DATA_DIR ?= $(CURDIR)/data
COMPOSE ?= docker compose

# 创建数据目录
mkdir:
	@echo "Creating data directories in $(DATA_DIR)..."
	@mkdir -p "$(DATA_DIR)/pgsql/data" "$(DATA_DIR)/redis/data" "$(DATA_DIR)/nats/data" "$(DATA_DIR)/consul/data" "$(DATA_DIR)/nginx/conf.d"
	@test -f "$(DATA_DIR)/nginx/conf.d/default.conf" || cp ./nginx/conf.d/default.conf "$(DATA_DIR)/nginx/conf.d/default.conf"
	@if [ -f "$(DATA_DIR)/nginx/conf.d/default.conf" ] && grep -q "proxy_pass http://nacos:8848;" "$(DATA_DIR)/nginx/conf.d/default.conf"; then \
		sed -i.bak 's|proxy_pass http://nacos:8848;|proxy_pass http://consul:8500;|g' "$(DATA_DIR)/nginx/conf.d/default.conf"; \
		rm -f "$(DATA_DIR)/nginx/conf.d/default.conf.bak"; \
	fi

up: mkdir
	$(COMPOSE) up -d

down:
	$(COMPOSE) down

restart:
	$(COMPOSE) restart

logs:
	$(COMPOSE) logs -f

ps:
	$(COMPOSE) ps

status: ps

# 查看所有服务健康状态
health:
	@echo "Checking service health..."
	@$(COMPOSE) ps --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"

# 启动单个服务
start-%:
	$(COMPOSE) up -d $*

# 停止单个服务
stop-%:
	$(COMPOSE) stop $*

# 查看单个服务日志
logs-%:
	$(COMPOSE) logs -f $*

# 完全清理（删除数据卷）
clean: down
	@echo "WARNING: This will delete all data volumes!"
	$(COMPOSE) down -v

# 默认帮助
help:
	@echo "OpsDock - Docker Services Management"
	@echo ""
	@echo "Commands:"
	@echo "  make up           - Create dirs and start all services"
	@echo "  make mkdir        - Create data directories"
	@echo "  make down         - Stop all services"
	@echo "  make restart      - Restart all services"
	@echo "  make logs         - Follow all logs"
	@echo "  make ps           - Show service status"
	@echo "  make health       - Show service health status"
	@echo "  make start-<svc>  - Start specific service (e.g., make start-postgres)"
	@echo "  make stop-<svc>   - Stop specific service"
	@echo "  make logs-<svc>   - Follow specific service logs"
	@echo "  make clean        - Stop and delete data volumes"
	@echo "  make help         - Show this help"
