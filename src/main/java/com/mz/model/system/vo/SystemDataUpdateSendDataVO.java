package com.mz.model.system.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *版本更新记录下发数据表(SystemDataUpdateSendData)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SystemDataUpdateSendDataVO {
    private static final long serialVersionUID = -15819035110003051L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 更新记录ID
    */
    private Long recordId;
    /**
    * 下发数据类型代码
    */
    private String sendDataTypeCode;
    /**
    * 下发数据类型名称
    */
    private String sendDataTypeName;
    /**
    * 下发数据业务ID
    */
    private Long sendDataId;
    /**
    * 下发数据名称
    */
    private String sendDataName;
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
