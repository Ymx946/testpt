package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.vo.BaseSiteInformationVO;

import java.util.List;

/**
 * 站点信息表(BaseSiteInformation)表服务接口
 *
 * @author makejava
 * @since 2024-12-23 14:06:22
 */
public interface BaseSiteInformationService  extends IService<BaseSiteInformation>{
    /**
     * 保存
     *
     */
    BaseSiteInformation insert(BaseSiteInformation pojo,String loginID);
    /**
     * 分页列表
     *
     */
     PageInfo<BaseSiteInformation> queryAllByLimit(BaseSiteInformationVO vo);
     /**
     * 查询所有
     *
     */
     List<BaseSiteInformation> queryAll(BaseSiteInformationVO vo);
    

}
