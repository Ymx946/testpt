package com.mz.common.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class TabCommonFileVO extends BaseDTO {

    private List<Long> idList;
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型
     */
    private Integer[] typeArr;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 文件类型 1-图片 2-音频 3-视频
     */
    private Integer fileType;
    /**
     * 关联ID
     */
    private Long linkId;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 图片高
     */
    private String picHeight;
    /**
     * 图片宽
     */
    private String picWidth;
    /**
     * 创建时间
     */
    private Date createTime;
}
