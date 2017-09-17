package com.chenxiaojie.http.invoker.test.http.test;

import com.alibaba.fastjson.JSON;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.test.http.api.SimpleHttpApi;
import com.chenxiaojie.http.invoker.test.vo.Response;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenxiaojie on 16/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/config/spring/local/appcontext-simple.xml"})
public class SimpleTest {

    @Autowired
    private SimpleHttpApi simpleHttpApi;

    @Test
    public void test() {
        Response<UserLoginModel> response = simpleHttpApi.queryByLoginId(1);

        System.out.println(response.getData());
    }

    @Test
    public void test2() {
        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.queryHttpResultByLoginId(2);

        System.out.println(response.getData());
    }

    @Test
    public void test3() {
        Response<UserLoginModel> response = simpleHttpApi.getByLoginId(3, "0016004", "陈孝杰", "xiaojie.chen");

        System.out.println(response.getData());
    }

    @Test
    public void test4() {
        Response<UserLoginModel> response = simpleHttpApi.addByLoginId(4, "0016004", "陈孝杰", "xiaojie.chen");

        System.out.println(response.getData());
    }


    @Test
    public void test5() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(5);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        Response<UserLoginModel> response = simpleHttpApi.addUser(userLoginModel);

        System.out.println(response.getData());
    }

    @Test
    public void test6() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(6);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.addUser2(userLoginModel);

        System.out.println(response.getData());
    }

    @Test
    public void test7() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(7);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.addUser3(JSON.toJSONString(userLoginModel));

        System.out.println(response);
    }

    @Test
    public void test8() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(8);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.addUser4(JSON.toJSONString(userLoginModel));

        System.out.println(response);
    }

    @Test
    public void test9() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(9);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.addUser5(userLoginModel,
                "simple",
                "0016004",
                "0016005",
                "0016006",
                "陈孝杰",
                "xiaojie.chen");

        System.out.println(response);
    }

    @Test
    public void test10() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.updateUser(userLoginModel,
                10,
                "0016004",
                "0016005",
                "0016006",
                "陈孝杰",
                "xiaojie.chen");

        System.out.println(response);
    }

    @Test
    public void test11() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(0);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<Response<UserLoginModel>> response = simpleHttpApi.patchUser(userLoginModel,
                11,
                "0016004",
                "0016005",
                "0016006",
                "陈孝杰",
                "xiaojie.chen");

        System.out.println(response);
    }


    @Test
    public void test12() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(0);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        HttpResult<String> response = simpleHttpApi.deleteUser(userLoginModel,
                12,
                "0016004",
                "0016005",
                "0016006",
                "陈孝杰",
                "xiaojie.chen");

        System.out.println(response);
    }


}