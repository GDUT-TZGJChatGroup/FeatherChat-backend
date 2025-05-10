package com.wjz.webserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjz.webserver.config.JwtConfig;
import com.wjz.webserver.context.BaseContext;
import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.domian.entity.Friendship;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.domian.entity.vo.UserVo;
import com.wjz.webserver.domian.request.UserRegisterRequest;
import com.wjz.webserver.mapper.FriendMapper;
import com.wjz.webserver.mapper.UserMapper;
import com.wjz.webserver.service.UserService;
import com.wjz.webserver.utils.JwtUtil;
import com.wjz.webserver.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service()
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String SALT = "zy";
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Override
    public Result userRegister(UserRegisterRequest user) {
        // 虽然前端也会判断，但是有绕过前端直接发送请求的可能
        if (StringUtils.isAnyBlank(user.getUserAccount(),
                user.getUserPassword(),
                user.getCheckPassword())){
            return Result.error("用户名或密码为空");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUseraccount,user.getUserAccount());
        long count = count(queryWrapper);
        if (count > 0 ) return Result.error("用户名以存在");
        if (user.getUserPassword().equals(user.getCheckPassword())){
            User register = new User();
            register.setUseraccount(user.getUserAccount());
            register.setEmail(user.getEmail());
            // 加密
            String password = DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes());
            register.setUserpassword(password);
            boolean save = save(register);
            if (!save){
                return Result.error("注册失败");
            }
            return Result.ResultOk("注册成功");
        }
        return Result.error("两次输入的密码不一致");
    }

    @Override
    public boolean isEmailNull(ToEmail toEmail) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,toEmail.getTo());
        long count = count(queryWrapper);
        if (count <1 )
            return true;
        return false;
    }


    @Override
    public Result login(UserRegisterRequest user) {
        if(StringUtils.isAnyBlank(user.getUserAccount(),user.getUserPassword())){
            return Result.error("用户名或密码为空");
        }
        user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + user.getUserPassword())
                .getBytes())) ;

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUseraccount,user.getUserAccount());
        queryWrapper.eq(User::getUserpassword,user.getUserPassword());
        User flag = getOne(queryWrapper);
        if (null == flag) {
            return Result.error("密码错误");
        }
        //为用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userAccount",user.getUserAccount());
        String token = JwtUtil.createJWT(jwtConfig.getSecretKey(), jwtConfig.getTtl(), claims);

        UserVo userVo = new UserVo();
        userVo.setUsername(flag.getUsername());
        userVo.setId(flag.getId());
        userVo.setToken(token);
        return Result.ResultOk(userVo);
    }
    @Override
    public Result friendRequest(String accName){
        String currentAcc = BaseContext.getCurrentAcc();
        // 查询用户A
        User userA = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", currentAcc));
        // 查询用户B
        User userB = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", accName));

        if (userB == null) {
            // 用户不存在
            return Result.error("用户不存在");
        }
        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .and(wrapper -> wrapper
                        .eq("user_id", userA.getId()).eq("friend_id", userB.getId())
                        .or()
                        .eq("user_id", userB.getId()).eq("friend_id", userA.getId())
                );
        Friendship exist = friendMapper.selectOne(queryWrapper);
        if (exist != null) {
            // 已存在好友关系
            return Result.error("好友已申请或已存在");
        }

        Friendship friendship = new Friendship();
        friendship.setUserId(userA.getId());
        friendship.setFriendId(userB.getId());
        friendship.setStatus(0); // 待验证
        friendship.setCreatedTime(LocalDateTime.now());
        friendship.setUpdatedTime(LocalDateTime.now());

        // 插入好友关系
        friendMapper.insert(friendship);
        return Result.ResultOk();
    }

    public Result<List<UserVo>> friendList(){
        String userAccount = BaseContext.getCurrentAcc();
        System.out.println(userAccount);
        // 查询当前用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", userAccount));
        Long currentUserId = user.getId();
        // 查询所有已通过的好友关系，当前用户可能是user_id，也可能是friend_id
        List<Friendship> friendships = friendMapper.selectList(
                new QueryWrapper<Friendship>()
                        .eq("status", 1)
                        .and(wrapper -> wrapper
                                .eq("user_id", currentUserId)
                                .or()
                                .eq("friend_id", currentUserId)
                        )
        );

        // 获取所有好友ID（取不是自己的那一方）
        List<Long> friendIds = friendships.stream()
                .map(f -> f.getUserId().equals(currentUserId) ? f.getFriendId() : f.getUserId())
                .collect(Collectors.toList());

        if (friendIds.isEmpty()) {
            return Result.ResultOk(Collections.emptyList());
        }

        // 查询所有好友信息
        List<User> friends = userMapper.selectBatchIds(friendIds);

        // 转换为UserVo
        List<UserVo> userVoList = friends.stream().map(f -> {
            UserVo vo = new UserVo();
            vo.setId(f.getId());
            vo.setUsername(f.getUsername());
            vo.setAvatarurl(f.getAvatarurl());
            return vo;
        }).collect(Collectors.toList());

        return Result.ResultOk(userVoList);
    }

    @Override
    public Result<List<UserVo>> friendRequestList() {
        // 1. 获取当前登录用户名
        String userAccount = BaseContext.getCurrentAcc();

        // 2. 查询当前用户
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", userAccount));

        Long currentUserId = currentUser.getId();

        // 3. 查询所有发给当前用户的待验证好友请求
        List<Friendship> requests = friendMapper.selectList(
                new QueryWrapper<Friendship>()
                        .eq("friend_id", currentUserId)
                        .eq("status", 0)
        );

        if (requests.isEmpty()) {
            return Result.ResultOk(Collections.emptyList());
        }

        // 4. 获取所有请求发起者的用户ID
        List<Long> requesterIds = requests.stream()
                .map(Friendship::getUserId)
                .collect(Collectors.toList());

        // 5. 查询这些用户的信息
        List<User> requesterUsers = userMapper.selectBatchIds(requesterIds);

        // 6. 转为UserVo
        List<UserVo> voList = requesterUsers.stream().map(u -> {
            UserVo vo = new UserVo();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setAvatarurl(u.getAvatarurl());
            return vo;
        }).collect(Collectors.toList());

        return Result.ResultOk(voList);
    }
    public Result friendAgree(String accName){
// 1. 获取当前登录用户账号
        String currentUserAccount = BaseContext.getCurrentAcc();
        // 2. 查询当前用户
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", currentUserAccount));

        // 3. 查询发起方用户
        User fromUser = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", accName));


        // 4. 查找好友请求记录
        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", fromUser.getId())
                .eq("friend_id", currentUser.getId())
                .eq("status", 0);

        Friendship friendship = friendMapper.selectOne(queryWrapper);
        if (friendship == null) {
            return Result.error("好友请求不存在或已处理");
        }

        // 5. 更新状态为1（已通过）
        friendship.setStatus(1);
        friendship.setUpdatedTime(LocalDateTime.now());
        int update = friendMapper.updateById(friendship);

        if (update > 0) {
            return Result.ResultOk("已同意好友请求");
        } else {
            return Result.error("操作失败");
        }
    }
    public Result friendDelete(String accName){
        String currentAcc = BaseContext.getCurrentAcc();
        User userA = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", currentAcc));
        User userB = userMapper.selectOne(new QueryWrapper<User>().eq("useraccount", accName));
        Long userAId = userA.getId();
        Long userBId = userB.getId();

        QueryWrapper<Friendship> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w
                .eq("user_id", userAId).eq("friend_id", userBId)
                .or()
                .eq("user_id", userBId).eq("friend_id", userAId)
        );
        friendMapper.delete(wrapper);

        return Result.ResultOk();
    }
}
