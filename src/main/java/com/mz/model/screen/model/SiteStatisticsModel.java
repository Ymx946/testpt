package com.mz.model.screen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 站点统计
 *
 * @author makejava
 * @since 2021-07-05 11:49:26
 */
@Setter
@Getter
@ToString
public class SiteStatisticsModel {
    /**
     * 充电/组
     */
    private String chargeCount;
    /**
     * 放电/组
     */
    private String dischargeCount;
    /**
     * 浮充/组
     */
    private String floatChargeCount;
    /**
     * 异常/组
     */
    private String abnormalCount;

}