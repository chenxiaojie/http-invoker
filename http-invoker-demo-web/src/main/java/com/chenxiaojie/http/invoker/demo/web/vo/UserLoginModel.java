package com.chenxiaojie.http.invoker.demo.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by chenxiaojie on 15/8/30.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginModel {
    private int loginId;
    private String employeeId;
    private String employeeName;
    private String ad;
}