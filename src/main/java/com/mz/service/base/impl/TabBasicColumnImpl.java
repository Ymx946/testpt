package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.common.util.StringFormatUtil;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.TabBasicColumnMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.TabBasicColumn;
import com.mz.model.base.vo.TabBasicColumnVO;
import com.mz.service.base.SysDataDictService;
import com.mz.service.base.TabBasicColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 基础栏目表(TabBasicColumn)表服务实现类
 *
 * @author makejava
 * @since 2022-12-28 09:52:43
 */
@Service("tabBasicColumnService")
public class TabBasicColumnImpl extends ServiceImpl<TabBasicColumnMapper, TabBasicColumn> implements TabBasicColumnService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TabBasicColumnService tabBasicColumnService;
    @Autowired
    private SysDataDictService sysDataDictService;

    @Override
    public Result insert(TabBasicColumn pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        TabBasicColumnVO findTabPublicInfoColumn = new TabBasicColumnVO();
        findTabPublicInfoColumn.setId(pojo.getId()); //<!--id不等于，用于在修改时判断名称重复，其他不会有传ID的情况存在-->
        findTabPublicInfoColumn.setParaColumnCode(pojo.getParaColumnCode());
        findTabPublicInfoColumn.setColumnName(pojo.getColumnName());
        List<TabBasicColumn> list = queryAll(findTabPublicInfoColumn);
        if (list != null && list.size() > 0) {
            return Result.failed("栏目名称重复");
        }
        SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.XTLMGL_DATA_TYPE_CODE, String.valueOf(pojo.getColumnType()));
        if (sysDataDict != null) {
            pojo.setColumnTypeName(sysDataDict.getDictName());
        }
        pojo.setModifyUser(baseUser.getRealName());
        pojo.setModifyTime(DateUtil.now());
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreatUser(baseUser.getRealName());
            pojo.setCreatTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            pojo.setLastStage(1);
            pojo.setColumnLevel(1);
            pojo.setShowState(1);
            //自动编码
            findTabPublicInfoColumn = new TabBasicColumnVO();
            findTabPublicInfoColumn.setParaColumnCode(pojo.getParaColumnCode());
            List<TabBasicColumn> codeList = queryAll(findTabPublicInfoColumn);
            String paraColumnCode = pojo.getParaColumnCode();
            if (StringUtils.isEmpty(paraColumnCode)) {
                paraColumnCode = "01";//顶层代码以01开头
            }
            int no = 1;
            if (CollectionUtil.isNotEmpty(codeList)) {
                //如果已有栏目信息，则编码按原来栏目编码加1---获取原编码，获取序列码转int计算。
                no = Integer.valueOf(codeList.stream().findFirst().get().getColumnCode().substring(paraColumnCode.length())) + 1;
            }
            String newCode = paraColumnCode + StringFormatUtil.stringCompl(String.valueOf(no), 2);
            pojo.setColumnCode(newCode);
            //更新上级栏目是否末级
            if (!StringUtils.isEmpty(pojo.getParaColumnCode())) {
                //更新上级栏目是否末级
                TabBasicColumnVO vo = new TabBasicColumnVO();
                vo.setColumnCode(pojo.getParaColumnCode());
                TabBasicColumn para = queryByCode(vo);
                if (para != null) {
                    pojo.setColumnLevel(para.getColumnLevel().intValue() + 1);//根据上级栏目的级别，设置当前栏目的级别
                }
                pojo.setParaColumnName(para.getColumnName());
                if (para.getLastStage().intValue() == 1) {
                    para.setLastStage(2);
                    tabBasicColumnService.updateById(para);
                }
            }
            save(pojo);
        } else {
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public List<TabBasicColumn> queryAll(TabBasicColumnVO vo) {
        LambdaQueryChainWrapper<TabBasicColumn> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabBasicColumn::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabBasicColumn::getState, ConstantsUtil.STATE_NORMAL);
        if (vo.getId() != null) {
            lambdaQuery.ne(TabBasicColumn::getId, vo.getId()); //<!--id不等于，用于在修改时判断名称重复，其他不会有传ID的情况存在-->
        }
        if (!StringUtils.isEmpty(vo.getParaColumnCode())) {
            lambdaQuery.eq(TabBasicColumn::getParaColumnCode, vo.getParaColumnCode());
        } else {//没有传上级代码则默认查询顶层
            lambdaQuery.and((wrapper) -> {
                wrapper.isNull(TabBasicColumn::getParaColumnCode).or().eq(TabBasicColumn::getParaColumnCode, "");
            });
        }
        if (!StringUtils.isEmpty(vo.getColumnName())) {
            lambdaQuery.eq(TabBasicColumn::getColumnName, vo.getColumnName());
        }
        List<TabBasicColumn> list = lambdaQuery.orderByDesc(TabBasicColumn::getColumnCode).list();
        return list;
    }

    @Override
    public List<TabBasicColumn> querySonByType(TabBasicColumnVO vo) {
        LambdaQueryChainWrapper<TabBasicColumn> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabBasicColumn::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabBasicColumn::getState, ConstantsUtil.STATE_NORMAL);
        lambdaQuery.isNotNull(TabBasicColumn::getParaColumnCode);
        lambdaQuery.ne(TabBasicColumn::getParaColumnCode, "");
        if (vo.getColumnType() != null) {
            lambdaQuery.eq(TabBasicColumn::getColumnType, vo.getColumnType());
        }
        List<TabBasicColumn> list = lambdaQuery.orderByDesc(TabBasicColumn::getColumnCode).list();
        return list;
    }

    @Override
    public TabBasicColumn queryByCode(TabBasicColumnVO vo) {
        LambdaQueryChainWrapper<TabBasicColumn> lambdaQuery = lambdaQuery();
        if (!StringUtils.isEmpty(vo.getColumnCode())) {
            lambdaQuery.eq(TabBasicColumn::getColumnCode, vo.getColumnCode());
        }
        TabBasicColumn tabBasicColumn = lambdaQuery.orderByDesc(TabBasicColumn::getCreatTime).one();
        return tabBasicColumn;
    }

    @Override
    public PageInfo<TabBasicColumn> queryAllByLimit(TabBasicColumnVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        LambdaQueryChainWrapper<TabBasicColumn> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabBasicColumn::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getColumnType() != null) {
            lambdaQuery.eq(TabBasicColumn::getColumnType, vo.getColumnType());
        }
        if (vo.getPageType() != null) {
            lambdaQuery.eq(TabBasicColumn::getPageType, vo.getPageType());
        }
        if (!StringUtils.isEmpty(vo.getColumnName())) {
            lambdaQuery.like(TabBasicColumn::getColumnName, vo.getColumnName());
        }
        List<TabBasicColumn> roomList = lambdaQuery.orderByAsc(TabBasicColumn::getColumnCode).orderByAsc(TabBasicColumn::getColumnSort).list();
        PageInfo<TabBasicColumn> tabScreenTemplateSceneVOPageInfo = new PageInfo<TabBasicColumn>(roomList);
        return tabScreenTemplateSceneVOPageInfo;
    }
}
