package com.wjz.webserver.controller;

import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.service.EmailService;
import com.wjz.webserver.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    public void testSendMail_成功路径() throws Exception {
        // 模拟EmailService的commonEmail方法返回成功结果
        ToEmail toEmail = new ToEmail("recipient@example.com", "subject", "content");
        Result expectedResult = Result.ResultOk("邮件发送成功");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"recipient@example.com\",\"subject\":\"subject\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":200,\"msg\":\"邮件发送成功\"}"));
    }

    @Test
    public void testSendMail_空收件人() throws Exception {
        // 模拟EmailService的commonEmail方法返回失败结果
        ToEmail toEmail = new ToEmail("", "subject", "content");
        Result expectedResult = Result.error("收件人地址不能为空");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"\",\"subject\":\"subject\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":400,\"msg\":\"收件人地址不能为空\"}"));
    }

    @Test
    public void testSendMail_空主题() throws Exception {
        // 模拟EmailService的commonEmail方法返回失败结果
        ToEmail toEmail = new ToEmail("recipient@example.com", "", "content");
        Result expectedResult = Result.error("邮件主题不能为空");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"recipient@example.com\",\"subject\":\"\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":400,\"msg\":\"邮件主题不能为空\"}"));
    }

    @Test
    public void testSendMail_空内容() throws Exception {
        // 模拟EmailService的commonEmail方法返回失败结果
        ToEmail toEmail = new ToEmail("recipient@example.com", "subject", "");
        Result expectedResult = Result.error("邮件内容不能为空");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"recipient@example.com\",\"subject\":\"subject\",\"content\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":400,\"msg\":\"邮件内容不能为空\"}"));
    }

    @Test
    public void testSendMail_无效邮箱格式() throws Exception {
        // 模拟EmailService的commonEmail方法返回失败结果
        ToEmail toEmail = new ToEmail("invalid-email", "subject", "content");
        Result expectedResult = Result.error("收件人邮箱格式不正确");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"invalid-email\",\"subject\":\"subject\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":400,\"msg\":\"收件人邮箱格式不正确\"}"));
    }

    @Test
    public void testSendMail_服务异常() throws Exception {
        // 模拟EmailService的commonEmail方法返回失败结果
        ToEmail toEmail = new ToEmail("recipient@example.com", "subject", "content");
        Result expectedResult = Result.error("邮件发送失败，请稍后再试");

        when(emailService.commonEmail(toEmail)).thenReturn(expectedResult);

        // 测试POST请求
        mockMvc.perform(post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipient\":\"recipient@example.com\",\"subject\":\"subject\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":500,\"msg\":\"邮件发送失败，请稍后再试\"}"));
    }
}
