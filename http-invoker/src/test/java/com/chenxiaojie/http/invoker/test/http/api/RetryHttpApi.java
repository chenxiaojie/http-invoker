package com.chenxiaojie.http.invoker.test.http.api;

import com.chenxiaojie.http.invoker.annotation.PathVariable;
import com.chenxiaojie.http.invoker.annotation.RequestMapping;
import com.chenxiaojie.http.invoker.annotation.RequestParam;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.test.vo.Response;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;

/**
 * Created by chenxiaojie on 2017/9/10.
 */
public interface RetryHttpApi {

    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET, retryTimes = 1)
    HttpResult<Response<UserLoginModel>> getByLoginId(@PathVariable("loginId") String loginId,
                                                      @RequestParam(value = "employeeId") String employeeId,
                                                      @RequestParam(value = "employeeName") String employeeName,
                                                      @RequestParam(value = "ad") String ad);
}
