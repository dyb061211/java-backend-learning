# Week22 — Java Core & Maven

## 项目简介

本项目是 Java 后端工程学习 Week22 的实践项目，使用 Java 和 Maven 实现一个基于内存的商品目录管理程序。

## 技术环境

- IDE：IntelliJ IDEA
- JDK：Oracle JDK 27
- Maven：3.10.0
- 编译目标：Java 21
- 测试：JUnit Jupiter
- 构建工具：Apache Maven

## 项目结构

- `App.java`：程序入口和商品管理演示
- `Product.java`：商品数据、封装和数据校验
- `ProductCatalog.java`：新增、查询、修改和列出商品
- `FormatterDemo.java`：接口、多态、继承与抽象类实验
- `ExceptionDemo.java`：异常传播与资源关闭实验
- `ProductCatalogTest.java`：JUnit 自动化测试

## 业务功能

- 新增商品
- 根据 SKU 查询商品
- 修改商品名称与库存
- 列出商品目录
- 拒绝重复 SKU
- 拒绝非法商品数据
- 对不存在的商品给出明确错误

业务约束：

- SKU 和名称不能为空
- SKU 区分大小写，创建后不修改
- 商品名称长度为 1～20（使用 `String.length()` 判断）
- 库存不能小于 0
- 非法修改失败后保留原有合法数据

## 构建与运行

进入包含 `week22-java-core/pom.xml` 的项目目录。

编译：

```bash
mvn compile
```

测试：

```bash
mvn test
```

清理并打包：

```bash
mvn clean package
```

运行：

```bash
java -cp target/week22-java-core-1.0-SNAPSHOT.jar learning.catalog.App
```

## 测试结果

Week22 最终验证：

- Tests run：5
- Failures：0
- Errors：0
- Skipped：0
- Maven BUILD SUCCESS
- JAR 终端运行成功

覆盖新增商品、重复 SKU 拒绝、非法库存修改，以及商品名称长度规则。

## 项目边界

- 商品数据仅保存在当前 Java 进程内存中
- 程序重启后不会自动恢复旧数据
- 未接入数据库、Web API 或并发处理
- `FormatterDemo` 和 `ExceptionDemo` 是独立学习实验，不属于核心商品管理业务
- 本项目是 Java 基础工程练习，不是生产级应用


## Week23：Java 集合、泛型与数据处理

在 Week22 商品管理项目基础上，完成以下学习与实践：

- List、Set、Map，以及集合视图与浅拷贝
- equals/hashCode、HashSet 与 SKU 唯一性
- 使用 LinkedHashMap 按 SKU 管理商品，保留插入顺序
- 泛型方法、类型约束、通配符和类型擦除
- Comparator、Lambda、Stream 筛选、排序与库存统计
- Optional 与简单分组统计
- Path、Files、UTF-8、TSV 文件读写
- 严格导入：非法数据立即失败，不返回半成品 Catalog
- 按库存及 SKU 排序导出，并重新读取验证

### 数据格式

输入文件：samples/products.tsv

采用 UTF-8 编码的 TSV 格式：

sku、name、stock 三列，使用 Tab 分隔。

当前不支持复杂 CSV 转义、字段内 Tab 或换行。

### 业务规则

- SKU 必填且不能重复
- 商品名称必填，长度不超过 20
- 库存不能为负数
- 重复 SKU 直接拒绝，不覆盖旧商品
- 导入失败时不返回部分结果
- 输出文件已存在时不自动覆盖

### 运行环境

- IntelliJ IDEA
- Oracle JDK 27
- Maven 3.10.0
- 编译目标 Java 21
- JUnit Jupiter

### 测试

最近一次已验证：
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0

运行测试：

```bash
mvn test
```

从 java-backend-learning 仓库根目录运行文件处理演示：

```bash
java -cp week22-java-core/target/week22-java-core-1.0-SNAPSHOT.jar learning.catalog.demo.FileReadDemo
```

注意：上面的命令需要先完成 Maven 打包，且导出文件名必须尚未存在。

### 已知边界

- listProducts() 返回独立列表结构，但 Product 对象仍然共享
- 当前采用单线程数据处理，不提供线程安全保证
- 文件导出不提供原子写入或崩溃恢复保证
- 未接入数据库、HTTP 或消息队列


## Week24：Java 并发基础与任务管理

本周基于已有 Maven 工程，学习 Java 多线程、线程池、同步机制、异步任务处理及资源管理。

### 学习内容

- Thread / Runnable：线程创建、start、run、join、sleep
- 线程安全：synchronized、volatile、AtomicInteger、ReentrantLock
- ExecutorService：线程池、Callable、Future、任务异常
- 任务生命周期：超时、中断、取消、shutdown、shutdownNow
- ThreadPoolExecutor：有界队列、任务拒绝策略
- Semaphore：限制共享资源的并发访问
- ConcurrentHashMap：putIfAbsent 与线程安全边界
- CompletableFuture：thenApply、thenCombine、handle
- Virtual Thread：虚拟线程基础

### 综合实验

入口：`learning.concurrent.ConcurrentProductTaskDemo`

使用 6 条固定模拟商品请求，每条模拟等待约 300ms。

实现：

- 顺序执行与并发执行对比
- 有界线程池与最大活跃任务数统计
- Future 获取任务结果及异常
- 5 成功、1 失败的部分失败场景
- 按输入顺序汇总成功与失败 SKU
- 等待超时、取消请求与清理确认
- 线程池关闭

