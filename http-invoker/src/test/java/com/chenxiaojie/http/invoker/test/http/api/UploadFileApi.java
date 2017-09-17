package com.chenxiaojie.http.invoker.test.http.api;

import com.chenxiaojie.http.invoker.annotation.PathVariable;
import com.chenxiaojie.http.invoker.annotation.RequestFile;
import com.chenxiaojie.http.invoker.annotation.RequestMapping;
import com.chenxiaojie.http.invoker.annotation.RequestParam;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.test.vo.Response;

import java.io.File;
import java.io.InputStream;

/**
 * Created by chenxiaojie on 2017/9/10.
 */
public interface UploadFileApi {

    @RequestMapping(value = "/file", method = HttpMethod.POST)
    HttpResult<Response<String>> upload(@RequestFile("u1.png") InputStream in);

    @RequestMapping(value = "/file", method = HttpMethod.POST)
    HttpResult<Response<String>> upload(@RequestFile("u2.png") File file);

    @RequestMapping(value = "/file", method = HttpMethod.POST)
    HttpResult<Response<String>> upload(@RequestFile("u3.png") byte[] fileBytes);

    @RequestMapping(value = "/file", method = HttpMethod.POST)
    HttpResult<Response<String>> upload(@RequestFile("u4.png") String base64);

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


}
