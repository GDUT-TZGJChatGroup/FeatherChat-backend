package com.wjz.webserver.domian.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatMessage implements Serializable {
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private LocalDateTime time;
}