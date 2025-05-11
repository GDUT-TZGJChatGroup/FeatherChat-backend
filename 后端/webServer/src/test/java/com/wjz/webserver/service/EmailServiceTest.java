package com.wjz.webserver.service;

import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.utils.Result;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceTest implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceTest(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Result commonEmail(ToEmail toEmail) {
        // 验证邮箱格式
        if (toEmail.toString() == ("")  || toEmail.toString().trim().isEmpty()) {
            return Result.error("邮箱地址不能为空");
        }
        if (!toEmail.toString().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
            return Result.error("邮箱地址格式不正确");
        }

        try {
            // 发送邮件
            mailSender.send(mimeMessage -> {
                mimeMessage.setSubject(toEmail.getSubject());
                mimeMessage.setText(toEmail.getContent());
                mimeMessage.setRecipients(MimeMessage.RecipientType.TO, toEmail.toString());
            });
            return Result.ResultOk("邮件发送成功");
        } catch (Exception e) {
            return Result.error("邮件发送失败");
        }
    }
}
