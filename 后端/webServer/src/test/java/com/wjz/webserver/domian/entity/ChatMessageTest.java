package com.wjz.webserver.domian.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ChatMessageTest {

    @Test
    public void testHappyPath() {
        // 测试正常情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(1L);
        chatMessage.setToUserId(2L);
        chatMessage.setContent("你好，世界！");
        chatMessage.setTime(LocalDateTime.now());

        assertEquals(1L, chatMessage.getFromUserId());
        assertEquals(2L, chatMessage.getToUserId());
        assertEquals("你好，世界！", chatMessage.getContent());
        assertNotNull(chatMessage.getTime());
    }

    @Test
    public void testEdgeCaseWithNullContent() {
        // 测试内容为空的情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(1L);
        chatMessage.setToUserId(2L);
        chatMessage.setContent(null);
        chatMessage.setTime(LocalDateTime.now());

        assertEquals(1L, chatMessage.getFromUserId());
        assertEquals(2L, chatMessage.getToUserId());
        assertNull(chatMessage.getContent());
        assertNotNull(chatMessage.getTime());
    }

    @Test
    public void testEdgeCaseWithEmptyContent() {
        // 测试内容为空字符串的情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(1L);
        chatMessage.setToUserId(2L);
        chatMessage.setContent("");
        chatMessage.setTime(LocalDateTime.now());

        assertEquals(1L, chatMessage.getFromUserId());
        assertEquals(2L, chatMessage.getToUserId());
        assertEquals("", chatMessage.getContent());
        assertNotNull(chatMessage.getTime());
    }

    @Test
    public void testEdgeCaseWithZeroUserIds() {
        // 测试用户ID为0的情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(0L);
        chatMessage.setToUserId(0L);
        chatMessage.setContent("测试消息");
        chatMessage.setTime(LocalDateTime.now());

        assertEquals(0L, chatMessage.getFromUserId());
        assertEquals(0L, chatMessage.getToUserId());
        assertEquals("测试消息", chatMessage.getContent());
        assertNotNull(chatMessage.getTime());
    }

    @Test
    public void testEdgeCaseWithNegativeUserIds() {
        // 测试用户ID为负数的情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(-1L);
        chatMessage.setToUserId(-2L);
        chatMessage.setContent("测试消息");
        chatMessage.setTime(LocalDateTime.now());

        assertEquals(-1L, chatMessage.getFromUserId());
        assertEquals(-2L, chatMessage.getToUserId());
        assertEquals("测试消息", chatMessage.getContent());
        assertNotNull(chatMessage.getTime());
    }

    @Test
    public void testEdgeCaseWithNullTime() {
        // 测试时间为空的情况
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(1L);
        chatMessage.setToUserId(2L);
        chatMessage.setContent("测试消息");
        chatMessage.setTime(null);

        assertEquals(1L, chatMessage.getFromUserId());
        assertEquals(2L, chatMessage.getToUserId());
        assertEquals("测试消息", chatMessage.getContent());
        assertNull(chatMessage.getTime());
    }
}
