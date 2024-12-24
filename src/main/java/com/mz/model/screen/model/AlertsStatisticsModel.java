package com.mz.model.screen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 告警统计
 *
 * @author makejava
 * @since 2021-07-05 11:49:26
 */
@Setter
@Getter
@ToString
public class AlertsStatisticsModel {
    /**
     * 一般告警数量
     */
    private String generalCount;
    /**
     * 重大告警数量
     */
    private String majorCount;
    /**
     * 紧急告警数量
     */
    private String seriousCount;

}