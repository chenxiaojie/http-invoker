package com.chenxiaojie.http.invoker.test.java;

import com.chenxiaojie.http.invoker.proxy.HttpInvocationHandler;
import com.chenxiaojie.http.invoker.test.consts.Consts;
import com.chenxiaojie.http.invoker.test.http.api.SimpleHttpApi;
import com.chenxiaojie.http.invoker.test.vo.Response;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;

import java.lang.reflect.Proxy;


/**
 * Created by chenxiaojie on 16/9/10.
 */
public class ApiJavaCallTest {

    public static void main(String[] args) {
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
    }
}
