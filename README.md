## 基于apache httpclient的轻量http请求

### 特性
> 1.  基于httpclient, 仅暴露常用的配置, 填掉了常见的坑
> 2.  接口设计参考jsoup, 简易的语法让人耳目一新
> 3.  支持所有的请求方法:GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS,TRACE
> 4.  出参入参对json极为友好, 自动解析返回对象to pojo
> 5.  可以像rpc一样基于java api发送http请求, 代码更加规范稳定, 易于管理
> 6.  当返回值为HttpResult时, 调用失败也不会抛出异常.
> 7.  支持返回值取json path, 例如:msg.user.id/msg.user[1].id 参考测试包下 ResultJsonPathTest
> 8.  支持设计重试次数, 建议get请求都有重试机制, 参考测试包下 RetryTest
> 9.  支持上传各种类型的文件, 支持File/InputStream/byte[]/base64上传, 参考测试包下 UploadFileTest
> 10. 支持@PathVariable 如 http://*.com/{key}/info -> http://*.com/xiaojie.chen/info, 参考测试包下 SimpleTest
> 11. 支持请求拦截器和自定义httpclient, 参考 CustomHttpClientTest

### 添加依赖

```xml
<dependency>
    <groupId>com.chenxiaojie</groupId>
    <artifactId>http-invoker</artifactId>
    <version>1.0.2</version>
</dependency>
```

如果您的项目中有依赖以下包,请指定以下版本或高于以下版本

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.1</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpcore</artifactId>
    <version>4.4.3</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpmime</artifactId>
    <version>4.5.1</version>
</dependency>
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.9</version>
</dependency>
```

### 快速入门

http-invoker-demo-web 是专门用于测试, 运行以下代码时, 请先启动这个web项目

##### 使用HttpInvoker, 参考测试包下 HttpInvokerTest

```java

@Test
public void testGet() {
     HttpInvoker.Response response = HttpInvoker.builder()
                     .uri("https://www.dianping.com")
                     .get();
}


@Test
public void testPost() {
   HttpInvoker.Response response = HttpInvoker.builder()
                   .uri(Consts.URL + "/simple/3")
                   .data("employeeId", "00160043")
                   .data("ad", "xiaojie.chen", "ad", "xiaojie.chen3")
                   .post();
}


@Test
public void testPostJson() {
    UserLoginModel userLoginModel = new UserLoginModel();
    userLoginModel.setLoginId(5);
    userLoginModel.setEmployeeId("0016004");
    userLoginModel.setEmployeeName("陈孝杰");
    userLoginModel.setAd("xiaojie.chen");

    HttpInvoker.Response response = HttpInvoker.builder()
            .uri(Consts.URL + "/postJson")
            .json(userLoginModel)
            .post();
}


@Test
public void testPostFile() {
    InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
    
    HttpInvoker.Response response = HttpInvoker.builder()
            .uri(Consts.URL + "/file")
            .data("fileinput", "attachment.png", in)
            .post();
}
```

##### 通过api的方式请求

java api

```java
public interface SimpleHttpApi {

    //get
    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET)
    Response<UserLoginModel> get(@PathVariable("loginId") int loginId,
                                          @RequestParam(value = "employeeId") String employeeId,
                                          @RequestParam(value = "employeeName") String employeeName,
                                          @RequestParam(value = "ad") String ad);

    //post
    @RequestMapping(value = "/{path}", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> post(@RequestBody UserLoginModel userLoginModel,
                                                  @PathVariable("path") String path,
                                                  @RequestParam(value = "employeeId") String employeeId,
                                                  @RequestParam(value = "employeeId") String employeeId2,
                                                  @RequestParam(value = "employeeId") String employeeId3,
                                                  @RequestParam(value = "employeeName") String employeeName,
                                                  @RequestParam(value = "ad") String ad);
    
    //postJson
    @RequestMapping(value = "/simple", method = HttpMethod.POST)
        HttpResult<Response<UserLoginModel>> postJosn(@RequestBody UserLoginModel userLoginModel);
    
    //upload
    @RequestMapping(value = "/{path}", method = HttpMethod.POST)
    HttpResult<Response<String>> upload(@RequestFile("a1.png") InputStream in,
                                        @RequestFile("a2.png") File file,
                                        @RequestFile("a3.png") byte[] fileBytes,
                                        @RequestFile("a4.png") String base64,
                                        @PathVariable("path") String path,
                                        @RequestParam(value = "employeeId") String employeeId,
                                        @RequestParam(value = "employeeId") String employeeId2,
                                        @RequestParam(value = "employeeId") String employeeId3,
                                        @RequestParam(value = "employeeName") String employeeName,
                                        @RequestParam(value = "ad") String ad);
    
    //retry
    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET, retryTimes = 1)
    HttpResult<Response<UserLoginModel>> retry(@PathVariable("loginId") String loginId,
                                                          @RequestParam(value = "employeeId") String employeeId,
                                                          @RequestParam(value = "employeeName") String employeeName,
                                                          @RequestParam(value = "ad") String ad);
    
    //jsonPath
    @RequestMapping(value = "/simple/list", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data[1].employeeName")
    String jsonPath();

}
```

通过java 请求

```java
HttpInvocationHandler httpInvocationHandler = new HttpInvocationHandler();
httpInvocationHandler.setRequestUrlPrefix(Consts.URL);

