package com.mz.common.model.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义表单(TabFormDefine)表实体类
 *
 * @author yangh
 * @since 2023-04-06 19:31:55
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabFormDefineData implements Serializable {
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 所属租户
     */
    private Long tenantId;
    /**
     * 所属主体ID
     */
    private Long mainBodyId;
    /**
     * 所属主体代码
     */
    private String mainBodyCode;
    /**
     * 主体名称
     */
    private String mainBodyName;
    /**
     * 区分不同的功能
     */
    private String formCode;
    /**
     * 分类字典编码
     */
    private String categoryDictCode;
    /**
     * 分类字典名称
     */
    private String categoryDictName;
    /**
     * 使用范围：1-本区域 2-本区域以下
     */
    private Integer useScope;
    /**
     * 自定义表单ID
     */
    private Long formId;
    /**
     * 自定义表单数据
     */
    private String content;
    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 手写签名url
     */
    private String signUrl;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 -1:无效 1:有效
     */
    private Integer state;
    /**
     * 创建时间
     */
    @CreatedDate
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建人的id
     */
    private Long createUserId;
    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 修改人的id
     */
    private Long modifyUserId;

}

