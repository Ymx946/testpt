package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
public class UploadParamsDto implements Serializable {

    /**
     * 所属地区行政区代码
     **/
    private String areaNumber;

    private List<AttributeParamDto> attributeParamDtoList;
    /**
     * 所属主体唯一 ID:身份证号,社会统一信用代码(农产品分类及资源装备分类时 必填)
     */
    private String belong;
    /**
     * 所属主体唯一 ID 的类型 1-身份证 2-统一社会信用代码(农产品分类及资源装备分类时必填)
     */
    private Integer belongType;
    /**
     * 唯一性标识
     */
    private String extendCode;
    /**
     * 唯一性标识类型, 1-统一社会信用代码,2-身份证号,3-农户户号,4-行政村代码,5-农产品编号,6-资源装备编号
     */
    private Integer extendCodeType;
    /**
     * 赋码对象的图片 url
     */
    private String imageUrl;
    /**
     * 赋码对象名称
     */
    private String objectName;

}
