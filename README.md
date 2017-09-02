[English Version](README_EN.md)

#spring-boot-start-curator

## 如何使用

### 1. clone 代码

```
git clone git@github.com:vincentruan/spring-boot-starter-curator.git
```

### 2. 编译安装

```
cd spring-boot-starter-curator
mvn clean install
```


### 3. 修改maven配置文件(可以参考样例[hydra-batch-job](https://github.com/vincentruan/hydra/tree/1.0.0-DEV-snapshot/hydra-batch-job))

* 在Spring Boot项目的pom.xml增加parent:[非必须]
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.3.5.RELEASE</version>
</parent>
 ```

* 在Spring Boot项目的pom.xml中添加以下依赖:
```
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-curator</artifactId>
     <version>1.3.5.RELEASE</version>
 </dependency>
 ```

 * maven插件用于打包成可执行的jar文件,添加以下插件(这里一定要加载需要打包成jar的mudule的pom中)
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>1.3.5.RELEASE</version>
</plugin>
```

### 4. Example



### 5. 打包运行

> 可以直接执行main启动

> 可以通过mvn clean package 打包成可执行的jar文件
