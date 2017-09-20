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
     * @return 参数名
     */
    String value() default "";

    /**
     * @return 当参数为空时, 默认填写值
     */
    String defaultValue() default "";
}
