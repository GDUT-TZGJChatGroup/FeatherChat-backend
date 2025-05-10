package com.wjz.webserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjz.webserver.domian.entity.User;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface UserMapper extends BaseMapper<User> {

}
