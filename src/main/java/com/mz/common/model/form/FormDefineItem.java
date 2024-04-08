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
public class FormDefineItem implements Serializable {
    /**
     * 自定义表单字段
     */
    private String name;
    /**
     * 自定义表单标签
     */
    private String label;
}

