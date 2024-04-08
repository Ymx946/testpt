package com.mz.framework.util.weixin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class WxServerAsynMsg {

    /**
     * 小程序的username
     */
    private String ToUserName;
    /**
     * 平台推送服务UserName
     */
    private String FromUserName;
    /**
     * 发送时间
     */
    private Long CreateTime;
    /**
     * 默认为：Event
     */
    private String MsgType;
    /**
     * 默认为：wxa_media_check
     */
    private String Event;
    /**
     * 小程序的appid
     */
    private String appid;
    /**
     * 任务id
     */
    private String trace_id;
    /**
     * 可用于区分接口版本
     */
    private Integer version;
    /**
     * 综合结果
     */
    private WxAsynMsgResult result;
    /**
     * 详细检测结果
     */
    private List<WxAsynMsgDetail> detail;

    public WxServerAsynMsg(String toUserName, String fromUserName, Long createTime, String msgType, String event, String appid, String trace_id, Integer version, WxAsynMsgResult result, List<WxAsynMsgDetail> detail) {
        this.ToUserName = toUserName;
        this.FromUserName = fromUserName;
        this.CreateTime = createTime;
        this.MsgType = msgType;
        this.Event = event;
        this.appid = appid;
        this.trace_id = trace_id;
        this.version = version;
        this.result = result;
        this.detail = detail;
    }
}
