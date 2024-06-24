package com.mz.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.SysAreaMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysArea;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.SysAreaLimitVO;
import com.mz.model.base.vo.SysAreaVO;
import com.mz.model.base.vo.SysFullAreaVO;
import com.mz.service.base.SysAreaService;
import com.mz.service.base.SysDataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 区域表(SysArea)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 10:58:50
 */
@Service("sysAreaService")
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements SysAreaService {
    @Resource
    private SysAreaMapper sysAreaMapper;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private SysDataDictService sysDataDictService;

    @Override
    public List<SysArea> queryAllByAreaCode(SysArea sysArea) {
        return this.sysAreaMapper.queryAllByAreaCode(sysArea);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysArea queryById(String id) {
        return this.sysAreaMapper.queryById(id);
    }

    /**
     * 通过代码查询单条数据
     *
     * @param areaCode 主键
     * @return 实例对象
     */
    @Override
    public SysArea queryByCode(String areaCode) {
        return this.sysAreaMapper.queryByCode(areaCode);
    }

    /**
     * 获取名称map
     *
     * @return 实例对象
     */
    @Override
    public Map<String, SysArea> queryNameMap(String areaCode, Integer len) {
        Map<String, SysArea> map = new HashMap<>();
        List<SysArea> list = queryAreaList(areaCode, len);
        for (SysArea sysArea : list) {
            map.put(sysArea.getAreaName(), sysArea);
        }
        return map;
    }

    /**
     * 获取code map
     *
     * @return 实例对象
     */
    @Override
    public Map<String, SysArea> queryCodeMap(String areaCode, Integer len) {
        Map<String, SysArea> map = new HashMap<>();
        List<SysArea> list = queryAreaList(areaCode, len);
        for (SysArea sysArea : list) {
            map.put(sysArea.getAreaCode(), sysArea);
        }
        return map;
    }

    @Override
    public Map<String, String> getSysAreaCacheMap(SysArea sysArea) {
        String sysareaCacheAll = redisUtil.get(ConstantsCacheUtil.SYSAREA_CACHE_ALL_LIST);
        if (StringUtils.isEmpty(sysareaCacheAll)) {
            List<SysArea> sysAreas = this.sysAreaMapper.queryAllByAreaCode(sysArea);
            sysareaCacheAll = JSON.toJSONString(sysAreas);
            redisUtil.setEx(ConstantsCacheUtil.SYSAREA_CACHE_ALL_LIST + sysArea.getAreaCode(), sysareaCacheAll, ConstantsCacheUtil.REDIS_MINUTE_TEN_SECOND, TimeUnit.SECONDS);
        }
        Map<String, String> sysAreaMap = JSON.parseArray(sysareaCacheAll, SysArea.class).parallelStream().collect(Collectors.toMap(SysArea::getAreaCode, SysArea::getAreaName, (key1, key2) -> key2));
        return sysAreaMap;
    }

    /**
     * 通过代码查询单条数据
     *
     * @return 实例对象
     */
    @Override
    public SysArea queryAreaByName(String paraCode, String areaName, Integer len) {
        return this.sysAreaMapper.queryAreaByName(paraCode, areaName, len);
    }

    /**
     * 通过代码查询单条数据
     *
     * @param areaCode 主键
     * @return 实例对象
     */
    @Override
    public SysArea queryByCodeAndLen(String areaCode, Integer len) {
        SysArea sysArea = null;
        if (areaCode.length() >= len) {
            sysArea = sysAreaMapper.queryByCode(areaCode.substring(0, len.intValue()));
        }
        return sysArea;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SysArea> queryAllByLimit(int offset, int limit) {
        return this.sysAreaMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<SysArea> queryAreaList(String paraCode, Integer len) {
        return this.sysAreaMapper.queryAreaList(paraCode, len);
    }

    /**
     * 根据上级查询（有村的下级）
     *
     * @return 对象列表
     */
    @Override
    public List<SysArea> queryAreaListForVillage(String paraCode, Integer len) {
        Map<String, List<SysArea>> mapVillage = new HashMap<String, List<SysArea>>();
        List<SysArea> listVillage = this.sysAreaMapper.queryAreaList(paraCode, 12);
        for (SysArea sysArea : listVillage) {
            List<SysArea> listVillage2 = mapVillage.get(sysArea.getAreaCode().substring(0, 2));
            listVillage2 = listVillage2 != null ? listVillage2 : new ArrayList<SysArea>();
            listVillage2.add(sysArea);
            mapVillage.put(sysArea.getAreaCode().substring(0, 2), listVillage2);
            List<SysArea> listVillage4 = mapVillage.get(sysArea.getAreaCode().substring(0, 4));
            listVillage4 = listVillage4 != null ? listVillage4 : new ArrayList<SysArea>();
            listVillage4.add(sysArea);
            mapVillage.put(sysArea.getAreaCode().substring(0, 4), listVillage4);
            List<SysArea> listVillage6 = mapVillage.get(sysArea.getAreaCode().substring(0, 6));
            listVillage6 = listVillage6 != null ? listVillage6 : new ArrayList<SysArea>();
            listVillage6.add(sysArea);
            mapVillage.put(sysArea.getAreaCode().substring(0, 6), listVillage6);
            List<SysArea> listVillage9 = mapVillage.get(sysArea.getAreaCode().substring(0, 9));
            listVillage9 = listVillage9 != null ? listVillage9 : new ArrayList<SysArea>();
            listVillage9.add(sysArea);
            mapVillage.put(sysArea.getAreaCode().substring(0, 9), listVillage9);
            List<SysArea> listVillage12 = mapVillage.get(sysArea.getAreaCode());
            listVillage12 = listVillage12 != null ? listVillage12 : new ArrayList<SysArea>();
            listVillage12.add(sysArea);
            mapVillage.put(sysArea.getAreaCode(), listVillage12);

        }
        List<SysArea> reList = new ArrayList<SysArea>();
        List<SysArea> list = this.sysAreaMapper.queryAreaList(paraCode, len);
        for (SysArea sysArea : list) {
            List<SysArea> listVillage1 = mapVillage.get(sysArea.getAreaCode());
            if (listVillage1 != null && listVillage1.size() > 0) {
                reList.add(sysArea);
            }
        }
        return reList;
    }

    /**
     * 查询区域分层机
     *
     * @return 对象列表
     */
    @Override
    public List<SysAreaVO> queryAreaListAndSon(String paraCode, Integer maxLen) {
        if (paraCode.length() < 12) {
            int len = 2;
            if (!StringUtils.isEmpty(paraCode)) {
                if (paraCode.length() == 2) {//
                    len = 4;
                } else if (paraCode.length() == 4) {
                    len = 6;
                } else if (paraCode.length() == 6) {
                    len = 9;
                } else if (paraCode.length() == 9) {
                    len = 12;
                }
            }
            List<SysAreaVO> sysAreaVOList = this.sysAreaMapper.queryAreaListAndSon(paraCode, len, maxLen);
            if (sysAreaVOList != null && sysAreaVOList.size() > 0) {
                for (SysAreaVO sysAreaVO : sysAreaVOList) {
                    List<SysAreaVO> sonList = queryAreaListAndSon(sysAreaVO.getAreaCode(), maxLen);
                    sysAreaVO.setSonList(sonList);
                }
            }
            return sysAreaVOList;
        } else {
            return null;
        }
    }

    /**
     * 查询区域(所有层级--有村的上级)
     *
     * @return 对象列表
     */
    @Override
    public List<SysAreaVO> queryAllByCodeForVillage(String paraCode) {
        List<SysAreaVO> sysAreaVOList = new ArrayList<SysAreaVO>();
        Map<String, List<SysAreaVO>> mapVillage = new HashMap<String, List<SysAreaVO>>();
        Map<String, List<SysAreaVO>> map = new HashMap<String, List<SysAreaVO>>();
        List<SysAreaVO> list2 = new ArrayList<SysAreaVO>();
        List<SysAreaVO> sysAreaList = this.sysAreaMapper.queryAllByCode(paraCode);
        for (SysAreaVO sysAreaVO : sysAreaList) {
            if (sysAreaVO.getAreaCode().length() == 2) {
                list2.add(sysAreaVO);
            } else if (sysAreaVO.getAreaCode().length() == 4) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 2));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 2), list);
            } else if (sysAreaVO.getAreaCode().length() == 6) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 4));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 4), list);
            } else if (sysAreaVO.getAreaCode().length() == 9) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 6));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 6), list);
            } else if (sysAreaVO.getAreaCode().length() == 12) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 9));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 9), list);

                List<SysAreaVO> listVillage2 = mapVillage.get(sysAreaVO.getAreaCode().substring(0, 2));
                listVillage2 = listVillage2 != null ? listVillage2 : new ArrayList<SysAreaVO>();
                listVillage2.add(sysAreaVO);
                mapVillage.put(sysAreaVO.getAreaCode().substring(0, 2), listVillage2);
                List<SysAreaVO> listVillage4 = mapVillage.get(sysAreaVO.getAreaCode().substring(0, 4));
                listVillage4 = listVillage4 != null ? listVillage4 : new ArrayList<SysAreaVO>();
                listVillage4.add(sysAreaVO);
                mapVillage.put(sysAreaVO.getAreaCode().substring(0, 4), listVillage4);
                List<SysAreaVO> listVillage6 = mapVillage.get(sysAreaVO.getAreaCode().substring(0, 6));
                listVillage6 = listVillage6 != null ? listVillage6 : new ArrayList<SysAreaVO>();
                listVillage6.add(sysAreaVO);
                mapVillage.put(sysAreaVO.getAreaCode().substring(0, 6), listVillage6);
                List<SysAreaVO> listVillage9 = mapVillage.get(sysAreaVO.getAreaCode().substring(0, 9));
                listVillage9 = listVillage9 != null ? listVillage9 : new ArrayList<SysAreaVO>();
                listVillage9.add(sysAreaVO);
                mapVillage.put(sysAreaVO.getAreaCode().substring(0, 9), listVillage9);
                List<SysAreaVO> listVillage12 = mapVillage.get(sysAreaVO.getAreaCode());
                listVillage12 = listVillage12 != null ? listVillage12 : new ArrayList<SysAreaVO>();
                listVillage12.add(sysAreaVO);
                mapVillage.put(sysAreaVO.getAreaCode(), listVillage12);
            }
        }
        if (StringUtils.isEmpty(paraCode)) {
            for (SysAreaVO sysAreaVO : list2) {
                List<SysAreaVO> listVillage = mapVillage.get(sysAreaVO.getAreaCode());
                if (listVillage != null && listVillage.size() > 0) {
                    sysAreaVOList.add(sysAreaVO);
                }
            }
        } else {
            SysAreaVO paraSysAreaVO = new SysAreaVO();
            SysArea sysArea = sysAreaMapper.queryByCode(paraCode);
            BeanUtil.copyProperties(sysArea, paraSysAreaVO);
            sysAreaVOList.add(paraSysAreaVO);
        }
        for (SysAreaVO sysAreaVO : sysAreaVOList) {
            List<SysAreaVO> listVillage = mapVillage.get(sysAreaVO.getAreaCode());
            if (listVillage != null && listVillage.size() > 0) {
                List<SysAreaVO> son = getSonVillage(sysAreaVO.getAreaCode(), map, mapVillage);
                sysAreaVO.setSonList(son);
            }
        }
        return sysAreaVOList;
    }

    public List<SysAreaVO> getSonVillage(String paraCode, Map<String, List<SysAreaVO>> map, Map<String, List<SysAreaVO>> mapVillage) {
        List<SysAreaVO> reList = new ArrayList<SysAreaVO>();
        List<SysAreaVO> sysAreaVOS = map.get(paraCode);
        if (sysAreaVOS != null && sysAreaVOS.size() > 0) {
            for (SysAreaVO sysAreaVO : sysAreaVOS) {
                List<SysAreaVO> listVillage = mapVillage.get(sysAreaVO.getAreaCode());
                if (listVillage != null && listVillage.size() > 0) {
                    List<SysAreaVO> son = getSonVillage(sysAreaVO.getAreaCode(), map, mapVillage);
                    sysAreaVO.setSonList(son);
                    reList.add(sysAreaVO);
                }
            }
        }
        return reList;
    }


    /**
     * 查询区域(所有层级)
     *
     * @return 对象列表
     */
    @Override
    public List<SysAreaVO> queryAllByCode(String paraCode, Integer len) {
        List<SysAreaVO> sysAreaVOList = new ArrayList<SysAreaVO>();
        Map<String, List<SysAreaVO>> map = new HashMap<String, List<SysAreaVO>>();
        List<SysAreaVO> list2 = new ArrayList<SysAreaVO>();
        List<SysAreaVO> sysAreaList = this.sysAreaMapper.queryAllByCode(paraCode);
        for (SysAreaVO sysAreaVO : sysAreaList) {
            if (sysAreaVO.getAreaCode().length() == 2) {
                list2.add(sysAreaVO);
            } else if (sysAreaVO.getAreaCode().length() == 4) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 2));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 2), list);
            } else if (sysAreaVO.getAreaCode().length() == 6) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 4));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 4), list);
            } else if (sysAreaVO.getAreaCode().length() == 9) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 6));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 6), list);
            } else if (sysAreaVO.getAreaCode().length() == 12) {
                List<SysAreaVO> list = map.get(sysAreaVO.getAreaCode().substring(0, 9));
                list = list != null ? list : new ArrayList<SysAreaVO>();
                list.add(sysAreaVO);
                map.put(sysAreaVO.getAreaCode().substring(0, 9), list);
            }
        }
        if (StringUtils.isEmpty(paraCode)) {
            sysAreaVOList = list2;
        } else {
            SysAreaVO paraSysAreaVO = new SysAreaVO();
            SysArea sysArea = sysAreaMapper.queryByCode(paraCode);
            BeanUtil.copyProperties(sysArea, paraSysAreaVO);
            sysAreaVOList.add(paraSysAreaVO);
        }
        for (SysAreaVO sysAreaVO : sysAreaVOList) {
            List<SysAreaVO> son = getSon(sysAreaVO.getAreaCode(), map, len);
            sysAreaVO.setSonList(son);
        }
        return sysAreaVOList;
    }

    public List<SysAreaVO> getSon(String paraCode, Map<String, List<SysAreaVO>> map, Integer len) {
        if (paraCode.length() >= len.intValue()) {
            return null;
        }
        List<SysAreaVO> sysAreaVOS = map.get(paraCode);
        if (sysAreaVOS != null && sysAreaVOS.size() > 0) {
            for (SysAreaVO sysAreaVO : sysAreaVOS) {
                List<SysAreaVO> son = getSon(sysAreaVO.getAreaCode(), map, len);
                sysAreaVO.setSonList(son);
            }
        }
        return sysAreaVOS;
    }

    /**
     * 查询区域代码查看全区域名称
     *
     * @return 对象列表
     */
    @Override
    public SysFullAreaVO getFullAreaNameByAreaCode(String areaCode) {
        if (areaCode.length() > 1) {
            SysFullAreaVO sysFullAreaVO = this.sysAreaMapper.getFullAreaNameByAreaCode(areaCode);
            return sysFullAreaVO;
        } else {
            return null;
        }
    }

    /**
     * 查询区域代码查看全区域名称(拼接字符串)
     *
     * @return 对象列表
     */
    @Override
    public String getFullAreaName(String areaCode) {
        String areaName = "";
        if (areaCode.length() > 1) {
            SysFullAreaVO sysFullAreaVO = this.sysAreaMapper.getFullAreaNameByAreaCode(areaCode);
            if (ObjectUtil.isNotEmpty(sysFullAreaVO)) {
                if (!StringUtils.isEmpty(sysFullAreaVO.getProvince())) {
                    areaName += sysFullAreaVO.getProvince();
                }
                if (!StringUtils.isEmpty(sysFullAreaVO.getCity())) {
                    areaName += sysFullAreaVO.getCity();
                }
                if (!StringUtils.isEmpty(sysFullAreaVO.getCounty())) {
                    areaName += sysFullAreaVO.getCounty();
                }
                if (!StringUtils.isEmpty(sysFullAreaVO.getStreet())) {
                    areaName += sysFullAreaVO.getStreet();
                }
                if (!StringUtils.isEmpty(sysFullAreaVO.getVillage())) {
                    areaName += sysFullAreaVO.getVillage();
                }
                return areaName;
            }
        }
        return areaName;
    }

    /**
     * 查询区域分层机
     *
     * @return 对象列表
     */
    @Override
    public List<SysAreaVO> queryListSelfAndSon(String paraCode, Integer maxLen) {
        SysAreaVO paraSysAreaVO = new SysAreaVO();
        SysArea sysArea = sysAreaMapper.queryByCode(paraCode);
        BeanUtil.copyProperties(sysArea, paraSysAreaVO);
        List<SysAreaVO> sysAreaVOList = new ArrayList<SysAreaVO>();
        sysAreaVOList.add(paraSysAreaVO);
        if (sysAreaVOList != null && sysAreaVOList.size() > 0) {
            for (SysAreaVO sysAreaVO : sysAreaVOList) {
                List<SysAreaVO> sonList1 = queryAreaListAndSon(sysAreaVO.getAreaCode(), maxLen);
                sysAreaVO.setSonList(sonList1);
            }
        }
        return sysAreaVOList;
    }

    /**
     * 村查询-村/社区排序
     *
     * @return 对象列表
     */
    @Override
    public List<SysArea> queryVillageList(String paraCode) {
        return sysAreaMapper.queryVillageList(paraCode);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.sysAreaMapper.deleteById(id) > 0;
    }

    /**
     * 通过区域名称查询单条数据
     *
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @return 实例对象
     */
    @Override
    public SysArea queryByAreaName(String provinceName, String cityName, String areaName) {
        return this.sysAreaMapper.queryByAreaName(provinceName, cityName, areaName);
    }

    /**
     * 通过区域名称查询单条数据
     *
     * @param paraCode 区域代码（越长越准确）
     * @param areaName 区
     * @return 实例对象
     */
    @Override
    public SysArea queryByParaAndName(String paraCode, String areaName, Integer areaLevel) {
        return this.sysAreaMapper.queryByParaAndName(paraCode, areaName, areaLevel);
    }

    @Override
    public Result insert(SysArea pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        SysAreaLimitVO vo = new SysAreaLimitVO();
        Map<String, SysArea> manageMap = queryAreaMap(vo);
        if (!StringUtils.isEmpty(pojo.getAreaCode())) {
            SysArea sysArea = queryByCode(pojo.getAreaCode());
            if (sysArea != null) {
                pojo.setAreaName(sysArea.getAreaName());
            }
        }
        if (pojo.getCityClassifyCode() != null) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.CITY_CLASSIFY_CODE, pojo.getCityClassifyCode());
            if (sysDataDict != null) {
                pojo.setCityClassifyName(sysDataDict.getDictName());
            }
        }
        if (ObjectUtil.isEmpty(pojo.getId())) {
            String areaCode = pojo.getAreaCode();
            String areaName = pojo.getAreaName();
            if (ObjectUtil.isNotEmpty(areaCode) && ObjectUtil.isNotEmpty(areaName)) {
                SysArea sysArea = manageMap.get(areaCode + "@" + areaName);
                if (ObjectUtil.isNotEmpty(sysArea)) {
                    return Result.failed("该区域已存在!");
                }
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            long newId = idWorker.nextId();
            pojo.setId(String.valueOf(newId));
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            save(pojo);
        } else {
            LambdaQueryChainWrapper<SysArea> lambdaQuery = lambdaQuery();
            lambdaQuery.eq(SysArea::getDelState, ConstantsUtil.IS_DONT_DEL);
            lambdaQuery.ne(SysArea::getId, pojo.getId());
            lambdaQuery.eq(SysArea::getAreaName, pojo.getAreaName());
            lambdaQuery.eq(SysArea::getAreaCode, pojo.getAreaCode());
            List<SysArea> list = lambdaQuery.orderByAsc(SysArea::getId).list();
            if (list.size() > 0) {
                return Result.failed("该区域已存在!");
            }
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public PageInfo<SysArea> queryAllByLimitNew(SysAreaLimitVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<SysArea> list = queryAll(vo);
        PageInfo<SysArea> pageInfo = new PageInfo<SysArea>(list);
        return pageInfo;
    }

    @Override
    public List<SysArea> queryAll(SysAreaLimitVO vo) {
        LambdaQueryChainWrapper<SysArea> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SysArea::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getState())) {
            lambdaQuery.eq(SysArea::getState, vo.getState());
        }
        if (ObjectUtil.isNotEmpty(vo.getCityClassifyCode())) {
            lambdaQuery.eq(SysArea::getCityClassifyCode, vo.getCityClassifyCode());
        }
        if (!StringUtils.isEmpty(vo.getFindStr())) {
            lambdaQuery.and((wrapper) -> {
                wrapper.like(SysArea::getAreaName, vo.getFindStr()).or().like(SysArea::getAreaCode, vo.getFindStr());
            });
        }
        List<SysArea> roomList = lambdaQuery.orderByAsc(SysArea::getSort).orderByAsc(SysArea::getAreaCode).list();
        return roomList;
    }

    @Override
    public Map<String, SysArea> queryAreaMap(SysAreaLimitVO vo) {
        Map<String, SysArea> map = new HashMap<>();
        List<SysArea> list = queryAll(vo);
        for (SysArea tabBaseAreaManage : list) {
            String key = tabBaseAreaManage.getAreaCode() + "@" + tabBaseAreaManage.getAreaName();
            map.put(key, tabBaseAreaManage);
        }
        return map;
    }
}