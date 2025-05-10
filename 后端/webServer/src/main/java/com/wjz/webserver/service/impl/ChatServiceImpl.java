package com.wjz.webserver.service.impl;

import com.wjz.webserver.domian.entity.ChatMessage;
import com.wjz.webserver.service.ChatService;
import com.wjz.webserver.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<ChatMessage> getChatHistory(Long userId1, Long userId2) {
        String key = "chat:" + (userId1 < userId2 ? userId1 + ":" + userId2 : userId2 + ":" + userId1);
        return redisCache.getCacheList(key);
    }
}