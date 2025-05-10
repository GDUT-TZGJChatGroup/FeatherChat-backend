package com.wjz.webserver.controller;

import com.wjz.webserver.context.BaseContext;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.domian.entity.ChatMessage;
import com.wjz.webserver.mapper.UserMapper;
import com.wjz.webserver.service.ChatService;
import com.wjz.webserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 拉取与某个好友的聊天记录
     * @param friendAccount 好友账号
     * @return 聊天记录
     */
    @GetMapping("/history")
    public Result<List<ChatMessage>> getChatHistory(@RequestParam String friendAccount) {
        // 获取当前用户账号
        String currentAccount = BaseContext.getCurrentAcc();
        User currentUser = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("useraccount", currentAccount));
        User friendUser = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("useraccount", friendAccount));
        if (currentUser == null || friendUser == null) {
            return Result.error("用户不存在");
        }
        List<ChatMessage> history = chatService.getChatHistory(currentUser.getId(), friendUser.getId());
        return Result.ResultOk(history);
    }
}