### 实测结果

| 模式 | 耗时 |
|---|---:|
| 顺序执行 | 1843ms |
| 并发数 2 | 926ms |
| 并发数 3 | 608ms |

模拟失败场景：5 成功、1 失败。

最新 Maven 构建成功，JUnit 10 项通过。

### 运行方式

在 `week22-java-core` 目录执行：

```bash
mvn clean package
java -cp target/week22-java-core-1.0-SNAPSHOT.jar learning.concurrent.ConcurrentProductTaskDemo
```

### 实验边界

- 不调用真实外部 API、数据库或 LLM
- 不提供任务持久化与崩溃恢复
- 不保证 Exactly-once
- 不代表真实生产环境的性能
- 未将原 ProductCatalog 改造为线程安全系统

# Week25 — Spring Boot Web

本项目是 Java 后端学习 Week25 的实践工程，使用 Spring Boot 实现一个基于内存的商品 HTTP 服务。

## 技术环境

- Java：运行 JDK 27，编译目标 Java 21
- Maven：3.10.0
- Spring Boot：4.1.2
- Spring MVC、Bean Validation
- JUnit 5、MockMvc
- 内嵌 Tomcat

## 学习内容

- Spring IoC、Bean 与构造器注入
- Spring Boot 自动配置和组件扫描
- Spring MVC 请求映射、参数绑定与 JSON 序列化
- 请求体校验与商品业务规则
- 统一异常处理和 HTTP 状态码
- `@ConfigurationProperties` 配置管理
- SLF4J 日志
- MockMvc 自动化测试
- Maven 打包和可执行 JAR

## 项目结构

- `Week25SpringWebApplication`：Spring Boot 启动入口
- `HealthController`：健康检查
- `ProductController`：商品 HTTP 接口
- `ProductService`：商品业务与内存数据管理
- `Product`：只读商品值对象
- `CreateProductRequest`、`UpdateProductRequest`：请求模型
- `ProductExceptionHandler`：统一异常处理
- `CatalogProperties`：商品列表默认数量配置

## HTTP 接口

| 方法 | 路径 | 功能 |
|---|---|---|
| GET | `/health` | 健康检查 |
| POST | `/products` | 创建商品 |
| GET | `/products/{sku}` | 查询商品 |
| GET | `/products` | 查询商品列表 |
| PUT | `/products/{sku}` | 替换商品名称和库存 |
| DELETE | `/products/{sku}` | 删除商品 |

列表支持 `limit` 和 `sortBySku` 参数。其中 `sortBySku=true` 表示按 SKU 升序排列，排序在数量限制之前执行。

主要 HTTP 状态约定：

- 200：查询或更新成功
- 201：商品创建成功
- 204：商品删除成功
- 400：请求数据不合法
- 404：商品不存在
- 409：SKU 重复
- 500：未预期的服务端错误

## 配置与启动

配置文件：`src/main/resources/application.properties`

默认监听 `127.0.0.1:8080`，支持通过 `catalog.default-limit` 调整列表默认查询数量，允许范围为 1～100。

在 `week25-spring-web` 工程目录运行：

- 开发启动：`mvn spring-boot:run`
- 运行测试：`mvn test`
- 构建 JAR：`mvn clean package`
- 运行 JAR：`java -jar target/week25-spring-web-0.0.1-SNAPSHOT.jar`

## 测试

目前共有 5 个自动化测试，其中 4 个是商品 Web 测试，1 个是 Spring Boot 上下文测试。

已验证创建、查询、重复 SKU、非法输入及商品不存在等场景，并通过真实 HTTP 请求检查主要接口。

## 数据与并发边界

商品存储在当前 JVM 的 `LinkedHashMap` 中，应用重启后恢复为初始化数据。

Service 使用同步方法保护内部 Map 操作；商品使用只读值对象，更新时创建新对象并替换原值。

本项目只面向单 JVM 内存实践，不提供跨进程数据共享、数据库持久化或分布式一致性。

项目目前没有用户认证，仅用于本地学习，不应直接公开部署。


## Week26 — Spring Boot + MySQL + MyBatis

在 Week25 Spring Boot 商品 API 的基础上，
将商品存储从内存 LinkedHashMap 迁移到 MySQL。

### 学习与实践内容

- JDBC：Connection、PreparedStatement、ResultSet
- MySQL：商品表、唯一约束、库存约束
- HikariCP：DataSource 与连接池
- MyBatis：Mapper 接口、XML SQL、结果映射
- 商品 API：GET、POST、PUT、DELETE、列表排序与 limit
- Spring 事务：@Transactional、SELECT FOR UPDATE
- 商品变更记录：product_change_log
- 测试隔离：开发数据库与测试数据库分离

### 数据库

- 开发库：week26_catalog_dev
- 测试库：week26_catalog_test
- 主要表：products、product_change_log

数据库表结构需提前准备，当前工程不自动建表。

### 构建与运行

在 week25-spring-web 目录执行：

mvn clean package

java -jar target/week25-spring-web-0.0.1-SNAPSHOT.jar

运行前需提供数据库连接配置：

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD

数据库密码通过环境变量提供，不提交到 Git。

### 验证结果

- Maven 测试：5 项通过
- Maven 打包：BUILD SUCCESS
- JAR 独立启动及 MySQL 商品查询通过
- Java 应用重启后商品仍可查询
- Spring 事务提交与回滚实验通过