
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
     * @return 请求路径
     */
    String value() default "";

    /**
     * @return 请求方法
     */
    HttpMethod method() default HttpMethod.GET;

    /**
     * @return 支持msg.user.id/msg.user[1].id 表达式解析返回值
     */
    String resultJsonPath() default "";

    /**
     * @return 重试次数, 默认不重试, 当为1时, 重试一次, 共请求两次
     */
    int retryTimes() default 0;

    /**
     * @return 默认编码
     */
    String charset() default "UTF-8";
}
