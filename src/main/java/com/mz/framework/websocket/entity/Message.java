package com.mz.framework.websocket.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@SuppressWarnings("serial")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Message {
    /**
     * 消息类型
     */
    private String type;

    /**
     * 更新时间
     */
    private String msg;
    /**
     * 更新内容
     */
    private String content;

    /**
     * 发送时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}

