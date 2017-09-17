package com.chenxiaojie.http.invoker.test.http.test;

import com.chenxiaojie.http.invoker.test.http.api.ResultJsonPathHttpApi;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by chenxiaojie on 16/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/config/spring/local/appcontext-simple.xml"})
public class ResultJsonPathTest {

    @Autowired
    private ResultJsonPathHttpApi resultJsonPathHttpApi;

    @Test
    public void test() {
        UserLoginModel response = resultJsonPathHttpApi.getByLoginId(1, "0016004", "陈孝杰", "xiaojie.chen");

        System.out.println(response);
    }

    @Test
    public void test2() {
        String response = resultJsonPathHttpApi.getByLoginId2(1, "0016004", "陈孝杰", "xiaojie.chen");

        System.out.println(response);
    }

    @Test
    public void test3() {
        List<UserLoginModel> response = resultJsonPathHttpApi.queryUsers();
        System.out.println(response);
    }

    @Test
    public void test4() {
        UserLoginModel response = resultJsonPathHttpApi.queryUsers2();
        System.out.println(response);
    }

    @Test
    public void test5() {
        String response = resultJsonPathHttpApi.queryUsers3();
        System.out.println(response);
    }


}