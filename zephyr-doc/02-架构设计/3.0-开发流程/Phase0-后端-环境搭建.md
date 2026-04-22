# Phase 0 · 后端环境搭建

> **目标**：后端开发者在本机以完全一致的方式启动 Zephyr Admin 后端服务，从拉代码到接口健康检查通过不超过 15 分钟。

---

## 1. 必须安装的工具

| 工具 | 版本要求 | 安装方式 |
|---|---|---|
| **Java** | 21 LTS | `sdk install java 21.0.2-open`（推荐 sdkman） |
| **Maven** | 3.9+ | `brew install maven` |
| **Git** | 任意新版 | macOS 自带或 `brew install git` |
| **OrbStack** | 最新版 | [orbstack.dev](https://orbstack.dev)（替代 Docker Desktop） |
| **IntelliJ IDEA Ultimate** | 最新版 | 官方安装包 |

### 推荐 IDEA 插件

| 插件 | 用途 |
|---|---|
| Lombok | 识别 `@Data`、`@Builder` |
| MapStruct Support | Mapper 接口跳转与补全 |
| MyBatis-Plus | Mapper XML 双向跳转 |
| SonarLint | 实时代码质量检查 |

---

## 2. 版本锁定

**Java 版本**：在项目根目录提供 `.sdkmanrc`：
```
java=21.0.2-open
```
执行 `sdk env install` 自动切换。

**Maven 编译目标**（`pom.xml`）：
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.release>21</maven.compiler.release>
</properties>
```

**Docker 镜像**：`docker-compose.yml` 使用固定 tag，禁止 `latest`：
```yaml
services:
  mysql:  { image: mysql:8.0.38 }
  redis:  { image: redis:7.2.4 }
  nacos:  { image: nacos/nacos-server:v2.3.2 }
  minio:  { image: minio/minio:RELEASE.2024-01-01 }
```

---

## 3. 本地依赖服务启动

```bash
# 启动 MySQL / Redis / Nacos / MinIO
./z-server.sh infra:up

# 验证服务健康
./z-server.sh infra:status
```

服务端口：

| 服务 | 端口 |
|---|---|
| MySQL | 3306 |
| Redis | 6379 |
| Nacos | 8848 / 9848 |
| MinIO | 9000 / 9001 |

---

## 4. 项目初始化

```bash
git clone https://github.com/your-org/zephyr-admin.git
cd zephyr-admin/zephyr-server

# 切换 JDK
sdk env install

# 构建（跳过测试）
mvn clean install -DskipTests

# 数据库迁移（Flyway 自动建表）
mvn flyway:migrate -Dspring.profiles.active=dev

# 启动服务
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 5. 开启虚拟线程

```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true
```

> ⚠️ 同时须将业务代码中所有 `synchronized` 块改为 `ReentrantLock`，避免线程固定（Thread Pinning）。

---

## 6. 验收标准

- [ ] `mvn clean install -DskipTests` 无报错
- [ ] `GET http://localhost:9000/actuator/health` 返回 `{"status":"UP"}`
- [ ] Nacos 控制台 `http://localhost:8848/nacos` 可正常登录
- [ ] IDEA 中无红色报错，Lombok/MapStruct 注解正常识别
- [ ] `git commit` 触发 Lefthook pre-commit 检查
