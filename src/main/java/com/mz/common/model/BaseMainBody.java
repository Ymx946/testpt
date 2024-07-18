package com.mz.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 主体表(BaseMainBody)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
@Setter
@Getter
@ToString
public class BaseMainBody {
    private static final long serialVersionUID = 263819037397482496L;
    public static int MAIN_BODY_TYPE_MANAGE = 1;//默认政府主体
    public static int MAIN_BODY_TYPE_VILLAGE = 2;//村主体
    public static int MAIN_BODY_TYPE_ENTERPRISE = 3;//企业
    public static int MAIN_BODY_TYPE_PERSONAL = 4;//4个体工商户
    public static int MAIN_BODY_TYPE_PLACE = 7;//场所(在线商城)
    public static int MAIN_BODY_TYPE_YS = 99;//演示主体
    public static String MAIN_BODY_TYPE_S = "1,2,3,4,5,6,7,10,11,12";//可操作主体 --去除了8文明实践站9志愿者队伍
    public static Integer[] MAIN_BODY_TYPE_INTEGER_ARR = new Integer[]{1, 2, 3, 4, 5, 6, 7, 10, 11, 12};//可操作主体 --去除了8文明实践站9志愿者队伍 数组
    /**
     * 主键
     */
    private String id;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 所属机构ID
     */
    private String instId;
    /**
     * 所属机构代码
     */
    private String instCode;
    /**
     * 所属机构名称
     */
    private String instName;
    /**
     * 主体类型1政府机关 2-村主体  3企业  4个体工商户 5自然人6交易市场7场所(在线商城)8文明实践站9志愿者队伍10联村党委11医院12卫生院
     */
    private Integer mainBodyType;
    /**
     * 主体代码
     */
    private String mainBodyCode;
    /**
     * 主体名称
     */
    private String mainBodyName;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String contactNo;
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 地址
     */
    private String address;
    /**
     * 坐标
     */
    private String coordinate;
    /**
     * 管理类型 1监管端2企业端3演示端
     */
    private Integer manageType;
    /**
     * 可用状态
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

}