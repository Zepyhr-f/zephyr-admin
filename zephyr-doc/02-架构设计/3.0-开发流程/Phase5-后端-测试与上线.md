# Phase 5 · 后端测试与上线

> **目标**：确保核心业务逻辑有测试覆盖，建立完整的 CI/CD 流水线，实现代码合并后自动测试、构建、发布。

---

## 1. 任务清单

| 编号 | 任务 | 优先级 |
|---|---|---|
| 5-BE-01 | 核心 Service 单元测试（JUnit 5 + Mockito）覆盖 Auth / System 模块 | 🔴 P0 |
| 5-BE-02 | API 集成测试（`@SpringBootTest` + TestRestTemplate）覆盖登录/刷新/权限接口 | 🔴 P0 |
| 5-BE-03 | 编写 `Dockerfile`：多阶段构建，最小化镜像体积 | 🔴 P0 |
| 5-BE-04 | 配置 GitHub Actions 流水线：test → build → docker push | 🔴 P0 |
| 5-BE-05 | 配置生产 `application-prod.yml`：关闭 SQL 日志、开启慢查询监控、调整日志级别 | 🟡 P1 |
| 5-BE-06 | 配置 Prometheus 指标：关键接口响应时间、错误率、连接池使用率 | 🟡 P1 |
| 5-BE-07 | 配置 SkyWalking Agent：`-javaagent` 接入链路追踪 | 🟡 P1 |
| 5-BE-08 | 压力测试：JMeter/Gatling 模拟并发，验证虚拟线程配置下的吞吐量 | 🟢 P2 |

---

## 2. 单元测试规范

**目标覆盖率**：核心 Service 层行覆盖率 ≥ 80%

```java
// 示例：UserServiceTest.java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserMapper userMapper;
    @Mock private RedisUtil redisUtil;
    @InjectMocks private UserServiceImpl userService;

    @Test
    @DisplayName("新增用户 - 用户名重复时应抛出 BusinessException")
    void createUser_ShouldThrowException_WhenUsernameExists() {
        // given
        when(userMapper.selectByUsername("admin")).thenReturn(new SysUser());
        // when & then
        assertThrows(BusinessException.class, () -> userService.createUser(request));
    }
}
```

---

## 3. Dockerfile 多阶段构建

```dockerfile
# 构建阶段
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# 运行阶段（最小镜像）
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# 开启虚拟线程（也可在 yml 中配置）
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 4. GitHub Actions 流水线

```yaml
# .github/workflows/backend-ci.yml
name: Backend CI/CD

on:
  push:
    branches: [main, dev]
    paths: ['zephyr-server/**']

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: 'temurin' }
      - run: mvn test -B
        working-directory: zephyr-server

  build-and-push:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Docker Image
        run: docker build -t zephyr-server:${{ github.sha }} .
      - name: Push to Registry
        run: docker push ...
```

---

## 5. 生产环境配置优化

```yaml
# application-prod.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 100    # 虚拟线程下适当调大
      connection-timeout: 5000

logging:
  level:
    root: WARN
    com.zephyr: INFO
    org.springframework.security: WARN

management:
  endpoints:
    web:
      exposure:
        include: health,prometheus,info
```

---

## 6. 阶段验收标准

- [ ] `mvn test` 全部通过，核心 Service 覆盖率 ≥ 80%
- [ ] Dockerfile 构建成功，镜像可正常启动
- [ ] GitHub Actions 流水线在 `push` 后自动触发并通过
- [ ] `GET /actuator/prometheus` 返回 Micrometer 指标
- [ ] 生产环境日志无多余 DEBUG 信息
