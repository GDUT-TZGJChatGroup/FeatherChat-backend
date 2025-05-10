package com.wjz.webserver.service;

import com.wjz.webserver.domian.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    List<ChatMessage> getChatHistory(Long userId1, Long userId2);
}