package com.wjz.webserver.domian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friendship")
public class Friendship {
    // 用户id@TableId
    @TableId(value = "id" ,type = IdType.AUTO)
    private Long id;

    /**
     * 用户A的ID
     */
    private Long userId;

    /**
     * 用户B的ID（好友ID）
     */
    private Long friendId;

    /**
     * 好友关系状态（0=待验证，1=已通过）
     */
    private Integer status;

    /**
     * 建立关系时间
     */

    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
