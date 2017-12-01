## 基于apache httpclient的轻量http请求

### 特性
> 1.  封装了httpclient, 仅暴露常用的配置, 将常见的坑填上
> 2.  接口设计参考jsoup http, 十分便捷的请求让人耳目一新
> 3.  支持所有的请求方式, GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS,TRACE
> 4.  可以像rpc请求一样发http请求, 代码更加规范稳定, 易于管理
> 5.  出参入参对json极为友好, 自动解析返回对象to pojo
> 6.  当返回值为HttpResult时, 调用失败也不会抛出异常. GET请求不建议使用HttpResult, 非GET如POST/PUT/DELETE请求都建议使用HttpResult
> 7.  支持返回值取jsonPath, 例如:msg.user.id/msg.user[1].id 参考测试包下com.chenxiaojie.http.invoker.test.http.test.ResultJsonPathTest
> 8.  支持设计重试次数, 建议get请求都有重试机制, 参考测试包下com.chenxiaojie.http.invoker.test.http.test.RetryTest
> 9.  支持上传各种类型的文件, 支持File/InputStream/byte[]/base64上传, 参考测试包下com.chenxiaojie.http.invoker.test.http.test.UploadFileTest
> 10. 支持@PathVariable 如 http://*.com/{key}/info -> http://*.com/xiaojie.chen/info, 参考测试包下com.chenxiaojie.http.invoker.test.http.test.SimpleTest
> 11. 支持请求拦截器和自定义httpclient, 参考com.chenxiaojie.http.invoker.test.http.test.CustomHttpClientTest

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

http-invoker-demo-web module是专门用于测试, 运行以下代码时, 请先启动这个web项目

##### 使用HttpInvoker, 参考测试包下com.chenxiaojie.http.invoker.test.httpinvoker.HttpInvokerTest

```java

@Test
public void testGet() {
     HttpInvoker.Response response = HttpInvoker.builder()
                    .uri(Consts.URL + "/simple/2")
                    .data("employeeId", "00160042")
                    .data(ImmutableMap.of("employeeName", "陈孝杰2"))
                    .data("ad", "xiaojie.chen", "ad", "xiaojie.chen2")
                    .get();
    
    response.log();
    Assert.assertTrue(response.isSuccess());
}
   

@Test
public void testPost() {
    InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
    InputStream in2 = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
    HttpInvoker.Response response = HttpInvoker.builder()
            .uri(Consts.URL + "/file")
            .data("employeeId", "00160043")
            .data(ImmutableMap.of("employeeName", "陈孝杰3"))
            .data("ad", "xiaojie.chen", "ad", "xiaojie.chen3")
            .data("fileinput", "attachment.png", in)
            .data("fileinput2", "attachment2.png", in2)
            .post();

    response.log();
    Assert.assertTrue(response.isSuccess());
}


@Test
public void testCookie() {
    BasicClientCookie cookie1 = new BasicClientCookie("Auth", "AuthAuth");
    cookie1.setPath("/");
    cookie1.setDomain("localhost");

    BasicClientCookie cookie2 = new BasicClientCookie("Auth2", "Auth2Auth2");
    cookie2.setPath("/");
    cookie2.setDomain("localhost");

    BasicClientCookie cookie3 = new BasicClientCookie("Auth3", "Auth3Auth3");
    cookie3.setPath("/");
    cookie3.setDomain("localhost");

    HttpInvoker.Response response = HttpInvoker.builder(HttpClientBuilder.builder()
            .header("AAA", "VVV")
            .header(HttpHeaders.USER_AGENT, "VVVVVVFSDSFSF")
            .cookie(cookie1)
            .cookies(Lists.<Cookie>newArrayList(cookie2, cookie3))
            .build())
            .uri(Consts.URL + "/simple/3")
            .data("employeeId", "00160041")
            .data(ImmutableMap.of("employeeName", "陈孝杰1"))
            .data("ad", "xiaojie.chen", "ad", "xiaojie.chen1")
            .header("AAA", "BBB")
            .header("AAA", "BBB2")
            .headers(ImmutableMap.of("BBB", "CCC"))
            .headers(ImmutableMap.of(HttpHeaders.USER_AGENT, "ASSSDDSDSDD"))
            .cookie("Auth", "123")
            .cookies(ImmutableMap.of("Auth5", "Auth5Auth5", "Auth6", "Auth6Auth6"))
            .get();

    response.log();
    Assert.assertTrue(response.isSuccess());
}


@Test
public void testInterceptor() {

    HttpInvoker.Response response = HttpInvoker.builder()
            .uri(Consts.URL + "/simple/3")
            .data("employeeId", "00160041")
            .data(ImmutableMap.of("employeeName", "陈孝杰1"))
            .data("ad", "xiaojie.chen", "ad", "xiaojie.chen1")
            .header("AAA", "BBB")
            .header("AAA", "BBB2")
            .headers(ImmutableMap.of("BBB", "CCC"))
            .headers(ImmutableMap.of(HttpHeaders.USER_AGENT, "ASSSDDSDSDD"))
            .cookie("Auth", "123")
            .cookies(ImmutableMap.of("Auth5", "Auth5Auth5", "Auth6", "Auth6Auth6"))
            .interceptor(new HttpInvoker.Interceptor() {
                @Override
                public boolean intercept(HttpRequestBase httpRequestBase) throws HttpRequestException {
                    System.out.println(httpRequestBase);
                    return true;
                }
            })
            .interceptor(new BasicAuthInterceptor("AAA", "BBB", "UTF-8"))
            .get();

    response.log();
    Assert.assertTrue(response.isSuccess());
}
```

##### 通过api的方式请求

java api

```java
public interface SimpleHttpApi {

    //get
    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET)
    Response<UserLoginModel> getByLoginId(@PathVariable("loginId") int loginId,
                                          @RequestParam(value = "employeeId") String employeeId,
                                          @RequestParam(value = "employeeName") String employeeName,
                                          @RequestParam(value = "ad") String ad);


    //post
    @RequestMapping(value = "/{path}", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> addUser5(@RequestBody UserLoginModel userLoginModel,
                                                  @PathVariable("path") String path,
                                                  @RequestParam(value = "employeeId") String employeeId,
                                                  @RequestParam(value = "employeeId") String employeeId2,
                                                  @RequestParam(value = "employeeId") String employeeId3,
                                                  @RequestParam(value = "employeeName") String employeeName,
                                                  @RequestParam(value = "ad") String ad);
    
    
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
    HttpResult<Response<UserLoginModel>> getByLoginId(@PathVariable("loginId") String loginId,
                                                          @RequestParam(value = "employeeId") String employeeId,
                                                          @RequestParam(value = "employeeName") String employeeName,
                                                          @RequestParam(value = "ad") String ad);
    
    
    //jsonPath
    @RequestMapping(value = "/simple/list", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data[1].employeeName")
    String queryUsers();

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

测试类请参考com.chenxiaojie.http.invoker.test.http.test.SimpleTest


### 建议

##### 如果请求量比较大, 请尽量自定义httpclient, 否则全局共享一个默认的httpclient
##### 如果请求量比较大, 请将http请求详细日志重置到其他位置或者关闭

日志位置重置

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

mvn clean install -DskipTests -Dmaven.javadoc.skip=true

### 维护

有任何问题请随时联系:陈孝杰, qq:3262515, email: 3262515@qq.com

