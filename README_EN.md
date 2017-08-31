[中文介绍](README.md)

#spring-boot-start-curator[non-official]

## How to use

### 1. Clone

```
git clone git@github.com:vincentruan/spring-boot-starter-curator.git
```

### 2. Build and install

```
cd spring-boot-starter-curator
mvn clean install
```


### 3. Modify maven pom.xml

* add parent reference on pom.xml in your Spring Boot project.[Optional]
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.2.RELEASE</version>
</parent>
 ```

* Add dependency. [Required]
```
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-curator</artifactId>
     <version>1.4.2.RELEASE</version>
 </dependency>
 ```

 * Package to a executable jar file by maven plugin, add plugin as below.
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>1.4.2.RELEASE</version>
</plugin>
```

### 4. Example
(See [hydra-batch-job](https://github.com/vincentruan/hydra/tree/1.0.0-DEV-snapshot/hydra-batch-job))


### 5. Run

> Launch by main class

> Packaged to an executable Jar by command `mvn clean package`