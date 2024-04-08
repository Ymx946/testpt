package com.mz.common.model.form.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabFormDefineDataVO extends BaseDTO {
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 头部跨行的标题名称
     */
    private String title;
    /**
     * 导出excel的文件名称
     */
    private String fileName;
    /**
     * excel扩展名称：xls、xlsx
     */
    private String extName;
    /**
     * 登录id
     */
    private String loginID;
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
    private String formCode = "QUEST";
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
