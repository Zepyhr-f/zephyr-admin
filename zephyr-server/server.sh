#!/bin/bash
set -e

ACTION=$1

case "$ACTION" in
  start)
    export DOCKER_BUILDKIT=1
    export COMPOSE_DOCKER_CLI_BUILD=1
    cd "$(dirname "$0")/docker"
    echo "🐳 正在启动带有 BuildKit 缓存增量挂载的容器并行构建部署..."
    docker compose up -d --build
    
    # 统一日志追踪输出
    LOG_DIR="$(cd "$(dirname "$0")/.." && pwd)/logs"
    mkdir -p "$LOG_DIR"
    pkill -f "docker compose logs -f" 2>/dev/null || true
    nohup docker compose logs -f > "$LOG_DIR/backend.log" 2>&1 &
    
    echo "✅ 容器服务更新指令已下发！后端日志正持续写入 $LOG_DIR/backend.log"
    ;;
  stop)
    cd "$(dirname "$0")/docker"
    echo "🛑 正在安全停止并下线所有的后端容器服务..."
    docker compose down
    pkill -f "docker compose logs -f" 2>/dev/null || true
    echo "✅ 所有后端微服务及其网络环境已安全停止清理完毕！"
    ;;
  restart)
    $0 stop
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac
