package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.util.Result;
import com.mz.model.base.SysDataDictClassify;
import com.mz.model.base.model.SysDataDictClassifyModel;
import com.mz.model.base.vo.SysDataDictClassifyVO;

import java.util.List;

/**
 * 系统数据字典分类(SysDataDictClassify)表服务接口
 *
 * @author makejava
 * @since 2024-04-08 18:06:51
 */
public interface SysDataDictClassifyService  extends IService<SysDataDictClassify>{
    /**
     * 保存
     *
     */
    Result insert(SysDataDictClassify pojo, String loginID);

    /**
     * 列表
     */
    List<SysDataDictClassifyModel> queryAll(SysDataDictClassifyVO vo);

}
