package com.mz.common.model.open.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class OpenUserModel {

    /**
     * ⽤户名
     */
    private String userName;
    /**
     * ⼿机号
     */
    private String phone;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * ⽤户昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 员⼯编码
     */
    private String employeeCode;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private String gender;

}
