package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 应用密钥(TabApplicationSecretKey)表实体类
 *
 * @author yangh
 * @since 2023-02-23 10:55:36
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabApplicationSecretKey {
    //主键
    private Long id;
    //应用申请ID
    private Long applicationBaseId;
    //应用类型1-PC 2-H5
    private Integer applicationType;
    //AppId
    private String appId;
    //AppKey
    private String appKey;
    //应用ID(根据类型放入对应应用表的ID)
    private Long applicationId;
    //状态
    private Integer state;
    //逻辑删除
    private Integer delState;
    //创建时间
    private Date createTime;
    //创建人
    private String createUser;
    //修改时间
    private Date modifyTime;
    //修改人
    private String modifyUser;
    //备注
    private String remarks;

}

