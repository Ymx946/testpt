package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.TabActivitiFlowInfo;
import com.mz.model.base.vo.TabActivitiFlowInfoVO;
import java.util.List;


/**
 * 流程控制表(ActFlowInfo)表服务接口
 *
 * @author makejava
 * @since 2023-02-06 18:24:28
 */
public interface TabActivitiFlowInfoService extends IService<TabActivitiFlowInfo> {
    /**
     * 保存
     *
     */
    TabActivitiFlowInfo insert(TabActivitiFlowInfo pojo, String loginID);
    /**
     * 分页列表展示
     */
    PageInfo<TabActivitiFlowInfo> queryAllByLimit(TabActivitiFlowInfoVO vo);
    /***
     * 列表
     */
    List<TabActivitiFlowInfo> queryAll(TabActivitiFlowInfoVO vo);

}
