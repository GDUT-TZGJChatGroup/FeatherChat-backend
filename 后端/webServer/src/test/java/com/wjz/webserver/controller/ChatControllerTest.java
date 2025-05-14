package com.wjz.webserver.controller;

import com.wjz.webserver.context.BaseContext;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.domian.entity.ChatMessage;
import com.wjz.webserver.mapper.UserMapper;
import com.wjz.webserver.service.ChatService;
import com.wjz.webserver.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChatHistory_正常情况() {
        // 准备数据
        String currentAccount = "user1";
        String friendAccount = "user2";
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUseraccount(currentAccount);
        User friendUser = new User();
        friendUser.setId(2L);
        friendUser.setUseraccount(friendAccount);
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage());

        // 模拟行为
        when(BaseContext.getCurrentAcc()).thenReturn(currentAccount);
        when(userMapper.selectOne(any())).thenReturn(currentUser).thenReturn(friendUser);
        when(chatService.getChatHistory(currentUser.getId(), friendUser.getId())).thenReturn(chatMessages);

        // 执行测试
        Result<List<ChatMessage>> result = chatController.getChatHistory(friendAccount);

        // 验证结果
        assertEquals(Result.ResultOk(chatMessages), result);
        verify(userMapper, times(2)).selectOne(any());
        verify(chatService, times(1)).getChatHistory(currentUser.getId(), friendUser.getId());
    }

    @Test
    void testGetChatHistory_当前用户不存在() {
        // 准备数据
        String currentAccount = "user1";
        String friendAccount = "user2";

        // 模拟行为
        when(BaseContext.getCurrentAcc()).thenReturn(currentAccount);
        when(userMapper.selectOne(any())).thenReturn(null);

        // 执行测试
        Result<List<ChatMessage>> result = chatController.getChatHistory(friendAccount);

        // 验证结果
        assertEquals(Result.error("用户不存在"), result);
        verify(userMapper, times(1)).selectOne(any());
        verify(chatService, times(0)).getChatHistory(anyLong(), anyLong());
    }

    @Test
    void testGetChatHistory_好友用户不存在() {
        // 准备数据
        String currentAccount = "user1";
        String friendAccount = "user2";
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUseraccount(currentAccount);

        // 模拟行为
        when(BaseContext.getCurrentAcc()).thenReturn(currentAccount);
        when(userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("useraccount", currentAccount))).thenReturn(currentUser);
        when(userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("useraccount", friendAccount))).thenReturn(null);

        // 执行测试
        Result<List<ChatMessage>> result = chatController.getChatHistory(friendAccount);

        // 验证结果
        assertEquals(Result.error("用户不存在"), result);
        verify(userMapper, times(2)).selectOne(any());
        verify(chatService, times(0)).getChatHistory(anyLong(), anyLong());
    }

    @Test
    void testGetChatHistory_两者用户都不存在() {
        // 准备数据
        String currentAccount = "user1";
        String friendAccount = "user2";

        // 模拟行为
        when(BaseContext.getCurrentAcc()).thenReturn(currentAccount);
        when(userMapper.selectOne(any())).thenReturn(null);

        // 执行测试
        Result<List<ChatMessage>> result = chatController.getChatHistory(friendAccount);

        // 验证结果
        assertEquals(Result.error("用户不存在"), result);
        verify(userMapper, times(2)).selectOne(any());
        verify(chatService, times(0)).getChatHistory(anyLong(), anyLong());
    }

    @Test
    void testGetChatHistory_聊天记录为空() {
        // 准备数据
        String currentAccount = "user1";
        String friendAccount = "user2";
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUseraccount(currentAccount);
        User friendUser = new User();
        friendUser.setId(2L);
        friendUser.setUseraccount(friendAccount);
        List<ChatMessage> chatMessages = new ArrayList<>();

        // 模拟行为
        when(BaseContext.getCurrentAcc()).thenReturn(currentAccount);
        when(userMapper.selectOne(any())).thenReturn(currentUser).thenReturn(friendUser);
        when(chatService.getChatHistory(currentUser.getId(), friendUser.getId())).thenReturn(chatMessages);

        // 执行测试
        Result<List<ChatMessage>> result = chatController.getChatHistory(friendAccount);

        // 验证结果
        assertEquals(Result.ResultOk(chatMessages), result);
        verify(userMapper, times(2)).selectOne(any());
        verify(chatService, times(1)).getChatHistory(currentUser.getId(), friendUser.getId());
    }
}
