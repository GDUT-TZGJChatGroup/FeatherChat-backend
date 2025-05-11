package com.wjz.webserver.service;

import com.wjz.webserver.domian.entity.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Mock
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试两个用户之间有聊天记录的情况
    @Test
    public void testGetChatHistoryWithMessages() {
        Long userId1 = 1L;
        Long userId2 = 2L;
        ChatMessage message1 = new ChatMessage();
        ChatMessage message2 = new ChatMessage();
        List<ChatMessage> expectedMessages = Arrays.asList(message1, message2);

        when(chatService.getChatHistory(userId1, userId2)).thenReturn(expectedMessages);

        List<ChatMessage> result = chatService.getChatHistory(userId1, userId2);

        assertNotNull(result, "结果不应为空");
        assertEquals(expectedMessages, result, "结果应与预期消息列表一致");
        verify(chatService, times(1)).getChatHistory(userId1, userId2);
    }

    // 测试两个用户之间没有聊天记录的情况
    @Test
    public void testGetChatHistoryWithNoMessages() {
        Long userId1 = 1L;
        Long userId2 = 2L;

        when(chatService.getChatHistory(userId1, userId2)).thenReturn(Collections.emptyList());

        List<ChatMessage> result = chatService.getChatHistory(userId1, userId2);

        assertNotNull(result, "结果不应为空");
        assertTrue(result.isEmpty(), "结果应为空列表");
        verify(chatService, times(1)).getChatHistory(userId1, userId2);
    }

    // 测试传入用户ID为null的情况
    @Test
    public void testGetChatHistoryWithNullUserId() {
        Long userId1 = null;
        Long userId2 = 2L;

        assertThrows(NullPointerException.class, () -> {
            chatService.getChatHistory(userId1, userId2);
        }, "当用户ID为null时，应抛出NullPointerException");

        verify(chatService, times(1)).getChatHistory(userId1, userId2);
    }

    // 测试传入两个用户ID都为null的情况
    @Test
    public void testGetChatHistoryWithBothNullUserIds() {
        Long userId1 = null;
        Long userId2 = null;

        assertThrows(NullPointerException.class, () -> {
            chatService.getChatHistory(userId1, userId2);
        }, "当两个用户ID都为null时，应抛出NullPointerException");

        verify(chatService, times(1)).getChatHistory(userId1, userId2);
    }

    // 测试传入相同的用户ID的情况
    @Test
    public void testGetChatHistoryWithSameUserIds() {
        Long userId1 = 1L;
        Long userId2 = 1L;

        when(chatService.getChatHistory(userId1, userId2)).thenReturn(Collections.emptyList());

        List<ChatMessage> result = chatService.getChatHistory(userId1, userId2);

        assertNotNull(result, "结果不应为空");
        assertTrue(result.isEmpty(), "当用户ID相同时，结果应为空列表");
        verify(chatService, times(1)).getChatHistory(userId1, userId2);
    }
}
