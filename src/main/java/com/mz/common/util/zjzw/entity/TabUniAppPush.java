package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author mx
 * @date 2023/3/14
 */
@Setter
@Getter
@ToString
public class TabUniAppPush {
    /**
     * 推送标题
     */
    private String title;
    /**
     * 推送内容
     */
    private String content;
    /**
     * ClientId集合
     */
    private List<String> cids;

}
