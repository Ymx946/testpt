package com.mz.model.system.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 服务节点版本更新记录导出
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@Setter
@Getter
@ToString
public class ExportUpdateRecordModel {
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 版本号
     */
    private String versionNo;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 更新内容
     */
    private String updateContent;

}
