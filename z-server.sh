#!/bin/bash

# 获取脚本所在根目录
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVER_DIR="$ROOT_DIR/zephyr-go/app"
LOG_DIR="$ROOT_DIR/logs"

ACTION=$1


case "$ACTION" in
  start)
    echo "====================================="
    echo "🚀 启动 Zephyr 后端服务 (Docker Compose)"
    echo "====================================="
    
    cd "$ROOT_DIR/zephyr-go"
    docker-compose up -d --build
    
    echo "====================================="
    echo "✅ 所有后端服务已通过 Docker 启动！"
    echo "====================================="
    ;;
  stop)
    echo "====================================="
    echo "🛑 停止 Zephyr 后端服务 (Docker Compose)"
    echo "====================================="
    
    cd "$ROOT_DIR/zephyr-go"
    docker-compose down
    
    echo "====================================="
    echo "✅ 所有后端服务已停止！"
    echo "====================================="
    ;;
  restart)
    $0 stop
    sleep 2
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac
