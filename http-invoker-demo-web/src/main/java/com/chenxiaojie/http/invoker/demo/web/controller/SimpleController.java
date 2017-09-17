package com.chenxiaojie.http.invoker.demo.web.controller;

import com.chenxiaojie.http.invoker.demo.web.vo.Response;
import com.chenxiaojie.http.invoker.demo.web.vo.UserLoginModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class SimpleController {

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.GET)
    @ResponseBody
    public Response<UserLoginModel> userGet(
            HttpServletRequest request,
            @PathVariable(value = "loginId") int loginId,
            @RequestParam(value = "employeeId", required = false, defaultValue = "") String employeeId,
            @RequestParam(value = "employeeName", required = false, defaultValue = "") String employeeName,
            @RequestParam(value = "ad", required = false, defaultValue = "") String ad) {

        if (!request.getMethod().equalsIgnoreCase(RequestMethod.GET.name())) {
            log.warn("method not equals");
        }

        return Response.success(UserLoginModel.builder().
                loginId(loginId)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .ad(ad)
                .build());
    }

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserLoginModel> userPost(
            HttpServletRequest request,
            @PathVariable(value = "loginId") int loginId,
            @RequestParam(value = "employeeId", required = false, defaultValue = "") String employeeId,
            @RequestParam(value = "employeeName", required = false, defaultValue = "") String employeeName,
            @RequestParam(value = "ad", required = false, defaultValue = "") String ad) {

        if (!request.getMethod().equalsIgnoreCase(RequestMethod.POST.name())) {
            log.warn("method not equals");
        }

        return Response.success(UserLoginModel.builder().
                loginId(loginId)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .ad(ad)
                .build());
    }


    @RequestMapping(value = "/simple", method = {RequestMethod.POST})
    @ResponseBody
    public Response<UserLoginModel> userPost(HttpServletRequest request, @RequestBody UserLoginModel userLoginModel) {

        if (!request.getMethod().equalsIgnoreCase(RequestMethod.POST.name())) {
            log.warn("method not equals");
        }

        return Response.success(userLoginModel);
    }

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.PUT)
    @ResponseBody
    public Response<UserLoginModel> userPut(HttpServletRequest request, @PathVariable(value = "loginId") int loginId, @RequestBody UserLoginModel userLoginModel) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.PUT.name())) {
            log.warn("method not equals");
        }

        userLoginModel.setLoginId(loginId);
        return Response.success(userLoginModel);
    }


    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.PATCH)
    @ResponseBody
    public Response<UserLoginModel> userPatch(HttpServletRequest request, @PathVariable(value = "loginId") int loginId, @RequestBody UserLoginModel userLoginModel) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.PATCH.name())) {
            log.warn("method not equals");
        }

        userLoginModel.setLoginId(loginId);
        return Response.success(userLoginModel);
    }


    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Response userDelete(HttpServletRequest request, @PathVariable(value = "loginId") int loginId) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.DELETE.name())) {
            log.warn("method not equals");
        }

        return Response.success(request.getMethod() + loginId);
    }

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.HEAD)
    @ResponseBody
    public Response userHead(HttpServletRequest request, @PathVariable(value = "loginId") int loginId) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.HEAD.name())) {
            log.warn("method not equals");
        }

        return Response.success(request.getMethod() + loginId);
    }

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.OPTIONS)
    @ResponseBody
    public Response userOptions(HttpServletRequest request, @PathVariable(value = "loginId") int loginId) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())) {
            log.warn("method not equals");
        }

        return Response.success(request.getMethod() + loginId);
    }

    @RequestMapping(value = "/simple/{loginId}", method = RequestMethod.TRACE)
    @ResponseBody
    public Response userTrace(HttpServletRequest request, @PathVariable(value = "loginId") int loginId) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.TRACE.name())) {
            log.warn("method not equals");
        }

        return Response.success(request.getMethod() + loginId);
    }

    @RequestMapping(value = "/simple/list", method = RequestMethod.GET)
    @ResponseBody
    public Response userTrace(HttpServletRequest request) {
        if (!request.getMethod().equalsIgnoreCase(RequestMethod.GET.name())) {
            log.warn("method not equals");
        }

        UserLoginModel userLoginModel1 = new UserLoginModel();
        userLoginModel1.setLoginId(1);
        userLoginModel1.setEmployeeId("0016004_1");
        userLoginModel1.setEmployeeName("陈孝杰_1");
        userLoginModel1.setAd("xiaojie.chen_1");

        UserLoginModel userLoginModel2 = new UserLoginModel();
        userLoginModel2.setLoginId(2);
        userLoginModel2.setEmployeeId("0016004_2");
        userLoginModel2.setEmployeeName("陈孝杰_2");
        userLoginModel2.setAd("xiaojie.chen_2");

        UserLoginModel userLoginModel3 = new UserLoginModel();
        userLoginModel3.setLoginId(3);
        userLoginModel3.setEmployeeId("0016004_3");
        userLoginModel3.setEmployeeName("陈孝杰_3");
        userLoginModel3.setAd("xiaojie.chen_3");

        List<UserLoginModel> list = Lists.newArrayList();
        list.add(userLoginModel1);
        list.add(userLoginModel2);
        list.add(userLoginModel3);

        return Response.success(list);
    }

}
