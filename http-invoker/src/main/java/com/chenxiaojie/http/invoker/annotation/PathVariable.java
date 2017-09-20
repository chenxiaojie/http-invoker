package com.chenxiaojie.http.invoker.annotation;

import java.lang.annotation.*;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    /**
     * @return 路径上参数的key
     */
    String value() default "";

    /**
     * @return 当入参为空时, 默认填写值
     */
    String defaultValue() default "";
}
