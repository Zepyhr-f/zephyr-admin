# Zephyr 项目报告

## 1. 项目概述

**项目名称**: Zephyr
**项目类型**: Java Spring Boot 微服务架构
**Java 版本**: Java 17
**Spring Boot 版本**: 3.3.4
**构建工具**: Maven

## 2. 架构概览

```
zephyr-parent (根 Maven 项目)
├── zephyr-core          # 核心工具模块
├── zephyr-common        # 公共配置模块
├── zephyr-gateway       # API 网关 (Spring Cloud Gateway)
├── zephyr-starter       # 可复用启动器模块
├── zephyr-service       # 业务微服务
├── zephyr-support       # 支持工具
└── zephyr-ops           # 运维监控模块
```

## 3. 模块详情

### 3.1 核心模块 (zephyr-core)

| 模块 | 路径 | 说明 |
|------|------|------|
| zephyr-core-boot | zephyr-core/zephyr-core-boot | 启动工具 |
| zephyr-core-tool | zephyr-core/zephyr-core-tool | 核心工具 (Spring, Apache Commons, Knife4j) |

### 3.2 公共模块 (zephyr-common)

| 模块 | 说明 |
|------|------|
| zephyr-common-config | Nacos 配置集成 |

### 3.3 网关模块 (zephyr-gateway)

- 使用 Spring Cloud Gateway
- JWT 认证
- Redis 缓存

### 3.4 启动器模块 (zephyr-starter)

| 启动器 | 说明 |
|--------|------|
| zephyr-starter-api | OpenFeign 服务客户端 |
| zephyr-starter-auth | Spring Security 集成 |
| zephyr-starter-jwt | JWT 令牌处理 (ES256/HS256) |
| zephyr-starter-mp | MyBatis-Plus ORM + Knife4j API 文档 |
| zephyr-starter-redis | Redis 缓存 (Jackson 序列化) |
| zephyr-starter-runner | 应用运行工具 |
| zephyr-starter-vault | 密钥管理 |

### 3.5 业务服务模块 (zephyr-service)

| 服务 | 端口 | 说明 |
|------|------|------|
| zephyr-service-auth | 8072 | 认证服务 |
| zephyr-service-system | 8073 | 系统/用户管理服务 |
| zephyr-service-rule | 9301 | 规则引擎 (Drools/Kie) |

### 3.6 支持模块 (zephyr-support)

| 模块 | 端口 | 说明 |
|------|------|------|
| zepphyr-generator | 9001 | 代码生成器 (MyBatis-Plus Generator + FreeMarker) |

### 3.7 运维模块 (zephyr-ops)

| 模块 | 端口 | 说明 |
|------|------|------|
| zephyr-ops | 9811 | 运维监控服务 |

## 4. 技术栈

### 核心框架
- **Spring Boot**: 3.3.4
- **Spring Cloud**: 2023.0.3
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 认证授权

### 数据库与缓存
- **MySQL**: 8.0.33
- **Redis**: 8.0
- **MyBatis-Plus**: 3.5.6

### 微服务组件
- **Nacos**: 服务发现与配置中心
- **Spring Cloud Gateway**: API 网关
- **OpenFeign**: 服务间调用

### 其他重要组件
- **Knife4j**: API 文档 (OpenAPI 3)
- **JWT**: jjwt 0.11.5
- **Drools/Kie**: 规则引擎 7.74.1.Final
- **Docker Compose**: 容器编排

## 5. 基础设施

### Docker Compose 配置

| 服务 | 端口 |
|------|------|
| MySQL | 3306 |
| Redis | 6379 |
| Nacos | 8848/9848 |
| Nginx | 80/443 |

### 服务端口汇总

| 服务 | 端口 |
|------|------|
| Auth Service | 8072 |
| System Service | 8073 |
| Rule Service | 9301 |
| Generator | 9001 |
| Ops | 9811 |
| Nacos | 8848 |

## 6. 关键配置文件

- `/pom.xml` - 根 Maven 父 POM
- `/zephyr-service/zephyr-service-auth/zephyr-service-auth-biz/src/main/resources/application.yml`
- `/zephyr-service/zephyr-service-system/zephyr-service-system-biz/src/main/resources/application.yml`
- `/zephyr-service/zephyr-service-rule/src/main/resources/application.yml`
- `/zephyr-ops/src/main/resources/application.yml`
- `/zephyr-support/zepphyr-generator/src/main/resources/application.yml`
- `/z-script/docker/docker-compose.yml`

## 7. 项目特点

1. **微服务架构**: 采用 Spring Cloud Alibaba 微服务架构
2. **模块化设计**: 多模块 Maven 项目，职责分离清晰
3. **认证授权**: JWT + Spring Security 双认证机制
4. **规则引擎**: 集成 Drools 规则引擎
5. **代码生成**: 内置基于 MyBatis-Plus 的代码生成器
6. **容器化部署**: 提供完整的 Docker Compose 配置
7. **API 文档**: 使用 Knife4j/Swagger 提供交互式 API 文档

## 8. 许可证

项目包含 LICENSE 文件。
