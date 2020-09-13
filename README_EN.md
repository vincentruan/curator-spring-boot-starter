[中文介绍](README.md)

# curator-spring-boot-starter[non-official]

## How to use

### 1. Clone

```shell script
git clone git@github.com:vincentruan/curator-spring-boot-starter.git
```

### 2. Build and install

```shell script
cd curator-spring-boot-starter
mvn clean install
```


### 3. Modify maven pom.xml

* add parent reference on pom.xml in your Spring Boot project.[Optional]
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>${spring-boot.version}</version>
</parent>
 ```

* Add dependency. [Required]
```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>curator-spring-boot-starter</artifactId>
     <version>${spring-boot.version}</version>
 </dependency>
 ```

 * Package to a executable jar file by maven plugin, add plugin as below.
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>${spring-boot.version}</version>
</plugin>
```

### 4. Example



### 5. Run

> Launch by main class

> Packaged to an executable Jar by command `mvn clean package`