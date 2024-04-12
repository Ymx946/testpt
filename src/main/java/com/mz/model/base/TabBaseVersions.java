package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *版本管理表(TabBaseVersions)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabBaseVersions {
    private static final long serialVersionUID = 463166192261376554L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 版本号
    */
    private String versionNumber;
    /**
    * 更新内容
    */
    private String updateContent;
    /**
     * 系统代码
     */
    private String systemCode;
    /**
     * 系统名称
     */
    private String systemName;
    /**
    * 状态
    */
    private Integer state;
    /**
    * 逻辑删除
    */
    private Integer delState;
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
