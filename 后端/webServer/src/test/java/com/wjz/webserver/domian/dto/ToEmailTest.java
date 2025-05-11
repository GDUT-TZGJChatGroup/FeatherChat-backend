package com.wjz.webserver.domian.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试ToEmail类的单元测试
 */
public class ToEmailTest {

    /**
     * 测试正常情况下的构造函数和getter方法
     */
    @Test
    public void testNormalConstructorAndGetters() {
        ToEmail toEmail = new ToEmail("example@example.com", "测试邮件", "这是测试邮件的内容");
        assertEquals("example@example.com", toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试无参构造函数和setter方法
     */
    @Test
    public void testNoArgsConstructorAndSetters() {
        ToEmail toEmail = new ToEmail();
        toEmail.setTo("example@example.com");
        toEmail.setSubject("测试邮件");
        toEmail.setContent("这是测试邮件的内容");

        assertEquals("example@example.com", toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试to字段为空的情况
     */
    @Test
    public void testToEmailEmpty() {
        ToEmail toEmail = new ToEmail("", "测试邮件", "这是测试邮件的内容");
        assertTrue(toEmail.getTo().isEmpty());
        assertEquals("测试邮件", toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试subject字段为空的情况
     */
    @Test
    public void testSubjectEmpty() {
        ToEmail toEmail = new ToEmail("example@example.com", "", "这是测试邮件的内容");
        assertEquals("example@example.com", toEmail.getTo());
        assertTrue(toEmail.getSubject().isEmpty());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试content字段为空的情况
     */
    @Test
    public void testContentEmpty() {
        ToEmail toEmail = new ToEmail("example@example.com", "测试邮件", "");
        assertEquals("example@example.com", toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertTrue(toEmail.getContent().isEmpty());
    }

    /**
     * 测试所有字段都为空的情况
     */
    @Test
    public void testAllFieldsEmpty() {
        ToEmail toEmail = new ToEmail("", "", "");
        assertTrue(toEmail.getTo().isEmpty());
        assertTrue(toEmail.getSubject().isEmpty());
        assertTrue(toEmail.getContent().isEmpty());
    }

    /**
     * 测试to字段为null的情况
     */
    @Test
    public void testToEmailNull() {
        ToEmail toEmail = new ToEmail(null, "测试邮件", "这是测试邮件的内容");
        assertNull(toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试subject字段为null的情况
     */
    @Test
    public void testSubjectNull() {
        ToEmail toEmail = new ToEmail("example@example.com", null, "这是测试邮件的内容");
        assertEquals("example@example.com", toEmail.getTo());
        assertNull(toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }

    /**
     * 测试content字段为null的情况
     */
    @Test
    public void testContentNull() {
        ToEmail toEmail = new ToEmail("example@example.com", "测试邮件", null);
        assertEquals("example@example.com", toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertNull(toEmail.getContent());
    }

    /**
     * 测试所有字段都为null的情况
     */
    @Test
    public void testAllFieldsNull() {
        ToEmail toEmail = new ToEmail(null, null, null);
        assertNull(toEmail.getTo());
        assertNull(toEmail.getSubject());
        assertNull(toEmail.getContent());
    }

    /**
     * 测试to字段为超长字符串的情况
     */
    @Test
    public void testToEmailLong() {
        StringBuilder longEmail = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            longEmail.append("a");
        }
        longEmail.append("@example.com");
        ToEmail toEmail = new ToEmail(longEmail.toString(), "测试邮件", "这是测试邮件的内容");
        assertEquals(longEmail.toString(), toEmail.getTo());
        assertEquals("测试邮件", toEmail.getSubject());
        assertEquals("这是测试邮件的内容", toEmail.getContent());
    }
}
