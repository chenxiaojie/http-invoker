package com.chenxiaojie.http.invoker.test.http.api;

import com.chenxiaojie.http.invoker.annotation.PathVariable;
import com.chenxiaojie.http.invoker.annotation.RequestBody;
import com.chenxiaojie.http.invoker.annotation.RequestMapping;
import com.chenxiaojie.http.invoker.annotation.RequestParam;
import com.chenxiaojie.http.invoker.entity.HttpResult;
import com.chenxiaojie.http.invoker.enums.HttpMethod;
import com.chenxiaojie.http.invoker.test.vo.Response;
import com.chenxiaojie.http.invoker.test.vo.UserLoginModel;

/**
 * Created by chenxiaojie on 2017/9/10.
 */
public interface SimpleHttpApi {

    @RequestMapping("/simple/{loginId}")
    Response<UserLoginModel> queryByLoginId(@PathVariable("loginId") int loginId);

    @RequestMapping("/simple/{loginId}")
    HttpResult<Response<UserLoginModel>> queryHttpResultByLoginId(@PathVariable("loginId") int loginId);

    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.GET)
    Response<UserLoginModel> getByLoginId(@PathVariable("loginId") int loginId,
                                          @RequestParam(value = "employeeId") String employeeId,
                                          @RequestParam(value = "employeeName") String employeeName,
                                          @RequestParam(value = "ad") String ad);


    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.POST)
    Response<UserLoginModel> addByLoginId(@PathVariable("loginId") int loginId,
                                          @RequestParam(value = "employeeId") String employeeId,
                                          @RequestParam(value = "employeeName") String employeeName,
                                          @RequestParam(value = "ad") String ad);


    @RequestMapping(value = "/simple", method = HttpMethod.POST)
    Response<UserLoginModel> addUser(UserLoginModel userLoginModel);

    @RequestMapping(value = "/simple", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> addUser2(@RequestBody UserLoginModel userLoginModel);

    @RequestMapping(value = "/simple", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> addUser3(String userLoginModel);

    @RequestMapping(value = "/simple", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> addUser4(@RequestBody String userLoginModel);

    @RequestMapping(value = "/{path}", method = HttpMethod.POST)
    HttpResult<Response<UserLoginModel>> addUser5(@RequestBody UserLoginModel userLoginModel,
                                                  @PathVariable("path") String path,
                                                  @RequestParam(value = "employeeId") String employeeId,
                                                  @RequestParam(value = "employeeId") String employeeId2,
                                                  @RequestParam(value = "employeeId") String employeeId3,
                                                  @RequestParam(value = "employeeName") String employeeName,
                                                  @RequestParam(value = "ad") String ad);

    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.PUT)
    HttpResult<Response<UserLoginModel>> updateUser(@RequestBody UserLoginModel userLoginModel,
                                                    @PathVariable("loginId") int loginId,
                                                    @RequestParam(value = "employeeId") String employeeId,
                                                    @RequestParam(value = "employeeId") String employeeId2,
                                                    @RequestParam(value = "employeeId") String employeeId3,
                                                    @RequestParam(value = "employeeName") String employeeName,
                                                    @RequestParam(value = "ad") String ad);

    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.PATCH)
    HttpResult<Response<UserLoginModel>> patchUser(@RequestBody UserLoginModel userLoginModel,
                                                   @PathVariable("loginId") int loginId,
                                                   @RequestParam(value = "employeeId") String employeeId,
                                                   @RequestParam(value = "employeeId") String employeeId2,
                                                   @RequestParam(value = "employeeId") String employeeId3,
                                                   @RequestParam(value = "employeeName") String employeeName,
                                                   @RequestParam(value = "ad") String ad);


    @RequestMapping(value = "/simple/{loginId}", method = HttpMethod.DELETE)
    HttpResult<String> deleteUser(@RequestBody UserLoginModel userLoginModel,
                                  @PathVariable("loginId") int loginId,
                                  @RequestParam(value = "employeeId") String employeeId,
                                  @RequestParam(value = "employeeId") String employeeId2,
                                  @RequestParam(value = "employeeId") String employeeId3,
                                  @RequestParam(value = "employeeName") String employeeName,
                                  @RequestParam(value = "ad") String ad);


}
