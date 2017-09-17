package com.chenxiaojie.http.invoker.demo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class RedirectController {

    @RequestMapping(value = "/redirect/loop", method = RequestMethod.GET)
    public String loop(HttpServletRequest request) {
        log.info("/redirect/loop");
        return "redirect:/httpinvoker/redirect/loop";
    }

}