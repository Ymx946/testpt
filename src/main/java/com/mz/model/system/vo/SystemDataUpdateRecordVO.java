package com.mz.model.system.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 服务节点版本更新记录查询
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SystemDataUpdateRecordVO extends BaseDTO {
    /**
     * 创建时间起
     */
    private String createTimeS;
    /**
     * 创建时间止
     */
    private String createTimeE;
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
     * 系统代码
     */
    private String systemCode;
    /**
     * 系统名称
     */
    private String systemName;
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
    * 状态 0-未下发 1已下发 -1下发失败
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
