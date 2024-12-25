package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.BaseSiteHost;
import com.mz.model.base.vo.BaseSiteHostVO;

import java.util.List;

/**
 * 站点主机信息表(BaseSiteHost)表服务接口
 *
 * @author makejava
 * @since 2024-12-25 08:58:37
 */
public interface BaseSiteHostService  extends IService<BaseSiteHost>{
    /**
     * 保存
     *
     */
    BaseSiteHost insert(BaseSiteHost pojo,String loginID);
    /**
     * 分页列表
     *
     */
     PageInfo<BaseSiteHost> queryAllByLimit(BaseSiteHostVO vo);
     /**
     * 查询所有
     *
     */
     List<BaseSiteHost> queryAll(BaseSiteHostVO vo);
    

}
