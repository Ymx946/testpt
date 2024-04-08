package com.mz.common.model.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 自定义表单(TabFormDefine)表实体类
 *
 * @author yangh
 * @since 2023-04-06 19:31:55
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class FormDefine implements Serializable {

    /**
     * 自定义表单的id
     */
    private String id;

    /**
     * 自定义表单Item
     */
    private com.mz.common.model.form.FormDefineItem options;

}
