package com.mz.common.util.zjzw.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class PersonalInfo implements Serializable {

    /**
     * 头像地址
     */
    private String headPicture;
    /**
     * 身份证
     */
    private String idc;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
}
