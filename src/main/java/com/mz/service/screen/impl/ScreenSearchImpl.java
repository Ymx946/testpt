package com.mz.service.screen.impl;

import com.mz.model.base.SysArea;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.SysAreaVO;
import com.mz.model.screen.model.AlertsStatisticsModel;
import com.mz.model.screen.model.BatteryHealthModel;
import com.mz.model.screen.model.SiteStatisticsModel;
import com.mz.model.screen.vo.TabScreenSearchVO;
import com.mz.service.base.SysAreaService;
import com.mz.service.base.SysDataDictService;
import com.mz.service.screen.ScreenMapDataService;
import com.mz.service.screen.ScreenSearchService;
import org.aspectj.bridge.MessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 大屏(TabScreenAge)表服务实现类
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
@Service("screenSearchService")
public class ScreenSearchImpl implements ScreenSearchService {
    @Autowired
    private ScreenMapDataService screenMapDataService;
    @Autowired
    private SysDataDictService sysDataDictService;
    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 查询多条数据
     * 村域概况：overview
     *
     * @return 对象列表
     */
    @Override
    public Map<String, Object> queryByModular(String modularNameS, String typeName, TabScreenSearchVO vo) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] modularNameArr = modularNameS.split(",");
            for (String s : modularNameArr) {
                if (s.equals("mapLocation")) {//地图点位
                    Map<String, Object> resMap = screenMapDataService.getMapData(s, vo);
                    map.put(s, resMap.get(s));
                }

                if (s.equals("sysDataDict")) {//数据字典统一查询接口
                    List<SysDataDict> list = sysDataDictService.queryAll(null, vo.getDictTypeCode(), null, null);
                    map.put(s, list);
                }
                if (s.equals("sysAreaSonList")) {//下极区域列表
                    List<SysArea> list = sysAreaService.queryAreaList(vo.getAreaCode(), vo.getSonLen());
                    map.put(s, list);
                }
                if (s.equals("sysAreaNoSelf")) {//区域查询(没有本层级)
                    List<SysAreaVO> list = sysAreaService.queryAreaListAndSon(vo.getAreaCode(), vo.getSonLen());
                    map.put(s, list);
                }
                if (s.equals("sysArea")) {//区域查询
                    List<SysAreaVO> list = sysAreaService.queryListSelfAndSon(vo.getAreaCode(), vo.getSonLen());
                    map.put(s, list);
                }
                if (s.equals("siteStatistics")) {//1.站点统计
                    SiteStatisticsModel model = new SiteStatisticsModel();
                    model.setChargeCount("0");
                    model.setDischargeCount("0");
                    model.setFloatChargeCount("0");
                    model.setAbnormalCount("0");
                    map.put(s, model);
                }
                if (s.equals("AlertsStatistics")) {//2.告警统计 type=1 当前 type=2 历史
                    AlertsStatisticsModel model = new AlertsStatisticsModel();
                    model.setGeneralCount("0");
                    model.setMajorCount("0");
                    model.setSeriousCount("0");
                    map.put(s, model);
                }
                if (s.equals("BatteryHealth")) {//3.电池健康状态
                    BatteryHealthModel model = new BatteryHealthModel();
                    model.setOverNinety("0");
                    model.setEightToEightyNine("0");
                    model.setSeventyToSeventyNine("0");
                    model.setSixtyToSixtyNine("0");
                    model.setUnderFiftyNine("0");
                    map.put(s, model);
                }
            }
        return map;
    }
}
