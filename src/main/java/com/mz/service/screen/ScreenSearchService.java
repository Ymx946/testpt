package com.mz.service.screen;

import com.mz.model.screen.vo.TabScreenSearchVO;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 年龄分布(TabScreenAge)表服务接口
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
public interface ScreenSearchService {
    /**
     * 分模块查询
     * 按传入的模块返回对应的数据
     *
     * @param modularNameS 模块名称串
     * @return 对象列表
     */
    Map<String, Object> queryByModular(String modularNameS, String typeName, TabScreenSearchVO vo) throws UnsupportedEncodingException;

}
