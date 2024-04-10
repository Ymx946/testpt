package com.mz.service.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.BaseSoftwareGroupClassifySys;
import com.mz.model.base.vo.BaseSoftwareGroupClassifySysVO;

import java.util.List;

/**
 * 应用分类关联应用(BaseSoftwareGroupClassifySys)表服务接口
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
public interface BaseSoftwareGroupClassifySysService extends IService<BaseSoftwareGroupClassifySys> {
    /**
     * 保存
     *
     */
    BaseSoftwareGroupClassifySys insert(BaseSoftwareGroupClassifySys pojo,String loginID);
    /**
     * 列表
     */
    PageInfo<BaseSoftwareGroupClassifySys> queryAllByLimit(BaseSoftwareGroupClassifySysVO vo);
    /**
     * 列表
     */
    List<BaseSoftwareGroupClassifySys> queryAll(BaseSoftwareGroupClassifySysVO vo) ;

}
