package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.BaseSiteBatteryPack;
import com.mz.model.base.vo.BaseSiteBatteryPackVO;

import java.util.List;

/**
 * 站点电池组信息表(BaseSiteBatteryPack)表服务接口
 *
 * @author makejava
 * @since 2024-12-25 11:17:56
 */
public interface BaseSiteBatteryPackService  extends IService<BaseSiteBatteryPack>{
    /**
     * 保存
     *
     */
    BaseSiteBatteryPack insert(BaseSiteBatteryPack pojo,String loginID);
    /**
     * 分页列表
     *
     */
     PageInfo<BaseSiteBatteryPack> queryAllByLimit(BaseSiteBatteryPackVO vo);
     /**
     * 查询所有
     *
     */
     List<BaseSiteBatteryPack> queryAll(BaseSiteBatteryPackVO vo);
    

}
