package com.mz.service.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.model.base.BaseSoftwareGroupClassifyNode;
import com.mz.model.base.vo.BaseSoftwareGroupClassifyNodeVO;

import java.util.List;

/**
 * 应用分类关联应用节点(BaseSoftwareGroupClassifyNode)表服务接口
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
public interface BaseSoftwareGroupClassifyNodeService extends IService<BaseSoftwareGroupClassifyNode> {
    /**
     * 配置节点
     *
     */
    void insertByNode(Long classifySysId,String nodeIdS,String loginID);
    /**
     *列表
     */
    List<BaseSoftwareGroupClassifyNode> queryAll(BaseSoftwareGroupClassifyNodeVO vo);
}
