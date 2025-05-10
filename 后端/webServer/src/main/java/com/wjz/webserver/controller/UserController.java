package com.wjz.webserver.controller;


import com.wjz.webserver.domian.entity.vo.UserVo;
import com.wjz.webserver.domian.request.UserRegisterRequest;
import com.wjz.webserver.service.UserService;

import com.wjz.webserver.utils.RedisCache;
import com.wjz.webserver.utils.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@Slf4j
@RestController // 适用于编写restful风格的api，返回值默认为json类型
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest user){
        if (null == user) return Result.error("参数不能为空");
        String yzm = redisCache.getCacheObject(user.getEmail());
        if (user.getCode()!=null && user.getCode().equals(yzm)){
            return userService.userRegister(user);
        }
        return Result.error("验证码错误");
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserRegisterRequest user){
        log.info("登录请求");
        if (null == user) return Result.error("参数不能为空");
        return userService.login(user);

    }

    @PostMapping("/friendRequest/{accName}")
    public Result friendRequest(@PathVariable String accName){
        log.info("好友请求");
        return userService.friendRequest(accName);
    }

    @GetMapping("/friends")
    public Result<List<UserVo>> friendList(){
        log.info("查询好友列表");
        return userService.friendList();
    }

    @GetMapping("/friendsRequestList")
    public Result<List<UserVo>> friendRequestList(){
        log.info("查询好友请求列表");
        return userService.friendRequestList();
    }

    @PutMapping("/friendAgree/{accName}")
    public Result friendAgree(@PathVariable String accName){
        log.info("同意好友请求");
        return userService.friendAgree(accName);
    }

    @DeleteMapping("friendDelete/{accName}")
    public Result friendDelete(@PathVariable String accName){
        log.info("删除好友");
        return userService.friendDelete(accName);
    }


}
