package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class ZnmCodeParamsV3Dto implements Serializable {

    /**
     * 回调url
     **/
    private String callbackUrl;

    /**
     * 客户端 id
     **/
    private String clientId;

    /**
     * 扩展类型 3-单码
     **/
    private Integer extendType;

    /**
     * 扩展类型 3-单码
     **/
    private String resolveUrl;

    /**
     * 上传参数 lis
     */
    private List<UploadParamsDto> uploadParamsDtoList;

    /**
     * 用途代码,详细编码请参考附件及编码规范
     */
    private String useForCode;


}
