package com.chenxiaojie.http.invoker.test.http.api;

import com.chenxiaojie.http.invoker.annotation.PathVariable;
import com.chenxiaojie.http.invoker.annotation.RequestMapping;
import com.chenxiaojie.http.invoker.annotation.RequestParam;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;

import java.util.List;

/**
 * Created by chenxiaojie on 2017/9/10.
 */
public interface ResultJsonPathHttpApi {

    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data")
    UserLoginModel getByLoginId(@PathVariable("loginId") int loginId,
                                @RequestParam(value = "employeeId") String employeeId,
                                @RequestParam(value = "employeeName") String employeeName,
                                @RequestParam(value = "ad") String ad);


    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data.employeeName", charset = "GBK")
    String getByLoginId2(@PathVariable("loginId") int loginId,
                         @RequestParam(value = "employeeId") String employeeId,
                         @RequestParam(value = "employeeName") String employeeName,
                         @RequestParam(value = "ad") String ad);


    @RequestMapping(value = "/simple/list", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data")
    List<UserLoginModel> queryUsers();

    @RequestMapping(value = "/simple/list", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data[1]")
    UserLoginModel queryUsers2();

    @RequestMapping(value = "/simple/list", method = HttpMethod.GET, retryTimes = 1, resultJsonPath = "data[1].employeeName", charset = "GBK")
    String queryUsers3();
}
