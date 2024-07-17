package com.mz.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 考核统计表头查询
 *
 * @author makejava
 * @since 2023-05-17 16:57:43
 */
@Setter
@Getter
@ToString
public class ExcelStringModel {
    /**
     * 行数
     */
    private Integer lineNum;
    /**
     * 列数
     */
    private Integer arrangeNum;
    /**
     * 值
     */
    private String dataValue;

}
