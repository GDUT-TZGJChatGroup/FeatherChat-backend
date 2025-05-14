package com.wjz.webserver.domian.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FriendshipTest {

    @Test
    // 测试正常路径
    public void test正常创建好友关系() {
        Long userId = 1L;
        Long friendId = 2L;
        Integer status = 1;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertEquals(status, friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试用户ID为空的情况
    public void test用户ID为空() {
        Long friendId = 2L;
        Integer status = 1;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertNull(friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertEquals(status, friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试好友ID为空的情况
    public void test好友ID为空() {
        Long userId = 1L;
        Integer status = 1;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertNull(friendship.getFriendId());
        assertEquals(status, friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试状态为空的情况
    public void test状态为空() {
        Long userId = 1L;
        Long friendId = 2L;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertNull(friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试创建时间为null的情况
    public void test创建时间为Null() {
        Long userId = 1L;
        Long friendId = 2L;
        Integer status = 1;
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertEquals(status, friendship.getStatus());
        assertNull(friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试更新时间为null的情况
    public void test更新时间为Null() {
        Long userId = 1L;
        Long friendId = 2L;
        Integer status = 1;
        LocalDateTime createdTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertEquals(status, friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertNull(friendship.getUpdatedTime());
    }

    @Test
    // 测试状态为非预期值的情况
    public void test状态为非预期值() {
        Long userId = 1L;
        Long friendId = 2L;
        Integer status = 3; // 假设3是一个非预期的状态值
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(friendId, friendship.getFriendId());
        assertEquals(status, friendship.getStatus()); // 即使是非预期值也会被设置
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }

    @Test
    // 测试用户ID和好友ID相同的情况
    public void test用户ID和好友ID相同() {
        Long userId = 1L;
        Integer status = 1;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        Friendship friendship = new Friendship();

        assertEquals(userId, friendship.getUserId());
        assertEquals(userId, friendship.getFriendId()); // 用户ID和好友ID可以相同，但实际应用中可能需要额外的逻辑处理这种情况
        assertEquals(status, friendship.getStatus());
        assertEquals(createdTime, friendship.getCreatedTime());
        assertEquals(updatedTime, friendship.getUpdatedTime());
    }
}
