FROM golang:alpine AS builder

LABEL stage=gobuilder

ENV CGO_ENABLED 0
ENV GOPROXY https://goproxy.cn,direct

RUN apk update --no-cache && apk add --no-cache tzdata

WORKDIR /build

# 复制 go.mod 和 go.sum 提前下载依赖
COPY go.mod go.sum ./
RUN go mod edit -go=1.24.0 && go mod download

# 复制项目其他源码
COPY . .

# 编译 gateway 服务
WORKDIR /build/app/gateway
RUN go build -ldflags="-s -w" -o /app/gateway gateway.go

FROM alpine

RUN apk update --no-cache && apk add --no-cache ca-certificates tzdata
ENV TZ Asia/Shanghai

WORKDIR /app
COPY --from=builder /app/gateway /app/gateway
# 配置文件需要在运行时通过 volume 挂载，或者构建时复制对应的 docker 专属配置
COPY --from=builder /build/app/gateway/etc /app/etc

CMD ["./gateway", "-f", "etc/gateway-api-docker.yaml"]
