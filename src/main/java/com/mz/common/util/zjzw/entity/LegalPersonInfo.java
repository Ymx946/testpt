package com.mz.common.util.zjzw.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class LegalPersonInfo implements Serializable {
    private String companyLegRep;
    /**
     * 统一社会信用代码
     */
    private String uscc;
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;

}
