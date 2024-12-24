package com.mz.model.screen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 电池健康状态
 *
 * @author makejava
 * @since 2021-07-05 11:49:26
 */
@Setter
@Getter
@ToString
public class BatteryHealthModel {
    /**
     * 90%以上
     */
    private String overNinety;
    /**
     * 80%-90%
     */
    private String eightToEightyNine;
    /**
     * 70%-79%
     */
    private String seventyToSeventyNine;
    /**
     * 60%-69%
     */
    private String sixtyToSixtyNine;
    /**
     * 59%以下
     */
    private String underFiftyNine;

}