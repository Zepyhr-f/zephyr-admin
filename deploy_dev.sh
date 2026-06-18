#!/bin/bash
# Zephyr Admin 本地运行/部署脚本 (基于生产环境镜像容器化部署在本地)
# 用法: ./deploy_prod.sh [service]
# 示例: ./deploy_prod.sh web | go | opsdock | all

set -e

# 本地中间件的数据和 nginx 静态文件根目录
LOCAL_OPSDOCK_DIR="$(pwd)/zephyr-opsdock"
WEB_LOCAL_DIR="${LOCAL_OPSDOCK_DIR}/data/nginx/www/admin"

function print_info() {
    echo -e "\033[1;34m===> $1\033[0m"
}

function deploy_web() {
    print_info "开始本地部署前端 (zephyr-web)"
    if [ ! -d "zephyr-web" ]; then
        echo "错误: 未找到 zephyr-web 目录，请在项目根目录执行该脚本"
        exit 1
    fi
    cd zephyr-web
    print_info "正在打包前端产物..."
    npx vite build
    
    print_info "正在复制前端产物到本地 Nginx 目录: $WEB_LOCAL_DIR"
    mkdir -p "$WEB_LOCAL_DIR"
    # 清空旧的前端文件并复制新的
    rm -rf "${WEB_LOCAL_DIR:?}/"*
    cp -r dist/* "$WEB_LOCAL_DIR/"
    
    print_info "正在重启本地 Nginx 容器 (若存在)..."
    docker restart nginx 2>/dev/null || true
    cd ..
    print_info "前端本地部署完成！你可以通过 http://admin.zephyr.green (如果配了本地 hosts) 或是 http://localhost 访问。"
}

function deploy_go() {
    print_info "开始本地构建并启动后端 (zephyr-go)"
    if [ ! -d "zephyr-go" ]; then
        echo "错误: 未找到 zephyr-go 目录，请在项目根目录执行该脚本"
        exit 1
    fi
    cd zephyr-go

    export DOCKER_BUILDKIT=1
    # 本地运行的话可以不强制 amd64，直接利用 Mac M 芯片原生架构，运行更快
    # export DOCKER_DEFAULT_PLATFORM=linux/amd64

    print_info "正在本地执行 docker compose build..."
    docker compose build

    print_info "在本地启动后端容器..."
    docker compose up -d
    
    cd ..
    print_info "后端本地部署完成！"
}

function deploy_opsdock() {
    print_info "开始部署本地中间件 (zephyr-opsdock)"
    if [ ! -d "zephyr-opsdock" ]; then
        echo "错误: 未找到 zephyr-opsdock 目录，请在项目根目录执行该脚本"
        exit 1
    fi
    cd zephyr-opsdock
    
    # 本地直接运行 init.sh
    print_info "执行初始化脚本..."
    chmod +x init.sh
    ./init.sh
    
    cd ..
    print_info "本地中间件部署完成！"
}

function show_help() {
    echo "使用说明:"
    echo "  ./deploy_prod.sh [service]"
    echo ""
    echo "可部署的服务 (service):"
    echo "  web      : 打包前端并直接覆盖到本地 zephyr-opsdock/nginx/www/admin 目录，重启本地 Nginx"
    echo "  go       : 本地构建 zephyr-go 镜像并利用 docker compose 启动微服务"
    echo "  opsdock  : 本地运行 init.sh 初始化所有基础中间件容器"
    echo "  all      : 依次在本地部署 opsdock, go, 和 web"
}

case "$1" in
    web)
        deploy_web
        ;;
    go)
        deploy_go
        ;;
    opsdock)
        deploy_opsdock
        ;;
    all)
        deploy_opsdock
        deploy_go
        deploy_web
        ;;
    *)
        show_help
        exit 1
        ;;
esac
