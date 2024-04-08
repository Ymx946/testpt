package com.mz.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.common.context.PageInfo;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;

import java.util.List;

/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)表服务接口
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
public interface SystemDataUpdateRecordService  extends IService<SystemDataUpdateRecord>{
    /**
     * 保存
     *
     */
    SystemDataUpdateRecord insert(SystemDataUpdateRecord pojo,String loginID);
    
    PageInfo<SystemDataUpdateRecord> queryAllByLimit(SystemDataUpdateRecordVO vo);
    List<SystemDataUpdateRecord> queryAll(SystemDataUpdateRecordVO vo);

}
