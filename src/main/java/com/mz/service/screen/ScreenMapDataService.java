package com.mz.service.screen;

import com.mz.model.screen.vo.TabScreenSearchVO;

import java.util.Map;

/**
 * 地图点位信息查询
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
public interface ScreenMapDataService {
    /**
     * 分模块查询
     * 按传入的模块返回对应的数据
     *
     * @return 对象列表
     */
    Map<String, Object> getMapData(String modularName, TabScreenSearchVO vo);

}
