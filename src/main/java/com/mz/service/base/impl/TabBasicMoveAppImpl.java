package com.mz.service.base.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.TabBasicMoveAppMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.SysDeft;

import com.mz.model.base.TabBasicMoveApp;
import com.mz.model.base.vo.TabBasicMoveAppVO;
import com.mz.service.base.SysDataDictService;
import com.mz.service.base.SysDeftService;
import com.mz.service.base.TabBasicMoveAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 基础移动应用表(TabBasicMoveApp)表服务实现类
 *
 * @author makejava
 * @since 2022-12-29 09:22:41
 */
@Service("tabBasicMoveAppService")
public class TabBasicMoveAppImpl extends ServiceImpl<TabBasicMoveAppMapper, TabBasicMoveApp> implements TabBasicMoveAppService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysDeftService sysDeftService;
    @Autowired
    private SysDataDictService sysDataDictService;
    @Autowired
    private TabBasicMoveAppMapper tabBasicMoveAppMapper;

    @Override
    public TabBasicMoveApp insert(TabBasicMoveApp pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (!StringUtils.isEmpty(pojo.getPcSysCode())) {
            SysDeft sysDeft = sysDeftService.queryByCode(pojo.getPcSysCode());
            if (sysDeft != null) {
                pojo.setPcSysName(sysDeft.getSysName());
            }
        }
        if (!StringUtils.isEmpty(pojo.getSortCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.WECHAT_NODE_SORT_TYPE_CODE, pojo.getSortCode());
            if (sysDataDict != null) {
                pojo.setSortName(sysDataDict.getDictName());
            }
        }
        if (!StringUtils.isEmpty(pojo.getUseTypeCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.WECHAT_NODE_USE_TYPE_CODE, pojo.getUseTypeCode());
            if (sysDataDict != null) {
                pojo.setUseTypeName(sysDataDict.getDictName());
            }
        }
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreatUser(baseUser.getRealName());
            pojo.setCreatTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用

            save(pojo);
        } else {
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public PageInfo<TabBasicMoveApp> queryAllByLimit(TabBasicMoveAppVO vo) {
        Integer[] bannerObjArr = null;
        String bannerObjS = vo.getBannerObj();
        if (!StringUtils.isEmpty(bannerObjS)) {
            String[] bannerObjStrArr = bannerObjS.split(",");
            if (bannerObjStrArr != null && bannerObjStrArr.length > 0) {
                bannerObjArr = new Integer[bannerObjStrArr.length];
                int i = 0;
                for (String tagCode : bannerObjStrArr) {
                    bannerObjArr[i] = Integer.valueOf(tagCode.trim());
                    i++;
                }
            }
        }
        vo.setBannerObjArr(bannerObjArr);
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<TabBasicMoveApp> roomList = tabBasicMoveAppMapper.queryAll(vo);
        PageInfo<TabBasicMoveApp> tabScreenTemplateSceneVOPageInfo = new PageInfo<TabBasicMoveApp>(roomList);
        return tabScreenTemplateSceneVOPageInfo;
    }
}
