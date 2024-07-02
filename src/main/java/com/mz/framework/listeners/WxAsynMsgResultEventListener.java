package com.mz.framework.listeners;

import com.alibaba.fastjson.JSON;
import com.mz.common.ConstantsCacheUtil;
import com.mz.framework.event.WxServerAsynMsgEvent;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.framework.util.weixin.WxCheckMediaUtil;
import com.mz.framework.util.weixin.WxServerAsynMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 事件监听
 *
 * @author yangh
 * @date 2022/11/9 17:20
 */
@Slf4j
@Component
public class WxAsynMsgResultEventListener {

    @Autowired
    private RedisUtil redisUtil;

    // 开启异步
    @Async
    @EventListener(WxServerAsynMsg.class)
    public void onApplicationEvent(WxServerAsynMsgEvent wxServerAsynMsgEvent) {
        log.info("事件监听接收到的消息为：" + JSON.toJSONString(wxServerAsynMsgEvent));

        WxServerAsynMsg wxServerAsynMsg = wxServerAsynMsgEvent.getWxServerAsynMsg();
        String traceId = wxServerAsynMsg.getTrace_id();
        String suggest = wxServerAsynMsg.getResult().getSuggest();
        redisUtil.setEx(ConstantsCacheUtil.WXCHECKIMG + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + traceId, suggest, ConstantsCacheUtil.REDIS_MINUTE_TEN_SECOND, TimeUnit.SECONDS);
        WxCheckMediaUtil.resultMap.put(traceId, suggest);
    }
}
