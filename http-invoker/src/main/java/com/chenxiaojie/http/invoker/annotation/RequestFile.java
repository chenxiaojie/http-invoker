package com.chenxiaojie.http.invoker.annotation;

import java.lang.annotation.*;

/**
 * Created by chenxiaojie on 16/3/30.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestFile {

    /**
     * 文件名,必填
     *
     * @return
     */
    String value() default "attachment.png";

    /**
     * 上传时的表单名,选填
     *
     * @return
     */
    String inputName() default "file";
}
