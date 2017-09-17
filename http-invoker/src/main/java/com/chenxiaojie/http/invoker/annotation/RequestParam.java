package com.chenxiaojie.http.invoker.annotation;

import java.lang.annotation.*;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    /**
     * 参数名
     *
     * @return
     */
    String value() default "";

    /**
     * 当参数为空时,默认填写值
     *
     * @return
     */
    String defaultValue() default "";
}
