package com.mz.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 医生表(TabMedicalDoctor)实体类
 *
 * @author makejava
 * @since 2022-01-07 14:49:57
 */
@Setter
@Getter
@ToString
public class TabMedicalDoctor {
    private static final long serialVersionUID = -49221395372179029L;
    /**
    * 主键
    */
    private String id;
    /**
    * 所属租户
    */
    private String tenantId;
    /**
    * 登记日期
    */
    private String registerDate;
    /**
    * 姓名
    */
    private String doctorName;
    /**
    * 身份证
    */
    private String idNo;
    /**
    * 联系电话
    */
    private String phoneNo;
    /**
    * 医生照片
    */
    private String doctorPortrait;
    /**
    * 就职医院
    */
    private String hospital;
    /**
    * 科室
    */
    private String department;
    /**
    * 职务
    */
    private String doctorDuty;
    /**
    * 问诊电话
    */
    private String doctorPhone;
    /**
    * 擅长
    */
    private String expertContent;
    /**
     * 是否显示 1显示2不显示
     */
    private Integer showState;
    /**
    * 可用状态1可用2不可用
    */
    private Integer useState;
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