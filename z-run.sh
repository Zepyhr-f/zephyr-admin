#!/bin/bash
set -e

ACTION=$1
FRONTEND_PID_FILE="/Users/zephyr/z-code/z-manager/slash-admin-main/.frontend.pid"
BACKEND_SCRIPT="/Users/zephyr/z-code/z-manager/zephyr-server/z-script/server.sh"

case "$ACTION" in
  start)
    echo "====================================="
    echo "🚀 开始统一启动 Zephyr 前后端服务"
    echo "====================================="

    # 1. 启动后端
    echo "▶ 启动后端服务..."
    sh "$BACKEND_SCRIPT" start
    
    # 2. 启动前端
    echo "▶ 启动前端服务 (React Slash Admin)..."
    cd /Users/zephyr/z-code/z-manager/slash-admin-main
    # 检查是否已有运行中的前端
    if [ -f "$FRONTEND_PID_FILE" ]; then
        echo "⚠️ 发现前端 PID 文件，正在清理旧进程..."
        PID=$(cat "$FRONTEND_PID_FILE")
        kill -9 "$PID" 2>/dev/null || true
        pkill -f "vite" 2>/dev/null || true
        rm -f "$FRONTEND_PID_FILE"
    fi
    
    LOG_DIR="/Users/zephyr/z-code/z-manager/logs"
    mkdir -p "$LOG_DIR"
    
    # 后台启动前端开发服 (React Slash Admin)
    export PATH="/opt/homebrew/opt/node@22/bin:/opt/homebrew/opt/pnpm/bin:$PATH"
    nohup pnpm dev > "$LOG_DIR/frontend.log" 2>&1 &
    PID=$!
    echo $PID > "$FRONTEND_PID_FILE"
    echo "✅ 前端服务已在后台启动 (PID: $PID)！日志输出至 $LOG_DIR/frontend.log"
    echo "====================================="
    echo "🎉 项目全部启动指令下发完成！"
    echo "====================================="
    ;;
  stop)
    echo "====================================="
    echo "🛑 开始统一停止 Zephyr 前后端服务"
    echo "====================================="
 
    # 1. 停止后端
    echo "▶ 停止后端服务..."
    sh "$BACKEND_SCRIPT" stop
    
    # 2. 停止前端
    echo "▶ 停止前端服务..."
    if [ -f "$FRONTEND_PID_FILE" ]; then
        PID=$(cat "$FRONTEND_PID_FILE")
        kill -9 "$PID" 2>/dev/null || true
        rm -f "$FRONTEND_PID_FILE"
    fi
    # 强制清理确保没有僵尸进程
    pkill -f "vite" 2>/dev/null || true
    echo "✅ 前端服务已停止并清理完毕！"
    echo "====================================="
    echo "✅ 项目全部停止指令执行完成！"
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
