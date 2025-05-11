package com.wjz.webserver.controller;

import com.wjz.webserver.domian.entity.vo.UserVo;
import com.wjz.webserver.domian.request.UserRegisterRequest;
import com.wjz.webserver.service.UserService;
import com.wjz.webserver.utils.RedisCache;
import com.wjz.webserver.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserControllerTest {

    @Mock
    private RedisCache redisCache;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserRegister_正常路径() {
        // 创建请求对象
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@example.com");
        request.setCode("123456");
        request.setUserAccount("testUser");

        // 模拟验证码匹配和注册成功
        when(redisCache.getCacheObject("test@example.com")).thenReturn("123456");
        when(userService.userRegister(request)).thenReturn(Result.ResultOk());

        // 调用控制器方法
        Result result = userController.userRegister(request);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    public void testUserRegister_验证码错误() {
        // 创建请求对象
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@example.com");
        request.setCode("654321");
        request.setUserAccount("testUser");

        // 模拟验证码不匹配
        when(redisCache.getCacheObject("test@example.com")).thenReturn("123456");

        // 调用控制器方法
        Result result = userController.userRegister(request);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("验证码错误", result.getMsg());
    }

    @Test
    public void testUserRegister_请求对象为空() {
        // 调用控制器方法
        Result result = userController.userRegister(null);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("参数不能为空", result.getMsg());
    }

    @Test
    public void testUserLogin_正常路径() {
        // 创建请求对象
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@example.com");
        request.setUserPassword("password");

        // 模拟登录成功
        when(userService.login(request)).thenReturn(Result.ResultOk());

        // 调用控制器方法
        Result result = userController.userLogin(request);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    public void testUserLogin_请求对象为空() {
        // 调用控制器方法
        Result result = userController.userLogin(null);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("参数不能为空", result.getMsg());
    }

    @Test
    public void testFriendRequest_正常路径() {
        // 调用控制器方法
        Result result = userController.friendRequest("testAccName");

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    public void testFriendRequest_异常路径() {
        // 模拟好友请求失败
        when(userService.friendRequest("testAccName")).thenReturn(Result.error("好友请求失败"));

        // 调用控制器方法
        Result result = userController.friendRequest("testAccName");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("好友请求失败", result.getMsg());
    }

    @Test
    public void testFriendList_正常路径() {
        // 创建返回对象
        UserVo userVo1 = new UserVo();
        userVo1.setUsername("friend1");
        UserVo userVo2 = new UserVo();
        userVo2.setUsername("friend2");
        List<UserVo> friends = Arrays.asList(userVo1, userVo2);

        // 模拟获取好友列表成功
        when(userService.friendList()).thenReturn(Result.ResultOk(friends));

        // 调用控制器方法
        Result<List<UserVo>> result = userController.friendList();

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
        assertEquals(2, result.getData().size());
    }

    @Test
    public void testFriendList_异常路径() {
        // 模拟获取好友列表失败
        when(userService.friendList()).thenReturn(Result.error());

        // 调用控制器方法
        Result<List<UserVo>> result = userController.friendList();

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("获取好友列表失败", result.getMsg());
    }

    @Test
    public void testFriendRequestList_正常路径() {
        // 创建返回对象
        UserVo userVo1 = new UserVo();
        userVo1.setUsername("requester1");
        UserVo userVo2 = new UserVo();
        userVo2.setUsername("requester2");
        List<UserVo> requesters = Arrays.asList(userVo1, userVo2);

        // 模拟获取好友请求列表成功
        when(userService.friendRequestList()).thenReturn(Result.ResultOk(requesters));

        // 调用控制器方法
        Result<List<UserVo>> result = userController.friendRequestList();

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
        assertEquals(2, result.getData().size());
    }

    @Test
    public void testFriendRequestList_异常路径() {
        // 模拟获取好友请求列表失败
        when(userService.friendRequestList()).thenReturn(Result.error("获取好友请求列表失败"));

        // 调用控制器方法
        Result<List<UserVo>> result = userController.friendRequestList();

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("获取好友请求列表失败", result.getMsg());
    }

    @Test
    public void testFriendAgree_正常路径() {
        // 调用控制器方法
        Result result = userController.friendAgree("testAccName");

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    public void testFriendAgree_异常路径() {
        // 模拟同意好友请求失败
        when(userService.friendAgree("testAccName")).thenReturn(Result.error("同意好友请求失败"));

        // 调用控制器方法
        Result result = userController.friendAgree("testAccName");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("同意好友请求失败", result.getMsg());
    }

    @Test
    public void testFriendDelete_正常路径() {
        // 调用控制器方法
        Result result = userController.friendDelete("testAccName");

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("操作成功", result.getMsg());
    }

    @Test
    public void testFriendDelete_异常路径() {
        // 模拟删除好友失败
        when(userService.friendDelete("testAccName")).thenReturn(Result.error("删除好友失败"));

        // 调用控制器方法
        Result result = userController.friendDelete("testAccName");

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("删除好友失败", result.getMsg());
    }
}