SimpleHttpApi simpleHttpApi = (SimpleHttpApi) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[]{SimpleHttpApi.class},
        httpInvocationHandler);

UserLoginModel userLoginModel = new UserLoginModel();
userLoginModel.setLoginId(1);
userLoginModel.setEmployeeId("0016004");
userLoginModel.setEmployeeName("陈孝杰");
userLoginModel.setAd("xiaojie.chen");

Response<UserLoginModel> response = simpleHttpApi.addUser(userLoginModel);

System.out.println(response.getData());
```

通过spring xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean id="httpParent" class="com.chenxiaojie.http.invoker.spring.HttpProxyFactoryBean" abstract="true" init-method="init">
        <property name="invocationHandler">
            <bean class="com.chenxiaojie.http.invoker.proxy.HttpInvocationHandler">
                <property name="requestUrlPrefix" value="http://localhost:8081/httpinvoker"></property>
            </bean>
        </property>
    </bean>

    <!-- 只需配置下方代码即可 -->
    <bean id="simpleHttpApi" parent="httpParent">
        <property name="proxyInterfaces" value="com.chenxiaojie.http.invoker.test.http.api.SimpleHttpApi"/>
    </bean>
</beans>
```

### 建议

##### 如果请求量比较大, 请尽量自定义httpclient, 否则全局共享一个默认的httpclient
##### 如果请求量比较大, 请将http请求详细日志重置到其他位置或者关闭

日志位置重置,如果您的项目中没有依赖slf4j,请添加以下配置

```xml
<!-- slf4j -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.2</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>1.7.2</version>
</dependency>
<!-- log4j2 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-1.2-api</artifactId>
    <version>2.7</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.7</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.7</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.7</version>
</dependency>
```

log4j2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="info">

    <Properties>
        <Property name="log-path">/data/applogs/http-invoker-demo/logs</Property>
        <Property name="pattern">%d{yyyy-MM-dd HH:mm:ss.SSS} %level [%t] [%c] %msg%xEx%n</Property>
    </Properties>

    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT" follow="true">
            <PatternLayout pattern="${pattern}"/>
        </Console>

        <RollingFile name="AppAppender" fileName="${log-path}/app.log" filePattern="${log-path}/app.log.%d{yyyy-MM-dd}">
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
        </RollingFile>

        <RollingFile name="HttpAppender" fileName="${log-path}/http.log" filePattern="${log-path}/http.log.%d{yyyy-MM-dd}">
            <PatternLayout pattern="${pattern}"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1"/>
            </Policies>
        </RollingFile>

        <Async name="AsyncAppAppender">
            <AppenderRef ref="AppAppender" level="info"/>
        </Async>

    </Appenders>

    <loggers>
        <logger name="com.chenxiaojie.http.invoker.HttpInvoker" additivity="false">
            <appender-ref ref="HttpAppender"/>
            <appender-ref ref="ConsoleAppender"/>
        </logger>

        <root level="info">
            <appender-ref ref="ConsoleAppender"/>
            <appender-ref ref="AsyncAppAppender"/>
        </root>
    </loggers>

</configuration>
```

关闭日志
spring xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean id="httpParent" class="com.chenxiaojie.http.invoker.spring.HttpProxyFactoryBean" abstract="true"
          init-method="init">
        <property name="invocationHandler">
            <bean class="com.chenxiaojie.http.invoker.proxy.HttpInvocationHandler">
                <property name="requestUrlPrefix" value="http://localhost:8081/httpinvoker"></property>
                <property name="openLog" value="true"></property>
            </bean>
        </property>
    </bean>

    <!-- 只需配置下方代码即可 -->
    <bean id="simpleHttpApi" parent="httpParent">
        <property name="proxyInterfaces" value="com.chenxiaojie.http.invoker.test.http.api.SimpleHttpApi"/>
    </bean>
</beans>
```

### 打包命令

mvn clean install -DskipTests

### 维护

有任何问题请随时联系:陈孝杰, qq:3262515, email: 3262515@qq.com