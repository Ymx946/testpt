package com.mz.framework.event;

import com.mz.framework.util.weixin.WxServerAsynMsg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author yangh
 * @date 2022/11/29 12:04
 */
@Setter
@Getter
@ToString
public class WxServerAsynMsgEvent extends ApplicationEvent {

    private WxServerAsynMsg wxServerAsynMsg;

    public WxServerAsynMsgEvent(Object source) {
        super(source);
    }

    public WxServerAsynMsgEvent(Object source, WxServerAsynMsg wxServerAsynMsg) {
        super(source);
        this.wxServerAsynMsg = wxServerAsynMsg;
    }
}
