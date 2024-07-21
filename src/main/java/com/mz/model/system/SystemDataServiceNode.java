package com.mz.model.system;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务节点(SystemDataServiceNode)实体类
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SystemDataServiceNode implements Serializable {
    private static final long serialVersionUID = -32436650274575751L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 节点名称
     */
    @FieldMeta(name = "节点名称")
    private String nodeName;
    /**
     * 节点地址
     */
    @FieldMeta(name = "节点地址")
    private String nodeUrl;
    /**
     * 在线状态 1在线2不在线
     */
    @FieldMeta(name = "在线状态", readConverterExp = "1=在线,2=不在线")
    private Integer onlineState;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 1运行-1维护
     */
    @FieldMeta(name = "状态", readConverterExp = "1=运行,-1=维护")
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
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 备注
     */
    @FieldMeta(name = "备注")
    private String remarks;

}
