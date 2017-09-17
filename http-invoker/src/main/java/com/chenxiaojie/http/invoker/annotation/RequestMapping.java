
package com.chenxiaojie.http.invoker.annotation;

import com.chenxiaojie.http.invoker.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * 请求路径
     *
     * @return
     */
    String value() default "";

    /**
     * 请求方法
     *
     * @return
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * 支持msg.user.id/msg.user[1].id 表达式解析返回值
     *
     * @return
     */
    String resultJsonPath() default "";

    /**
     * 重试次数,默认不重试,当为1时,重试一次,共请求两次
     *
     * @return
     */
    int retryTimes() default 0;


    /**
     * 默认编码
     *
     * @return
     */
    String charset() default "UTF-8";
}
