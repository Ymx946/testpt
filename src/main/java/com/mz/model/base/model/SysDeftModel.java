package com.mz.model.base.model;

import com.mz.model.base.SysDeft;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统定义表(SysDeft)实体类
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
@Setter
@Getter
@ToString
public class SysDeftModel extends SysDeft {
    /**
     * 应用点击时间
     */
    private String clickTime;
    /**
     * 应用点击时间
     */
    private String classifyId;
}