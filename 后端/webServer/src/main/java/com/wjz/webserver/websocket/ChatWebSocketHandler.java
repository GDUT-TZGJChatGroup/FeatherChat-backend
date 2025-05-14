package com.wjz.webserver.websocket;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjz.webserver.config.JacksonObjectMapper;
import com.wjz.webserver.config.JwtConfig;
import com.wjz.webserver.context.BaseContext;
import com.wjz.webserver.domian.entity.ChatMessage;
import com.wjz.webserver.domian.entity.Friendship;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.mapper.FriendMapper;
import com.wjz.webserver.mapper.UserMapper;
import com.wjz.webserver.utils.JwtUtil;
import com.wjz.webserver.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 用户ID与WebSocketSession的映射
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtConfig jwtConfig;

    private ObjectMapper objectMapper = new JacksonObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getToken(session);
        if (token == null) {
            session.close();
            return;
        }
        String userAccount = JwtUtil.getUserAccountFromToken(jwtConfig.getSecretKey(), token);
        if (userAccount == null) {
            session.close();
            return;
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", userAccount));
        if (user != null) {
            sessions.put(user.getId(), session);
            session.getAttributes().put("userId", user.getId());
        } else {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        // 校验是否为好友
        Long fromUserId = chatMessage.getFromUserId();
        Long toUserId = chatMessage.getToUserId();
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Friendship> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.and(w -> w
                .eq("user_id", fromUserId).eq("friend_id", toUserId)
                .or()
                .eq("user_id", toUserId).eq("friend_id", fromUserId)
        ).eq("status", 1);
        Friendship friendship = friendMapper.selectOne(wrapper);
        if (friendship == null) {
            // 不是好友，不能聊天
            session.sendMessage(new TextMessage("对方不是你的好友，无法发送消息"));
            return;
        }

        // 设置时间
        chatMessage.setTime(LocalDateTime.now());

        // 存储到Redis
        String redisKey = getChatKey(fromUserId, toUserId);
        redisCache.setCacheList(redisKey, Collections.singletonList(chatMessage));

        // 推送给对方
        WebSocketSession toSession = sessions.get(toUserId);
        if (toSession != null && toSession.isOpen()) {
            toSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }
        // 推送给自己（回显）
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
        }
    }

    private String getToken(WebSocketSession session) {
        // 你可以通过url参数或header获取token
        // 这里只是示例，具体实现看你的前端
        String uri = session.getUri().toString();
        // ws://host/ws/chat?token=xxx
        if (uri.contains("token=")) {
            return uri.substring(uri.indexOf("token=") + 6);
        }
        return null;
    }

    private String getChatKey(Long userId1, Long userId2) {
        // 保证key唯一且顺序无关
        return "chat:" + (userId1 < userId2 ? userId1 + ":" + userId2 : userId2 + ":" + userId1);
    }
}