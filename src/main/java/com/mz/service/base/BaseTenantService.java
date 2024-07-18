package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.BaseTenant;
import com.mz.model.base.vo.BaseTenantVO;

import java.util.List;

/**
 * 租户表(BaseTenant)表服务接口
 *
 * @author makejava
 * @since 2024-07-18 13:47:24
 */
public interface BaseTenantService extends IService<BaseTenant> {
    /**
     * 保存
     */
    Result insert(BaseTenant pojo, String loginID);

    /**
     * 分页列表
     */
    PageInfo<BaseTenant> queryAllByLimit(BaseTenantVO vo);

    /**
     * 查询所有
     */
    List<BaseTenant> queryAll(BaseTenantVO vo);

    /**
     * 租户下发
     */
    Integer issued(Long id);

    /**
     * 租户重新下发
     */
    Integer reissued(Long id);

}
