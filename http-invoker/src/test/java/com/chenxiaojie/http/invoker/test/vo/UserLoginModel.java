package com.chenxiaojie.http.invoker.test.vo;

/**
 * Created by chenxiaojie on 15/8/30.
 */
public class UserLoginModel {

    private int loginId;
    private String employeeId;
    private String employeeName;
    private String ad;

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    @Override
    public String toString() {
        return "UserLoginModel{" +
                "loginId=" + loginId +
                ", employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", ad='" + ad + '\'' +
                '}';
    }
}