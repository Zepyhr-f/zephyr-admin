#!/bin/bash
set -e

# 获取脚本所在根目录
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVER_DIR="$ROOT_DIR/zephyr-server"

ACTION=$1

case "$ACTION" in
  start|stop|restart)
    echo "====================================="
    echo "🐳 正在对后端执行操作: $ACTION"
    echo "====================================="
    
    # 调用后端目录内的详细脚本
    sh "$SERVER_DIR/server.sh" "$ACTION"
    
    echo "====================================="
    echo "✅ 后端操作 $ACTION 完成！"
    echo "====================================="
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac
