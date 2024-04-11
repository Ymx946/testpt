package com.mz.model.system.model;

import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.SystemDataUpdateSendData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)实体类
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SystemDataUpdateRecordModel extends SystemDataUpdateRecord {
    /**
     * 版本更新记录下发数据集合
     */
    List<SystemDataUpdateSendData> systemDataUpdateSendDataList;
}
