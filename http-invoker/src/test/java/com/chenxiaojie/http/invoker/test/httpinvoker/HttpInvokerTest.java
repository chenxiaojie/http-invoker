package com.chenxiaojie.http.invoker.test.httpinvoker;

import com.alibaba.fastjson.JSON;
import com.chenxiaojie.http.invoker.HttpInvoker;
import com.chenxiaojie.http.invoker.client.HttpClientBuilder;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.exception.HttpRequestException;
import com.chenxiaojie.http.invoker.interceptor.BasicAuthInterceptor;
import com.chenxiaojie.http.invoker.test.consts.Consts;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenxiaojie on 2017/9/10.
 */
public class HttpInvokerTest {

    @Test
    public void testGet() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri("https://www.dianping.com")
                .get();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testGet2() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/2")
                .data("employeeId", "00160042")
                .data(ImmutableMap.of("employeeName", "陈孝杰2"))
                .data("ad", "xiaojie.chen", "ad", "xiaojie.chen2")
                .get();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGet3() {
        thrown.expect(HttpRequestException.class);

        HttpInvoker.Response response = HttpInvoker.builder(HttpClientBuilder.builder().followRedirects(true).build())
                .uri(Consts.URL + "/redirect/loop")
                .get();

        response.log();
    }

    @Test
    public void testGet4() throws IOException {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/static/logo.png")
                .get();
        response.log();
        //FileUtils.writeByteArrayToFile(new File("test.png"), response.bodyBytes());
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testPost() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/3")
                .data("employeeId", "00160043")
                .data(ImmutableMap.of("employeeName", "陈孝杰3"))
                .data("ad", "xiaojie.chen", "ad", "xiaojie.chen3")
                .post();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testPost2() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(5);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple?employeeName=employeeName陈孝杰")
                .data("ad", "xiaojie.chen", "ad", "xiaojie.chen3")
                .json(userLoginModel)
                .post();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testPost3() {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        InputStream in2 = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/file")
                .data("employeeId", "00160043")
                .data(ImmutableMap.of("employeeName", "陈孝杰3"))
                .data("ad", "xiaojie.chen", "ad", "xiaojie.chen3")
                .data("fileinput", "attachment.pNg", in)
                .data("fileinput2", "attachment", in2)
                .post();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testPut() {

        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/1")
                .json(JSON.toJSONString(userLoginModel))
                .put();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testPatch() {

        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/2")
                .json(JSON.toJSONString(userLoginModel))
                .patch();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testDelete() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/1")
                .data("aa", "陈孝杰")
                .delete();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testHead() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/2")
                .head();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testOptions() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/3")
                .options();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testTrace() {
        HttpInvoker.Response response = HttpInvoker.builder()
                .uri(Consts.URL + "/simple/4")
                .trace();

        response.log();
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testTrace2() {
        HttpRequestException httpRequestException = null;
        try {
            HttpInvoker.Response response = HttpInvoker.builder(HttpClientBuilder.builder().followRedirects(true).build())
                    .uri(Consts.URL + "/redirect/loop")
                    .trace();
            response.log();
        } catch (HttpRequestException e) {
            httpRequestException = e;
        }

        Assert.assertNull(httpRequestException);
    }

    @Test
    public void testHeaders() {
        HttpInvoker.Response response = HttpInvoker.builder(HttpClientBuilder.builder()
                .header("AAA", "VVV")
                .header(HttpHeaders.USER_AGENT, "VVVVVVFSDSFSF")
                .build())
                .uri(Consts.URL + "/simple/3")
                .data("employeeId", "00160041")
                .data(ImmutableMap.of("employeeName", "陈孝杰1"))
                .data("ad", "xiaojie.chen", "ad", "xiaojie.chen1")
                .header("AAA", "BBB")
                .header("AAA", "BBB2")
                .headers(ImmutableMap.of("BBB", "CCC"))
                .headers(ImmutableMap.of(HttpHeaders.USER_AGENT, "ASSSDDSDSDD"))
                .get();

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

}
