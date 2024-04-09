package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 区域表(SysArea)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:48
 */
@Setter
@Getter
@ToString
public class SysAreaLimitVO extends BaseDTO {
    /**
     * 查询条件(区域名称/区域代码)
     */
    private String findStr;
    /**
    * 主键
    */
    private String id;
    /**
    * 区域代码
    */
    private String areaCode;
    /**
    * 区域名称
    */
    private String areaName;
    /**
     * 城市分类代码
     */
    private String cityClassifyCode;
    /**
     * 城市分类名称
     */
    private String cityClassifyName;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 备注
     */
    private String remarks;

}