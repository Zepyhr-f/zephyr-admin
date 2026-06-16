#!/bin/bash

# 获取脚本所在根目录
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVER_DIR="$ROOT_DIR/zephyr-go/app"
LOG_DIR="$ROOT_DIR/logs"

ACTION=$1

start_service() {
    local svc_name=$1
    local svc_dir=$2
    local svc_main=$3
    
    echo "Starting $svc_name..."
    cd "$svc_dir"
    
    # 编译 Go 二进制文件
    go build -o "$svc_name-bin" "$svc_main"
    
    nohup "./$svc_name-bin" > "$LOG_DIR/${svc_name}.log" 2>&1 &
    echo $! > "$LOG_DIR/${svc_name}.pid"
    echo "$svc_name started with PID $(cat "$LOG_DIR/${svc_name}.pid")"
}

stop_service() {
    local svc_name=$1
    local pid_file="$LOG_DIR/${svc_name}.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        echo "Stopping $svc_name (PID: $pid)..."
        kill -9 "$pid" 2>/dev/null || true
        rm -f "$pid_file"
        echo "$svc_name stopped."
    else
        echo "$svc_name is not running (no pid file)."
        # 尝试通过名字杀掉
        pkill -f "$svc_name-bin" || true
    fi
}

case "$ACTION" in
  start)
    echo "====================================="
    echo "🚀 启动 Zephyr 后端服务 (Go Native)"
    echo "====================================="
    mkdir -p "$LOG_DIR"
    
    export PATH=$PATH:$(go env GOPATH)/bin
    
    start_service "identity" "$SERVER_DIR/identity" "identity.go"
    sleep 2 # wait for identity to start
    start_service "auth" "$SERVER_DIR/auth" "auth.go"
    sleep 2 # wait for auth to start
    start_service "gateway" "$SERVER_DIR/gateway" "gateway.go"
    
    echo "====================================="
    echo "✅ 所有后端服务已启动！"
    echo "日志位于 $LOG_DIR 目录。"
    echo "你可以通过 tail -f logs/gateway.log 查看网关日志"
    echo "====================================="
    ;;
  stop)
    echo "====================================="
    echo "🛑 停止 Zephyr 后端服务"
    echo "====================================="
    
    stop_service "gateway" "" "gateway.go"
    stop_service "auth" "" "auth.go"
    stop_service "identity" "" "identity.go"
    
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
