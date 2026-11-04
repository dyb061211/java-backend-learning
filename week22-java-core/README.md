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

进入包含 `pom.xml` 的项目目录。

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