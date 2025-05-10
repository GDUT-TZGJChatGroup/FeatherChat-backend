package com.wjz.webserver.controller;


import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.service.EmailService;
import com.wjz.webserver.service.impl.EmailServiceImpl;
import com.wjz.webserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mail")
public class EmailController {

    @Autowired
    EmailService emailService = new EmailServiceImpl();
    @PostMapping
    public Result SendMail(@RequestBody ToEmail toEmail){

        return emailService.commonEmail(toEmail);
    }
}
