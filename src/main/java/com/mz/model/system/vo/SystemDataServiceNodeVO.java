package com.mz.model.system.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 服务节点查询
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SystemDataServiceNodeVO extends BaseDTO {
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
