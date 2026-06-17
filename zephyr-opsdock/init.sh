#!/usr/bin/env bash
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}=== OpsDock 初始化脚本 ===${NC}"

if [ ! -f ".env" ]; then
    echo -e "${YELLOW}未找到 .env 文件，正在复制模板...${NC}"
    cp .env.example .env
fi

# Export .env variables so child processes can read the same config.
set -a
source .env
set +a

if docker compose version >/dev/null 2>&1; then
    COMPOSE=(docker compose)
else
    COMPOSE=(docker-compose)
fi

DATA_DIR=${DATA_DIR:-$(pwd)/data}
echo -e "数据目录: ${DATA_DIR}"

echo -e "${YELLOW}创建数据目录...${NC}"
mkdir -p "${DATA_DIR}/pgsql/data" "${DATA_DIR}/redis/data" "${DATA_DIR}/nats/data" "${DATA_DIR}/consul/data" "${DATA_DIR}/nginx/conf.d"
cp ./nginx/conf.d/*.conf "${DATA_DIR}/nginx/conf.d/"
for conf in "${DATA_DIR}/nginx/conf.d/"*.conf; do
    if grep -q "proxy_pass http://nacos:8848;" "$conf"; then
        sed -i.bak 's|proxy_pass http://nacos:8848;|proxy_pass http://consul:8500;|g' "$conf"
        rm -f "${conf}.bak"
    fi
done

echo -e "${YELLOW}检查冲突容器...${NC}"
CONTAINER_NAMES=("pgsql-pgvector" "redis" "opsdock-nats" "consul" "nginx")
for name in "${CONTAINER_NAMES[@]}"; do
    if docker ps -a --format '{{.Names}}' | grep -q "^${name}$"; then
        echo -e "${YELLOW}停止并删除容器: $name${NC}"
        docker stop "$name" 2>/dev/null || true
        docker rm "$name" 2>/dev/null || true
    fi
done

echo -e "${GREEN}启动 Docker 服务...${NC}"
"${COMPOSE[@]}" up -d --remove-orphans

echo -e "${GREEN}服务状态:${NC}"
"${COMPOSE[@]}" ps

echo ""
echo -e "${GREEN}=== 初始化完成 ===${NC}"
echo -e "访问地址:"
echo -e "  NATS:       nats://localhost:${NATS_CLIENT_PORT:-4222}"
echo -e "  NATS 监控:  http://localhost:${NATS_MONITOR_PORT:-8222}"
echo -e "  Consul:     http://localhost:${CONSUL_HTTP_PORT:-8500}/ui/"
echo ""
echo -e "管理命令:"
echo -e "  make up     - 启动所有服务"
echo -e "  make down   - 停止所有服务"
echo -e "  make logs   - 查看日志"
