package com.mz.service.screen.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.GPSUtil;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.vo.BaseSiteInformationVO;
import com.mz.model.screen.model.MapDataModel;
import com.mz.model.screen.vo.TabScreenSearchVO;
import com.mz.service.base.BaseSiteInformationService;
import com.mz.service.screen.ScreenMapDataService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * 地图点位信息查询
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
@Slf4j
@Service("screenMapDataService")
public class ScreenMapDataImpl implements ScreenMapDataService {
    @Autowired
    private BaseSiteInformationService baseSiteInformationService;


    /**
     * 地图点位信息
     *
     */
    public Map<String, Object> getMapData(String modularName, TabScreenSearchVO vo) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MapDataModel> list = new ArrayList<MapDataModel>();
        if (vo.getDataObject().intValue() == 1) {//数据对象 1站点信息
            if (vo.getDataCode().equals("01")) { //站点概览
                BaseSiteInformationVO findVO = new BaseSiteInformationVO();
                findVO.setFindStr(vo.getName());
                List<BaseSiteInformation> siteInformationList = baseSiteInformationService.queryAll(findVO);
                if (CollectionUtil.isNotEmpty(siteInformationList)) {
                    for (BaseSiteInformation baseSiteInformation : siteInformationList) {
                        MapDataModel model = new MapDataModel();
                        BeanUtils.copyProperties(baseSiteInformation,model);
                        if (!StringUtils.isEmpty(baseSiteInformation.getLocalLat()) && !StringUtils.isEmpty(baseSiteInformation.getLocalLng())) {
                            String coordinate84 = GPSUtil.changeCoordinate(baseSiteInformation.getLocalLng() + "," + baseSiteInformation.getLocalLat());
                            model.setCoordinate(baseSiteInformation.getLocalLng() + "," + baseSiteInformation.getLocalLat());//高德坐标
                            model.setCoordinate84(coordinate84.split(",")[0] + "," + coordinate84.split(",")[1]);//天地图坐标
                        }
                        list.add(model);
                    }
                }
            }
        }
        map.put(modularName, list);
        return map;
    }
}
