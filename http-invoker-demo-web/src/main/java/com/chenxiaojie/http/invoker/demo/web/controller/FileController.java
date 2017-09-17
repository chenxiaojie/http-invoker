package com.chenxiaojie.http.invoker.demo.web.controller;

import com.alibaba.fastjson.JSON;
import com.chenxiaojie.http.invoker.demo.web.vo.Response;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class FileController {

    @RequestMapping(value = "/file", headers = "content-type=multipart/*", method = RequestMethod.POST)
    @ResponseBody
    public Response filePost(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<MultipartFile> multipartFiles = Lists.newArrayList();
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            for (String name : multipartHttpServletRequest.getMultiFileMap().keySet()) {
                List<MultipartFile> files = multipartHttpServletRequest.getMultiFileMap().get(name);
                for (MultipartFile file : files) {
                    if (file.getSize() > 0) {
                        multipartFiles.add(file);
                    }
                }
            }
            log.info(JSON.toJSONString(multipartHttpServletRequest.getParameterMap()));
        }

        if (CollectionUtils.isNotEmpty(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                try {
                    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(request.getSession().getServletContext().getRealPath("/") + multipartFile.getOriginalFilename()));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return Response.success(request.getParameterMap());
    }

    @RequestMapping(value = "/file", headers = "content-type=multipart/*", method = RequestMethod.PUT)
    @ResponseBody
    public Response filePut(HttpServletRequest request) {
        return filePost(request);
    }

    @RequestMapping(value = "/file", headers = "content-type=multipart/*", method = RequestMethod.PATCH)
    @ResponseBody
    public Response filePatch(HttpServletRequest request) {
        return filePost(request);
    }

}