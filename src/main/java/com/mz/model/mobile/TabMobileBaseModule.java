package com.mz.model.mobile;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *移动组件表(TabMobileBaseModule)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabMobileBaseModule {
    private static final long serialVersionUID = -66926760843134968L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 组件模块名称
    */
    private String moduleName;
    /**
    * 分类1-基础组件2-业务组件
    */
    private Integer moduleType;
    /**
    * 是否横向滑动1是2否
    */
    private Integer transverseState;
    /**
     * 数据1-指定数量(列表)2-无限数据(分页)3-自定义(多选)
     */
    private String moduleData;
    /**
    * 图标
    */
    private String moduleIcon;
    /**
    * 缩略图
    */
    private String modulePic;
    /**
     * 标题控制(1-是 2-否)
     */
    private Integer needTitle;
    /**
     * 使用场景(1-首页 2-活动页 3-一户一码 4-服务大厅5-光荣榜6-业务专区 8-商城 99我的页面)
     */
    private String useScene;
    /**
     * 数据源类型(多选)数据字典
     */
    private String dataSourceType;
    /**
     * 是否设置背景色 1是2否
     */
    private Integer isSetBackColor;
    /**
     * 背景色
     */
    private String backColor;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 状态 1已启用 -1禁用
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
