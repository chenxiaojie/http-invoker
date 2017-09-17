package com.chenxiaojie.http.invoker.test.http.test;

import com.chenxiaojie.http.invoker.test.http.api.SimpleHttpApi;
import com.chenxiaojie.http.invoker.test.vo.Response;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;
import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenxiaojie on 16/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/config/spring/local/appcontext-custom-httpclient.xml"})
public class CustomHttpClientTest {

    @Autowired
    private SimpleHttpApi simpleHttpApi;

    @Autowired
    @Qualifier("httpClient")
    private HttpClient httpClient1;

    @Autowired
    @Qualifier("httpClient")
    private HttpClient httpClient2;

    @Autowired
    @Qualifier("httpClient")
    private HttpClient httpClient3;

    @Test
    public void test() {
        UserLoginModel userLoginModel = new UserLoginModel();
        userLoginModel.setLoginId(1);
        userLoginModel.setEmployeeId("0016004");
        userLoginModel.setEmployeeName("陈孝杰");
        userLoginModel.setAd("xiaojie.chen");

        Response<UserLoginModel> response = simpleHttpApi.addUser(userLoginModel);

        System.out.println(response.getData());
    }
}