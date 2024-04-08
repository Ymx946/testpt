package com.mz.model.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class SystemDataServiceNode  {
    private static final long serialVersionUID = -32436650274575751L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 节点名称
    */
    private String nodeName;
    /**
    * 节点地址
    */
    private String nodeUrl;
    /**
    * 在线状态1在线 2不在线
    */
    private Integer onlineState;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 状态 1运行 -1维护
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
    private String remarks;

}
