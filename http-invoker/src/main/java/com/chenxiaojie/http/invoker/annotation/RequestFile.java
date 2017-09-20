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
     * @return 文件名, 必填
     */
    String value() default "attachment.png";

    /**
     * @return 上传时的表单名, 选填
     */
    String inputName() default "file";
}
