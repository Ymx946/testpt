package com.mz.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.util.Result;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.common.context.PageInfo;
import com.mz.model.system.model.SystemDataUpdateRecordModel;
import com.mz.model.system.vo.SelectSearchVO;
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
    SystemDataUpdateRecord insert(SystemDataUpdateRecord pojo,String loginID,String jsonStr);

    /**
     * 分页列表
     */
    PageInfo<SystemDataUpdateRecord> queryAllByLimit(SystemDataUpdateRecordVO vo);

    /**
     * 查询所有
     */
    List<SystemDataUpdateRecord> queryAll(SystemDataUpdateRecordVO vo);

    /**
     * 详情
     */
    SystemDataUpdateRecordModel queryById(Long id);

    /**
     * 下发数据选择列表
     */
    Result queryBusForSelect(SelectSearchVO vo);

}
