[English Version](README_EN.md)

# curator-spring-boot-starter[非官方]

## 如何使用

### 1. clone 代码

```shell script
git clone git@github.com:vincentruan/curator-spring-boot-starter.git
```

### 2. 编译安装

```shell script
cd curator-spring-boot-starter
mvn clean install
```


### 3. 修改maven配置文件(可以参考样例[hydra-batch-job](https://github.com/vincentruan/hydra/tree/1.0.0-DEV-snapshot/hydra-batch-job))

* 在Spring Boot项目的pom.xml增加parent:[非必须]
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>${spring-boot.version}</version>
</parent>
```

* 在Spring Boot项目的pom.xml中添加以下依赖:
```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>curator-spring-boot-starter</artifactId>
     <version>${spring-boot.version}</version>
 </dependency>
```

 * maven插件用于打包成可执行的jar文件,添加以下插件(这里一定要加载需要打包成jar的mudule的pom中)
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>${spring-boot.version}</version>
</plugin>
```

### 4. Example



### 5. 打包运行

> 可以直接执行main启动

> 可以通过mvn clean package 打包成可执行的jar文件

## 其他
### 模块命名说明
从springboot官方文档摘录如下：
> Do not start your module names with spring-boot, even if you use a different Maven groupId. We may offer official support for the thing you auto-configure in the future.As a rule of thumb, you should name a combined module after the starter.

从这段话可以看出spring-boot-starter命名的潜规则
- spring-boot-starter-XX是springboot官方的starter
- XX-spring-boot-starter是第三方扩展的starter

### configuration-processor
关于spring-boot-configuration-processor的说明，引自springBoot官方文档：
> Spring Boot uses an annotation processor to collect the conditions on auto-configurations in a metadata file ( META-INF/spring-autoconfigure-metadata.properties ). If that file is present, it is used to eagerly filter auto-configurations that do not match, which will improve startup time. It is recommended to add the following dependency in a module that contains auto-configurations:org.springframework.bootspring-boot-autoconfigure-processortrue

写starter模块时，在pom中配置spring-boot-autoconfigure-processor，在编译时会自动收集配置类的条件，写到一个META-INF/spring-autoconfigure-metadata.properties中。

### 自动配置逻辑各种conditional说明

| 类型                                              | 注解                          | 说明                                                         |
| :------------------------------------------------ | :---------------------------- | :----------------------------------------------------------- |
| Class Conditions类条件注解                        | @ConditionalOnClass           | 当前classpath下有指定类才加载                                |
| @ConditionalOnMissingClass                        | 当前classpath下无指定类才加载 |                                                              |
| Bean ConditionsBean条件注解                       | @ConditionalOnBean            | 当期容器内有指定bean才加载                                   |
| @ConditionalOnMissingBean                         | 当期容器内无指定bean才加载    |                                                              |
| Property Conditions环境变量条件注解（含配置文件） | @ConditionalOnProperty        | prefix 前缀name 名称havingValue 用于匹配配置项值matchIfMissing 没找指定配置项时的默认值 |
| ResourceConditions 资源条件注解                   | @ConditionalOnResource        | 有指定资源才加载                                             |
| Web Application Conditionsweb条件注解             | @ConditionalOnWebApplication  | 是web才加载                                                  |
| @ConditionalOnNotWebApplication                   | 不是web才加载                 |                                                              |
| SpEL Expression Conditions                        | @ConditionalOnExpression      | 符合SpEL 表达式才加载                                        |


