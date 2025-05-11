package com.wjz.webserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.domian.entity.vo.UserVo;
import com.wjz.webserver.domian.request.UserRegisterRequest;
import com.wjz.webserver.service.impl.UserServiceImpl;
import com.wjz.webserver.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IService<User> iService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest userRegisterRequest;
    private ToEmail toEmail;

    @BeforeEach
    void setUp() {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test@example.com");
        userRegisterRequest.setUserPassword("password123");
        userRegisterRequest.setUserAccount("testUser");

        toEmail = new ToEmail();
        toEmail.setTo("test@example.com");
    }

    // 测试用户注册成功的情况
    @Test
    void testUserRegister_HappyPath() {
        when(iService.save(new User())).thenReturn(true);
        Result result = userService.userRegister(userRegisterRequest);
        assertTrue(result.isSuccess());
        assertEquals("注册成功", result.getMsg());
    }

    // 测试用户注册失败的情况
    @Test
    void testUserRegister_Failure() {
        when(iService.save(new User())).thenReturn(false);
        Result result = userService.userRegister(userRegisterRequest);
        assertFalse(result.isSuccess());
        assertEquals("注册失败", result.getMsg());
    }

    // 测试邮箱已被注册的情况
    @Test
    void testIsEmailNull_EmailRegistered() {
        when(iService.getOne(new QueryWrapper<User>().eq("email", "test@example.com"))).thenReturn(new User());
        boolean isNull = userService.isEmailNull(toEmail);
        assertFalse(isNull);
    }

    // 测试邮箱未被注册的情况
    @Test
    void testIsEmailNull_EmailNotRegistered() {
        when(iService.getOne(new QueryWrapper<User>().eq("email", "test@example.com"))).thenReturn(null);
        boolean isNull = userService.isEmailNull(toEmail);
        assertTrue(isNull);
    }

    // 测试用户登录成功的情况
    @Test
    void testLogin_HappyPath() {
        User user = new User();
        user.setId(1L);
        when(iService.getOne(new QueryWrapper<User>().eq("accName", "testUser").eq("password", "password123"))).thenReturn(user);
        Result result = userService.login(userRegisterRequest);
        assertTrue(result.isSuccess());
        assertEquals("登录成功", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(user.getId(), ((UserVo) result.getData()).getId());
        assertEquals(user.getUsername(), ((UserVo) result.getData()).getUsername());
    }

    // 测试用户登录失败，用户名不存在的情况
    @Test
    void testLogin_UserNotFound() {
        when(iService.getOne(new QueryWrapper<User>().eq("accName", "testUser").eq("password", "password123"))).thenReturn(null);
        Result result = userService.login(userRegisterRequest);
        assertFalse(result.isSuccess());
        assertEquals("用户名或密码错误", result.getMsg());
    }

    // 测试用户登录失败，密码错误的情况
    @Test
    void testLogin_WrongPassword() {
        User user = new User();
        user.setId(1L);
        when(iService.getOne(new QueryWrapper<User>().eq("accName", "testUser").eq("password", "password123"))).thenReturn(user);
        Result result = userService.login(userRegisterRequest);
        assertFalse(result.isSuccess());
        assertEquals("用户名或密码错误", result.getMsg());
    }

    // 测试添加好友请求成功的情况
    @Test
    void testFriendRequest_HappyPath() {
        when(userService.friendRequest("testUser")).thenReturn(Result.error("添加好友请求成功"));
        Result result = userService.friendRequest("testUser");
        assertTrue(result.isSuccess());
        assertEquals("添加好友请求成功", result.getMsg());
    }

    // 测试添加好友请求失败的情况
    @Test
    void testFriendRequest_Failure() {
        when(userService.friendRequest("testUser")).thenReturn(Result.error("添加好友请求失败"));
        Result result = userService.friendRequest("testUser");
        assertFalse(result.isSuccess());
        assertEquals("添加好友请求失败", result.getMsg());
    }

    // 测试获取好友列表成功的情况
    @Test
    void testFriendList_HappyPath() {
        List<UserVo> userVoList = new ArrayList<>();
        userVoList.add(new UserVo());
        userVoList.add(new UserVo());
        when(userService.friendList()).thenReturn(Result.error());
        Result<List<UserVo>> result = userService.friendList();
        assertTrue(result.isSuccess());
        assertEquals("获取好友列表成功", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    // 测试获取好友列表失败的情况
    @Test
    void testFriendList_Failure() {
        when(userService.friendList()).thenReturn(Result.error("获取好友列表失败"));
        Result<List<UserVo>> result = userService.friendList();
        assertFalse(result.isSuccess());
        assertEquals("获取好友列表失败", result.getMsg());
    }

    // 测试获取好友请求列表成功的情况
    @Test
    void testFriendRequestList_HappyPath() {
        List<UserVo> userVoList = new ArrayList<>();
        userVoList.add(new UserVo());
        userVoList.add(new UserVo());
        when(userService.friendRequestList()).thenReturn(Result.error());
        Result<List<UserVo>> result = userService.friendRequestList();
        assertTrue(result.isSuccess());
        assertEquals("获取好友请求列表成功", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    // 测试获取好友请求列表失败的情况
    @Test
    void testFriendRequestList_Failure() {
        when(userService.friendRequestList()).thenReturn(Result.error("获取好友请求列表失败"));
        Result<List<UserVo>> result = userService.friendRequestList();
        assertFalse(result.isSuccess());
        assertEquals("获取好友请求列表失败", result.getMsg());
    }

    // 测试同意好友请求成功的情况
    @Test
    void testFriendAgree_HappyPath() {
        when(userService.friendRequest("testUser")).thenReturn(Result.error("同意好友请求成功"));
        Result result = userService.friendAgree("testUser");
        assertTrue(result.isSuccess());
        assertEquals("同意好友请求成功", result.getMsg());
    }

    // 测试同意好友请求失败的情况
    @Test
    void testFriendAgree_Failure() {
        when(userService.friendRequest("testUser")).thenReturn(Result.error("同意好友请求失败"));
        Result result = userService.friendAgree("testUser");
        assertFalse(result.isSuccess());
        assertEquals("同意好友请求失败", result.getMsg());
    }

    // 测试删除好友成功的情况
    @Test
    void testFriendDelete_HappyPath() {
        when(userService.friendDelete("testUser")).thenReturn(Result.error("删除好友成功"));
        Result result = userService.friendDelete("testUser");
        assertTrue(result.isSuccess());
        assertEquals("删除好友成功", result.getMsg());
    }

    // 测试删除好友失败的情况
    @Test
    void testFriendDelete_Failure() {
        when(userService.friendDelete("testUser")).thenReturn(Result.error("删除好友失败"));
        Result result = userService.friendDelete("testUser");
        assertFalse(result.isSuccess());
        assertEquals("删除好友失败", result.getMsg());
    }
}
