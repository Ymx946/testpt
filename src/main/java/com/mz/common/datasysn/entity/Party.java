package com.mz.common.datasysn.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Party implements Serializable {
    /**
     * 主键
     **/
    private Long id;
    /**
     * 党员照片(多个文件用英文,分割)
     **/
    private String party_pic_url;
    /**
     * 职责
     **/
    private String party_duty;
    /**
     * 姓名
     **/
    private String party_member_name;
    /**
     * 班子类别(党组织-党组织,村委会-村委会,村监会-村监会,合作社-合作社)
     **/
    private String party_type;
    /**
     * 层级(第一级-1,非第一层级-3)
     **/
    private Integer party_level;
}
