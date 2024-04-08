package com.mz.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 考核统计表头查询
 *
 * @author makejava
 * @since 2023-05-17 16:57:43
 */
@Setter
@Getter
@ToString
public class TitleModel {
    /**
    * 表头对应数据ID
    */
    private Long id;
    /**
    * 表头名称
    */
    private String name;
    /**
    * 参数名
    */
    private String dataName;
    /**
     * 二级表头名称
     */
    private List<TitleModel> sonTitle;

}
