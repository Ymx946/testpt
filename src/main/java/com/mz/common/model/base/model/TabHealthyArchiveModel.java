package com.mz.common.model.base.model;

import com.mz.common.model.base.TabCommonFile;
import com.mz.common.model.base.TabHealthyArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 健康档案表(TabHealthyArchive)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabHealthyArchiveModel extends TabHealthyArchive {
    /**
     * 图片集合
     */
    private List<TabCommonFile> fileList;
}
