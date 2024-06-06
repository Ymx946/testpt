package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 功能节点表(SysNode)实体类
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
@Setter
@Getter
@ToString
public class SysNode {
    private static final long serialVersionUID = 209792503102725771L;
    /**
    * 主键
    */
    private String id;
    /**
    * 所属系统
    */
    private String sysCode;
    /**
    * 节点代码
    */
    private String nodeCode;
    /**
    * 节点名称
    */
    private String nodeName;
    /**
    * 节点展示名称
    */
    private String nodeShowName;
    /**
    * 上级节点代码
    */
    private String paraNodeCode;
    /**
    * 上级节点名称
    */
    private String paraNodeName;
    /**
    * 地址
    */
    private String nodeUrl;
    /**
    * 页面路径
    */
    private String namePath;
    /**
    * 类型1-菜单 2-按钮
    */
    private Integer nodeType;
    /**
    * 图标
    */
    private String nodeIcon;
    /**
    * 级别
    */
    private Integer nodeLevel;
    /**
    * 备注
    */
    private String remarks;
    /**
    * 是否末级
    */
    private Integer lastStage;
    /**
    * 可用状态(1可用2不可用)
    */
    private Integer useState;
    /**
    * 按钮类型  1新增2修改3删除4核实
    */
    private Integer button;
    /**
    * 按钮类型名称
    */
    private String buttonName;
    /**
    * 排序
    */
    private Integer orderNo;
    /**
    * 多端类型 1-PC端、2-移动端
    */
    private Integer terminalType;
    /**
    * 链接类型 1-自建路由、2-自建链接、3-第三方链接
    */
    private Integer linkType;
    /**
     * 层级名称
     */
    private String nodeLevelName;
    /**
     * 操作手册
     */
    private String operatingBooklet;
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

}