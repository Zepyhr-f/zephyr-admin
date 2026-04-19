#!/bin/bash
set -e

# 获取脚本所在根目录
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
WEB_DIR="$ROOT_DIR/zephyr-web"
LOG_DIR="$ROOT_DIR/logs"
PID_FILE="$WEB_DIR/.frontend.pid"

mkdir -p "$LOG_DIR"

ACTION=$1

case "$ACTION" in
  start)
    echo "====================================="
    echo "🚀 正在启动前端服务 (React)..."
    echo "====================================="

    cd "$WEB_DIR"
    
    # 检查是否已有运行中的前端
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p $PID > /dev/null; then
            echo "⚠️  前端服务已在运行 (PID: $PID)，请先停止或重启。"
            exit 0
        else
            echo "🧹 清理过期的 PID 文件..."
            rm -f "$PID_FILE"
        fi
    fi

    # 包含必要的路径，确保命令可用
    export PATH="/opt/homebrew/opt/node@22/bin:/opt/homebrew/opt/pnpm/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$PATH"
    
    echo "▶ 日志输出至 $LOG_DIR/frontend.log"
    nohup pnpm dev > "$LOG_DIR/frontend.log" 2>&1 &
    PID=$!
    echo $PID > "$PID_FILE"
    
    echo "✅ 前端服务后台启动成功！(PID: $PID)"
    echo "====================================="
    ;;
  stop)
    echo "🛑 正在停止前端服务..."
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        kill -9 "$PID" 2>/dev/null || true
        rm -f "$PID_FILE"
    fi
    pkill -f "vite" 2>/dev/null || true
    echo "✅ 前端服务已停止。"
    ;;
  restart)
    $0 stop
    sleep 1
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac
