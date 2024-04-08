package com.mz.model.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)实体类
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SystemDataUpdateRecord  {
    private static final long serialVersionUID = 855804138903059127L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 节点id
    */
    private Long nodeId;
    /**
    * 节点名称
    */
    private String nodeName;
    /**
    * 版本号
    */
    private String versionNo;
    /**
    * 更新时间
    */
    private String updateTime;
    /**
    * 更新内容
    */
    private String updateContent;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 状态
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
