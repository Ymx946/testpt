package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应用分类关联应用查询
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
@Setter
@Getter
@ToString
public class BaseSoftwareGroupClassifySysVO extends BaseDTO {
    /**
    * 主键
    */
    private Long id;
    /**
    * 所属租户
    */
    private Long tenantId;
    /**
    * 分组ID
    */
    private Long groupId;
    /**
    * 分类ID
    */
    private Long classifyId;
    /**
    * 系统ID
    */
    private Long sysId;
    /**
    * 系统代码
    */
    private String sysCode;
    /**
    * 系统名称
    */
    private String sysName;
    /**
    * 图片logo
    */
    private String sysLogo;
    /**
    * 发布范围
    */
    private String releaseScope;
    /**
    * 范围名称
    */
    private String scopeName;
    /**
    * 所属类型 1-自建 2-第三方
    */
    private Integer belongType;
    /**
    * 区域级别 1-省2-市3-区县4-乡镇5-村社
    */
    private String areaLevel;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 状态 1已配置 -1 未配置
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
