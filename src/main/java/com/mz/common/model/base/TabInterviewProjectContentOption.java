package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 走访项目内容选项表(TabInterviewProjectContentOption)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabInterviewProjectContentOption {
    private static final long serialVersionUID = 866029985404239561L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 内容ID
     */
    private Long contentId;
    /**
     * 内容标题名称
     */
    private String contentTitleName;
    /**
     * 内容选项名称
     */
    private String contentOptionName;
    /**
     * 内容选项颜色
     */
    private String contentOptionColor;
    /**
     * 关联消息通知 1-开启 -1-关闭
     */
    private Integer openMessageInform;
    /**
     * 消息模板ID
     */
    private Long informTemplateId;
    /**
     * 消息模板名称
     */
    private String informTemplateName;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态1正常-1禁用
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
