package com.mz.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 医生表(TabMedicalDoctor)实体类
 *
 * @author makejava
 * @since 2022-01-07 14:49:57
 */
@Setter
@Getter
@ToString
public class TabMedicalDoctorVO extends TabMedicalDoctor {

    /**
     * 预约量
     */
    private Integer registerNum;

    /**
     * 剩余号量
     */
    private Integer grandNum;

    /**
     * 是否可挂号1可挂（有号）2不可挂（无号）
     */
    private Integer registerInState;

}