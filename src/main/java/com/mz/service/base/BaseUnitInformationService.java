package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.BaseUnitInformation;
import com.mz.model.base.model.BaseUnitInformationModel;
import com.mz.model.base.vo.BaseUnitInformationVO;

import java.util.List;
import java.util.Map;

/**
 * 单位信息表(BaseUnitInformation)表服务接口
 *
 * @author makejava
 * @since 2024-12-23 10:17:28
 */
public interface BaseUnitInformationService extends IService<BaseUnitInformation> {
    /**
     * 保存
     */
    Result insert(BaseUnitInformation pojo, String loginID);

    /**
     * 分页列表
     */
    PageInfo<BaseUnitInformation> queryAllByLimit(BaseUnitInformationVO vo);

    /**
     * 查询所有
     */
    List<BaseUnitInformation> queryAll(BaseUnitInformationVO vo);

    /**
     * 单位选择列表
     */
    List<BaseUnitInformationModel> queryTreeThree(BaseUnitInformationVO vo);

    /**
     * 单位选择列表(五层)
     */
    Map<String, Object> queryTreeFive(BaseUnitInformationVO vo);
    /**
     * 单位选择列表(六层)
     */
    Map<String, Object> queryTreeSix(BaseUnitInformationVO vo);


}
