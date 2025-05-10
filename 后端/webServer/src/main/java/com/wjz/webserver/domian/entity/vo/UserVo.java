package com.wjz.webserver.domian.entity.vo;

import lombok.Data;


@Data
public class UserVo {
    private Long id;
    // 昵称
    private String username;
    private String avatarurl;

    private String token;
}
