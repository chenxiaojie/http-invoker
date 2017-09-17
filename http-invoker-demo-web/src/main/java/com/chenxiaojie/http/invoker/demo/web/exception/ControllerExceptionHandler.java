package com.chenxiaojie.http.invoker.demo.web.exception;

import com.chenxiaojie.http.invoker.demo.web.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptions(Exception exception, WebRequest request) {

        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(exception), "Broken pipe")) {
            //socket is closed, cannot return any response
            return null;
        }

        if (exception instanceof SocketException) {
            log.warn("ControllerExceptionHandler: " + exception.getClass() + ",msg: " + exception.getMessage());
        } else {
            log.error("ControllerExceptionHandler", exception);
        }

        HttpStatus httpStatus = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        Object message = null;

        if (exception instanceof HttpStatusCodeException) {
            httpStatus = ((HttpStatusCodeException) exception).getStatusCode();
        } else {
            headers.add("Content-Type", "application/json;charset=UTF-8");
            String tip = exception.getMessage();
            if (StringUtils.isEmpty(tip)) {
                tip = "服务器不给力~~" + exception.getClass().getName();
            } else {
                if (exception instanceof RuntimeException == false) {
                    tip = "服务器不给力~~" + tip;
                }
            }
            message = Response.fail(tip);
        }
        return handleExceptionInternal(exception, message, headers, httpStatus, request);
    }
}