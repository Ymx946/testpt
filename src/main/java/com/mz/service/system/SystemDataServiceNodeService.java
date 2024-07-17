package com.mz.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.model.system.model.SystemDataServiceNodeModel;
import com.mz.model.system.vo.SystemDataServiceNodeVO;

import java.util.List;

/**
 * 服务节点(SystemDataServiceNode)表服务接口
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
public interface SystemDataServiceNodeService extends IService<SystemDataServiceNode> {
    /**
     * 保存
     */
    SystemDataServiceNode insert(SystemDataServiceNode pojo, String loginID);

    PageInfo<SystemDataServiceNode> queryAllByLimit(SystemDataServiceNodeVO vo);

    List<SystemDataServiceNode> queryAll(SystemDataServiceNodeVO vo);

    SystemDataServiceNodeModel getSystemDataServiceNodeCurMsg(SystemDataServiceNodeVO vo);

}
