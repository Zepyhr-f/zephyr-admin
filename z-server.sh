#!/bin/bash
set -e

# 获取脚本所在根目录
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVER_DIR="$ROOT_DIR/zephyr-server"
LOG_DIR="$SERVER_DIR/logs"

ACTION=$1

# 检查 Docker 运行状态 (仅 start/restart 时)
check_docker() {
  if ! docker info > /dev/null 2>&1; then
    echo "❌ 错误: Docker 引擎未运行，请先启动 Docker。"
    exit 1
  fi
}

case "$ACTION" in
  start|stop|restart)
    echo "====================================="
    echo "🐳 Zephyr 后端服务执行: $ACTION"
    echo "====================================="
    
    if [ "$ACTION" != "stop" ]; then
      check_docker
    fi
    
    # 调用并执行后端详细逻辑
    sh "$SERVER_DIR/scripts/z-server.sh" "$ACTION"
    
    echo "====================================="
    echo "✅ 后端 $ACTION 流程已下发！"
    if [ "$ACTION" == "start" ] || [ "$ACTION" == "restart" ]; then
      echo "▶ 实时查看日志请执行: tail -f $LOG_DIR/backend.log"
    fi
    echo "====================================="
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac
