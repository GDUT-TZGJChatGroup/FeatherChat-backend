package com.wjz.webserver.service;


import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.utils.Result;
import org.springframework.stereotype.Service;


@Service
public interface EmailService {
    /**
     *  发送验证码
     * @param toEmail 邮箱数据传输类
     * @return
     */
    Result commonEmail(ToEmail toEmail);
}
