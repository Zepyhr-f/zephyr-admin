# Zephyr All-in-one 项目

开发已有数载，总觉得自己沉淀不够。开发的东西很少记录下来。
本项目将自己从零开始完整的搭建一套一体化框架，里面融合所有个人的经验，遇到的问题，抄到的代码。

## 项目结构

```
zephyr-parent (父工程)
│
├── zephyr-base (pom) - 基础模块，无启动类
│    ├── zephyr-core-tool (jar)
│    ├── zephyr-core-boot (jar)
│    ├── zephyr-common-config (jar)
│    └── zephyr-starter (pom)
│         ├── zephyr-starter-runner (jar)
│         ├── zephyr-starter-redis (jar)
│         ├── zephyr-starter-mp (jar)
│         ├── zephyr-starter-auth (jar)
│         ├── zephyr-starter-api (jar)
│         ├── zephyr-starter-jwt (jar)
│         └── zephyr-starter-vault (jar)
│
├── zephyr-common (pom) - 公共模块，包含启动类
│    ├── common-boot (jar) ← CommonApplication (端口 8072)
│    ├── auth (pom)
│    │    ├── zephyr-auth-api (jar)
│    │    └── zephyr-auth-biz (jar)
│    ├── system (pom)
│    │    ├── zephyr-system-api (jar)
│    │    └── zephyr-system-biz (jar)
│    ├── ops (jar)
│    └── support (pom)
│         ├── zephyr-generator (jar)
│         └── zephyr-test (jar)
│
├── zephyr-biz (pom) - 业务模块
│    ├── biz-boot (jar) ← BizApplication (端口 9301)
│    └── rule (jar)
│
└── zephyr-gateway (jar)
```

## 启动类

| 启动类 | 模块 | 端口 | 说明 |
|--------|------|------|------|
| CommonApplication | zephyr-common/common-boot | 8072 | 公共模块启动类 |
| BizApplication | zephyr-biz/biz-boot | 9301 | 业务模块启动类 |
| GatewayApplication | zephyr-gateway | 9300 | 网关（保持不变） |

## 服务模块结构

```
zephyr-service-xxx
│
├── zephyr-xxx-api
│    ├── feign
│    └── pojo
│         ├── dto
│         ├── entity
│         └── vo
└── zephyr-xxx-biz
     ├── controller
     ├── feign
     ├── service
     │    └── impl
     ├── mapper
     │    └── xml
```

## 技术栈

- Java 17 + Spring Boot 3.3.4
- Spring Cloud 2023.0.3
- Spring Cloud Alibaba 2022.0.0.0
- MyBatis-Plus 3.5.6
- Nacos (服务注册与配置中心)
- JWT (ES256/HS256)
- Drools/KIE (规则引擎)
