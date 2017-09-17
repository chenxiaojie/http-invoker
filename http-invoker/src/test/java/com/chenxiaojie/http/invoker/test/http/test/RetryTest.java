package com.chenxiaojie.http.invoker.test.http.test;

import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.test.http.api.RetryHttpApi;
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
public class RetryTest {

    @Autowired
    private RetryHttpApi retryHttpApi;

    @Test
    public void test() {
        HttpResult<Response<UserLoginModel>> response = retryHttpApi.getByLoginId("abc", "0016004", "陈孝杰", "xiaojie.chen");

        System.out.println(response);
    }


}