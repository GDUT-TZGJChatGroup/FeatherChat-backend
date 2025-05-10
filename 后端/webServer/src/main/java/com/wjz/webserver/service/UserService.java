package com.wjz.webserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjz.webserver.domian.dto.ToEmail;
import com.wjz.webserver.domian.entity.User;
import com.wjz.webserver.domian.entity.vo.UserVo;
import com.wjz.webserver.domian.request.UserRegisterRequest;
import com.wjz.webserver.utils.Result;

import java.util.List;



public interface UserService extends IService<User> {

    /**
     *  用户注册
     * @param user 注册参数
     * @return 是否注册功能 或失败原因
     */
    Result userRegister(UserRegisterRequest user);

    /**
     *  判断邮箱是否注册
     * @return true|false
     */
    boolean isEmailNull(ToEmail toEmail);

    /**
     *  用户登录
     * @param user 用户数据
     * @return 返回用户信息(用户名与id)
     */
    Result login(UserRegisterRequest user);

    Result friendRequest(String accName);

    Result<List<UserVo>> friendList();

    Result<List<UserVo>> friendRequestList();

    Result friendAgree(String accName);

    Result friendDelete(String accName);
}

