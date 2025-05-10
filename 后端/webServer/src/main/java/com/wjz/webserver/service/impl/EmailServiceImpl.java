package com.wjz.webserver.service.impl;


import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.service.EmailService;
import com.wjz.webserver.service.UserService;
import com.wjz.webserver.utils.RedisCache;
import com.wjz.webserver.utils.Result;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;

    public Result commonEmail(ToEmail toEmail) {
        if (userService.isEmailNull(toEmail)) {

            toEmail.setSubject("注册验证码");
            // RandomStringUtils  commons-lang3引进的工具类
            String yzm = RandomStringUtils.randomNumeric(6);
            toEmail.setContent(yzm);
            redisCache.setCacheObject(toEmail.getTo(),yzm);
            // 验证码6分钟内有效
            redisCache.expire(toEmail.getTo(),60*10);

            //创建简单邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            //谁发的
            message.setFrom(from);
            //谁要接收
            message.setTo(toEmail.getTo());
            //邮件标题
            message.setSubject(toEmail.getSubject());
            //邮件内容
            message.setText(toEmail.getContent());
            try {
                mailSender.send(message);

                return Result.ResultOk(toEmail.getTo());
            } catch (MailException e) {
                e.printStackTrace();
                return Result.error("发送失败");
            }
        }
        return Result.error("该邮箱已注册");
    }
}
