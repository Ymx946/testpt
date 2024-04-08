package com.mz.framework.util.weixin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class WxTxtCheckResult extends WxComonCheckResult {
    /**
     * 综合结果
     */
    private WxAsynMsgResult result;
    /**
     * 详细检测结果
     */
    private List<WxAsynMsgDetail> detail;

}
