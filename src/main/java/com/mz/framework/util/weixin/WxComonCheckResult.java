package com.mz.framework.util.weixin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class WxComonCheckResult {

    /**
     * 唯一请求标识，标记单次请求，用于匹配异步推送结果
     */
    private String trace_id;
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;

}
