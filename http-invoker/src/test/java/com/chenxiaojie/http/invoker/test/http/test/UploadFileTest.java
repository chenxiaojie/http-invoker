package com.chenxiaojie.http.invoker.test.http.test;

import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.test.http.api.UploadFileApi;
import com.chenxiaojie.http.invoker.test.vo.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenxiaojie on 16/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/config/spring/local/appcontext-simple.xml"})
public class UploadFileTest {

    @Autowired
    private UploadFileApi uploadFileApi;

    @Test
    public void test() {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");

        HttpResult<Response<String>> response = uploadFileApi.upload(in);

        System.out.println(response.getData());
    }

    @Test
    public void test2() {
        File file = new File(Thread.currentThread().getClass().getResource("/logo.png").getFile());

        HttpResult<Response<String>> response = uploadFileApi.upload(file);

        System.out.println(response.getData());
    }

    @Test
    public void test3() throws IOException {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        HttpResult<Response<String>> response = uploadFileApi.upload(IOUtils.toByteArray(in));
        System.out.println(response.getData());
    }

    @Test
    public void test4() throws IOException {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        HttpResult<Response<String>> response = uploadFileApi.upload(Base64.encodeBase64String(IOUtils.toByteArray(in)));
        System.out.println(response.getData());
    }

    @Test
    public void test5() throws IOException {
        InputStream in1 = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        InputStream in2 = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        InputStream in3 = Thread.currentThread().getClass().getResourceAsStream("/logo.png");
        HttpResult<Response<String>> response = uploadFileApi.upload(
                in1,
                new File(Thread.currentThread().getClass().getResource("/logo.png").getFile()),
                IOUtils.toByteArray(in2),
                Base64.encodeBase64String(IOUtils.toByteArray(in3)),
                "file",
                "0016004",
                "0016005",
                "0016006",
                "陈孝杰",
                "xiaojie.chen"
        );
        System.out.println(response.getData());
    }

}