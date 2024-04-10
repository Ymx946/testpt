package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *基础移动应用表(TabBasicMoveApp)
 */
@Setter
@Getter
@ToString
public class TabBasicMoveAppVO extends BaseDTO {
    /**
     * 发布对象
     */
    private Integer[] bannerObjArr;
    /**
     * 登录人的id
     */
    private String loginID;
    /**
     * 主键
     */
    private Long id;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用图标
     */
    private String appIcon;
    /**
     * 应用归属1本地2第三方公共3第三方用户4第三方小程序
     */
    private Integer appBelong;
    /**
     * 应用地址
     */
    private String appUrl;
    /**
     * 发布对象1村民2村友3游客4村委(多选)
     */
    private String bannerObj;
    /**
     * 应用类型 1-服务大厅 2-我的
     */
    private Integer appType;
    /**
     * PC应用代码
     */
    private String pcSysCode;
    /**
     * PC应用名称
     */
    private String pcSysName;
    /**
     * 分类代码
     */
    private String sortCode;
    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 服务功能ID（类型=2我的才有）
     */
    private Long paraNodeId;
    /**
     * 第三方小程序APPid
     */
    private String thirdAppId;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 1已启用-1禁用
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String creatTime;
    /**
     * 创建人
     */
    private String creatUser;
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
