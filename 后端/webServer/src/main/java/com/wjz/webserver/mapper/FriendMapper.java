package com.wjz.webserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjz.webserver.domian.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper extends BaseMapper<Friendship> {

}
