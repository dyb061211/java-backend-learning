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
