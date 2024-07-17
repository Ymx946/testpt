package com.mz.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.SystemDataUpdateSendData;
import com.mz.model.system.vo.SystemDataUpdateSendDataVO;

import java.util.List;

/**
 * 版本更新记录下发数据表(SystemDataUpdateSendData)表服务接口
 *
 * @author makejava
 * @since 2024-04-11 17:07:45
 */
public interface SystemDataUpdateSendDataService extends IService<SystemDataUpdateSendData> {
    /**
     * 保存
     */
    SystemDataUpdateSendData insert(SystemDataUpdateSendData pojo, String loginID);

    List<SystemDataUpdateSendData> queryAll(SystemDataUpdateSendDataVO vo);

    /**
     * 批量保存
     */
    void batchInsert(SystemDataUpdateRecord systemDataUpdateRecord, String jsonStr);

}
