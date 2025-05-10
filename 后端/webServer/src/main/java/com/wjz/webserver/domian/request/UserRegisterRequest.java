package com.wjz.webserver.domian.request;

import lombok.Data;


@Data
public class UserRegisterRequest {
    // 用户名
    private String userAccount;
    // 密码
    private String userPassword;
    // 确认密码
    private String checkPassword;
    // 验证码
    private String code;
    // 邮箱
    private String email;
}
