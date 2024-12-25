package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统数据字典(SysDataDict)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
@Setter
@Getter
@ToString
public class SysDataDict {
    private static final long serialVersionUID = 380589106554752266L;
    public static String WECHAT_NODE_USE_TYPE_CODE = "WECHAT_USE_SORT";// 数据字典类型代码--小程序功能应用分类
    public static String WECHAT_NODE_SORT_TYPE_CODE = "WECHAT_NODE_SORT";// 数据字典类型代码--小程序功能场景分类
    public static String XTLMGL_DATA_TYPE_CODE = "XTLMGL";// 数据字典类型代码--系统栏目管理
    public static String NODE_BUTTON_TYPE_CODE = "NODE_BUTTON";// 数据字典类型代码--按钮类型
    public static String SYSTEM_TYPE_CODE = "SYSTEM_TYPE"; // 数据字典类型代码--系统类型
    public static String CITY_CLASSIFY_CODE = "CITY_CLASSIFY"; // 城乡分类代码
    public static String UPDATE_SEND_DATA_TYPE_CODE = "UPDATE_SEND_DATA_TYPE";// 版本更新下发数据类型
    public static String VOLTAGE_LEVEL_CODE = "VOLTAGE_LEVEL"; // 数据字典类型代码-电压等级
    public static String HOST_DEVICE_TYPE_CODE = "HOST_DEVICE_TYPE"; // 数据字典类型代码-主机设备类型
    public static String BATTERY_PACK_STATE_CODE = "BATTERY_PACK_STATE"; // 数据字典类型代码-电池组当前状态

    /**
     * 主键
     */
    private String id;
    /**
     * 类型代码
     */
    @FieldMeta(name = "类型代码")
    private String dictTypeCode;
    /**
     * 类型名称
     */
    @FieldMeta(name = "类型名称")
    private String dictTypeName;
    /**
     * 字典代码
     */
    @FieldMeta(name = "字典代码")
    private String dictCode;
    /**
     * 字典名称
     */
    @FieldMeta(name = "字典名称")
    private String dictName;
    /**
     * 区域代码
     */
    @FieldMeta(name = "区域代码")
    private String areaCode;
    /**
     * 备注
     */
    @FieldMeta(name = "备注")
    private String remarks;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 字典图片
     */
    private String dictPic;
    /**
     * 可用状态 (1可用2不可用)
     */
    @FieldMeta(name = "可用状态", readConverterExp = "1=可用,2=不可用")
    private Integer useState;
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