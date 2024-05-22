package com.mz.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gexin.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.common.util.StringUtils;
import com.mz.common.util.wxaes.HttpKit;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.system.SystemDataUpdateRecordMapper;
import com.mz.model.base.*;
import com.mz.model.base.vo.SysNodeVo;
import com.mz.model.base.vo.TabBasicMoveAppVO;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.SystemDataUpdateSendData;
import com.mz.model.system.model.SystemDataUpdateRecordModel;
import com.mz.model.system.vo.SelectSearchVO;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.model.system.vo.SystemDataUpdateSendDataVO;
import com.mz.service.base.SysDataDictService;
import com.mz.service.base.SysDeftService;
import com.mz.service.base.SysNodeService;
import com.mz.service.base.TabBasicMoveAppService;
import com.mz.service.system.SystemDataServiceNodeService;
import com.mz.service.system.SystemDataUpdateRecordService;
import com.mz.service.system.SystemDataUpdateSendDataService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@Service("systemDataUpdateRecordService")
public class SystemDataUpdateRecordImpl extends ServiceImpl<SystemDataUpdateRecordMapper, SystemDataUpdateRecord> implements SystemDataUpdateRecordService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SystemDataServiceNodeService systemDataServiceNodeService;
    @Autowired
    private SysDataDictService sysDataDictService;
    @Autowired
    private SystemDataUpdateSendDataService systemDataUpdateSendDataService;
    @Autowired
    private SysDeftService sysDeftService;
    @Autowired
    private TabBasicMoveAppService tabBasicMoveAppService;
    @Autowired
    private SysNodeService sysNodeService;

    @Override
    public SystemDataUpdateRecord insert(SystemDataUpdateRecord pojo, String loginID,String jsonStr){
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        pojo.setModifyUser(baseUser.getRealName());
        pojo.setModifyTime(DateUtil.now());
        SystemDataServiceNode serviceNode = systemDataServiceNodeService.getById(pojo.getNodeId());
        if(ObjectUtil.isNotEmpty(serviceNode)){
            pojo.setNodeName(serviceNode.getNodeName());
        }
        if (ObjectUtil.isNotEmpty(pojo.getSystemCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.SYSTEM_TYPE_CODE, pojo.getSystemCode());
            if (sysDataDict != null) {
                pojo.setSystemName(sysDataDict.getDictName());
            }
        }
        if(pojo.getId()==null){
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(0);
            save(pojo);
        }else{
            updateById(pojo);
        }
        if(ObjectUtil.isNotEmpty(jsonStr)){
            systemDataUpdateSendDataService.batchInsert(pojo, jsonStr);
        }
        return pojo;
    }

    public PageInfo<SystemDataUpdateRecord> queryAllByLimit(SystemDataUpdateRecordVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<SystemDataUpdateRecord> list = queryAll(vo);
        PageInfo<SystemDataUpdateRecord> pageInfo = new PageInfo<SystemDataUpdateRecord>(list);
        return pageInfo;
    }

    @Override
    public List<SystemDataUpdateRecord> queryAll(SystemDataUpdateRecordVO vo) {
        LambdaQueryChainWrapper<SystemDataUpdateRecord> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SystemDataUpdateRecord::getDelState, ConstantsUtil.IS_DONT_DEL);
        if(vo.getNodeId()!=null){
            lambdaQuery.eq(SystemDataUpdateRecord::getNodeId,vo.getNodeId());
        }
        if(!StringUtils.isEmpty(vo.getCreateTimeS())){
            lambdaQuery.ge(SystemDataUpdateRecord::getCreateTime,vo.getCreateTimeS());
        }
        if(!StringUtils.isEmpty(vo.getCreateTimeE())){
            lambdaQuery.le(SystemDataUpdateRecord::getCreateTime,vo.getCreateTimeE()+" 23:59:59");
        }
        if(ObjectUtil.isNotEmpty(vo.getSystemCode())){
            lambdaQuery.eq(SystemDataUpdateRecord::getSystemCode,vo.getSystemCode());
        }
        if(!StringUtils.isEmpty(vo.getVersionNo())){
            lambdaQuery.like(SystemDataUpdateRecord::getVersionNo,vo.getVersionNo());
        }
        if(ObjectUtil.isNotEmpty(vo.getState())){
            lambdaQuery.eq(SystemDataUpdateRecord::getState,vo.getState());
        }
        if(!StringUtils.isEmpty(vo.getUpdateContent())){
            lambdaQuery.like(SystemDataUpdateRecord::getUpdateContent,vo.getUpdateContent());
        }
        List<SystemDataUpdateRecord> list = lambdaQuery.orderByDesc(SystemDataUpdateRecord::getCreateTime).list();
        return list;
    }

    @Override
    public SystemDataUpdateRecordModel queryById(Long id) {
        SystemDataUpdateRecordModel model = null;
        SystemDataUpdateRecord record = getById(id);
        if (ObjectUtil.isNotEmpty(record)){
            model = new SystemDataUpdateRecordModel();
            BeanUtils.copyProperties(record,model);
            SystemDataUpdateSendDataVO vo = new SystemDataUpdateSendDataVO();
            vo.setRecordId(id);
            List<SystemDataUpdateSendData> dataList = systemDataUpdateSendDataService.queryAll(vo);
            if(CollectionUtil.isNotEmpty(dataList)){
                model.setSystemDataUpdateSendDataList(dataList);
            }
        }
        return model;
    }

    @Override
    public Result queryBusForSelect(SelectSearchVO vo) {
        Integer pageNo = vo.getPageNo();
        Integer pageSize = vo.getPageSize();
        String areaCode = vo.getAreaCode();
        String startTime = vo.getStartTime();
        String endTime = vo.getEndTime();
        String name = vo.getName();
        Integer state = vo.getState();
        Integer sysType = vo.getSysType();
        Integer belongType = vo.getBelongType();
        Integer nodeType = vo.getNodeType();
        if (vo.getType().intValue() == 1) {//1-数据字典
            PageInfo<SysDataDict> pageInfo = sysDataDictService.queryAllByLimits(pageNo, pageSize, areaCode, null, null, name, state,startTime,endTime);
            return Result.success(pageInfo);
        } else if (vo.getType().intValue() == 2) {//2-PC应用市场
            PageInfo<SysDeft> pageInfo = sysDeftService.queryAllByLimit(pageNo, pageSize, name, sysType, belongType, startTime, endTime);
            return Result.success(pageInfo);
        } else if (vo.getType().intValue() == 3) {//3-移动应用市场
            TabBasicMoveAppVO find = new TabBasicMoveAppVO();
            find.setPageNo(pageNo);
            find.setPageSize(pageSize);
            find.setAppName(name);
            find.setAppType(vo.getAppType());
            find.setBannerObj(vo.getBannerObj());
            find.setStartTime(startTime);
            find.setEndTime(endTime);
            PageInfo<TabBasicMoveApp> pageInfo = tabBasicMoveAppService.queryAllByLimit(find);
            return Result.success(pageInfo);
        } else if (vo.getType().intValue() == 4) {//4-节点管理
            SysNodeVo find = new SysNodeVo();
            find.setPageNo(pageNo);
            find.setPageSize(pageSize);
            find.setNodeName(name);
            find.setNodeType(nodeType);
            find.setStartTime(startTime);
            find.setEndTime(endTime);
            find.setUseState(state);
            PageInfo<SysNode> pageInfo = sysNodeService.queryAllOrderByTime(find);
            return Result.success(pageInfo);
        }
        return Result.success();
    }

    @SneakyThrows
    @Override
    public Integer issued(Long id) {
        SystemDataUpdateRecord dataUpdateRecord = getById(id);
        Integer reissuedState = 0;
        if(ObjectUtil.isNotEmpty(dataUpdateRecord)){
            if(ObjectUtil.isNotEmpty(dataUpdateRecord.getNodeId())){
                SystemDataServiceNode systemDataServiceNode = systemDataServiceNodeService.getById(dataUpdateRecord.getNodeId());
                if(ObjectUtil.isNotEmpty(systemDataServiceNode)){
                    if(ObjectUtil.isNotEmpty(systemDataServiceNode.getNodeUrl())){
                        String url = "https://"+systemDataServiceNode.getNodeUrl()+"/future-rural";
                        String versionUrl = url+"/tabBaseVersions/receipt";
                        String dataDictUrl = url+"/sysDataDict/receipt";
                        String deftUrl = url+"/sysDeft/receipt";
                        String moveAppUrl = url+"/tabBasicMoveApp/receipt";
                        String nodeUrl = url+"/sysNode/receipt";
                        List<SysDataDict> dataDictList = new ArrayList<>();
                        List<SysDeft> sysDeftList = new ArrayList<>();
                        List<TabBasicMoveApp> moveAppList = new ArrayList<>();
                        List<SysNode> sysNodeList = new ArrayList<>();
                        SystemDataUpdateRecordModel model = queryById(id);
                        if(ObjectUtil.isNotEmpty(model)){
                            List<SystemDataUpdateSendData> systemDataUpdateSendDataList = model.getSystemDataUpdateSendDataList();
                            if(CollectionUtil.isNotEmpty(systemDataUpdateSendDataList)){
                                for (SystemDataUpdateSendData systemDataUpdateSendData : systemDataUpdateSendDataList) {
                                    if(ObjectUtil.isNotEmpty(systemDataUpdateSendData.getSendDataTypeCode()) && ObjectUtil.isNotEmpty(systemDataUpdateSendData.getSendDataId())){
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("1")){
                                            SysDataDict sysDataDict = sysDataDictService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysDataDict)){
                                                dataDictList.add(sysDataDict);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("2")){
                                            SysDeft sysDeft = sysDeftService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysDeft)){
                                                sysDeftList.add(sysDeft);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("3")){
                                            TabBasicMoveApp moveApp = tabBasicMoveAppService.getById(systemDataUpdateSendData.getSendDataId());
                                            if (ObjectUtil.isNotEmpty(moveApp)){
                                                moveAppList.add(moveApp);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("4")){
                                            SysNode sysNode = sysNodeService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysNode)){
                                                sysNode.setOperatingBooklet(null);
                                                sysNodeList.add(sysNode);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(CollectionUtil.isNotEmpty(dataDictList)){
                            Gson gson = new Gson();
                            String result = gson.toJson(dataDictList);
                            HttpKit.post(dataDictUrl, result);
                        }
                        if(CollectionUtil.isNotEmpty(sysDeftList)){
                            Gson gson = new Gson();
                            String result1 = gson.toJson(sysDeftList);
                            HttpKit.post(deftUrl, result1);
                        }
                        if(CollectionUtil.isNotEmpty(moveAppList)){
                            Gson gson = new Gson();
                            String result2 = gson.toJson(moveAppList);
                            HttpKit.post(moveAppUrl, result2);
                        }
                        if(CollectionUtil.isNotEmpty(sysNodeList)){
                            Gson gson = new Gson();
                            String result3 = gson.toJson(sysNodeList);
                            HttpKit.post(nodeUrl, result3);
                        }
                        TabBaseVersions versions = new TabBaseVersions();
                        versions.setId(dataUpdateRecord.getId());
                        versions.setVersionNumber(dataUpdateRecord.getVersionNo());
                        versions.setUpdateContent(dataUpdateRecord.getUpdateContent());
                        versions.setSystemCode(dataUpdateRecord.getSystemCode());
                        versions.setSystemName(dataUpdateRecord.getSystemName());
                        versions.setState(1);
                        versions.setDelState(1);
                        versions.setCreateTime(DateUtil.now());
                        versions.setCreateUser(dataUpdateRecord.getCreateUser());
                        versions.setModifyTime(DateUtil.now());
                        versions.setModifyUser(dataUpdateRecord.getModifyUser());
                        if (ObjectUtil.isNotEmpty(dataUpdateRecord.getRemarks())){
                            versions.setRemarks(dataUpdateRecord.getRemarks());
                        }
                        Gson gson = new Gson();
                        String obj1 = gson.toJson(versions);
                        String result = HttpKit.post(versionUrl, obj1);
                        if(result.contains("10000")){
                            JSONObject jsonObjectAuth = JSONObject.parseObject(result);
                            String returnCode = jsonObjectAuth.getString("code");
                            if (returnCode.equals("10000")) {//请求成功
                                dataUpdateRecord.setState(1);
                                reissuedState = 1;
                            }
                        }else {
                            dataUpdateRecord.setState(-1);
                            reissuedState = -1;
                        }
                        updateById(dataUpdateRecord);
                    }
                }
            }
        }
        return reissuedState;
    }

    @SneakyThrows
    @Override
    public Integer reissued(Long id) { //1-重新下发成功 -1 重新下发失败
        SystemDataUpdateRecord dataUpdateRecord = getById(id);
        Integer reissuedState = 0;
        if(ObjectUtil.isNotEmpty(dataUpdateRecord)){
            if(ObjectUtil.isNotEmpty(dataUpdateRecord.getNodeId())){
                SystemDataServiceNode systemDataServiceNode = systemDataServiceNodeService.getById(dataUpdateRecord.getNodeId());
                if(ObjectUtil.isNotEmpty(systemDataServiceNode)){
                    if(ObjectUtil.isNotEmpty(systemDataServiceNode.getNodeUrl())){
                        String url = "https://"+systemDataServiceNode.getNodeUrl()+"/future-rural";
                        String dataDictUrl = url+"/sysDataDict/receipt";
                        String deftUrl = url+"/sysDeft/receipt";
                        String moveAppUrl = url+"/tabBasicMoveApp/receipt";
                        String nodeUrl = url+"/sysNode/receipt";
                        List<SysDataDict> dataDictList = new ArrayList<>();
                        List<SysDeft> sysDeftList = new ArrayList<>();
                        List<TabBasicMoveApp> moveAppList = new ArrayList<>();
                        List<SysNode> sysNodeList = new ArrayList<>();
                        SystemDataUpdateRecordModel model = queryById(id);
                        if(ObjectUtil.isNotEmpty(model)){
                            List<SystemDataUpdateSendData> systemDataUpdateSendDataList = model.getSystemDataUpdateSendDataList();
                            if(CollectionUtil.isNotEmpty(systemDataUpdateSendDataList)){
                                for (SystemDataUpdateSendData systemDataUpdateSendData : systemDataUpdateSendDataList) {
                                    if(ObjectUtil.isNotEmpty(systemDataUpdateSendData.getSendDataTypeCode()) && ObjectUtil.isNotEmpty(systemDataUpdateSendData.getSendDataId())){
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("1")){
                                            SysDataDict sysDataDict = sysDataDictService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysDataDict)){
                                                dataDictList.add(sysDataDict);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("2")){
                                            SysDeft sysDeft = sysDeftService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysDeft)){
                                                sysDeftList.add(sysDeft);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("3")){
                                            TabBasicMoveApp moveApp = tabBasicMoveAppService.getById(systemDataUpdateSendData.getSendDataId());
                                            if (ObjectUtil.isNotEmpty(moveApp)){
                                                moveAppList.add(moveApp);
                                            }
                                        }
                                        if(systemDataUpdateSendData.getSendDataTypeCode().equals("4")){
                                            SysNode sysNode = sysNodeService.queryById(String.valueOf(systemDataUpdateSendData.getSendDataId()));
                                            if (ObjectUtil.isNotEmpty(sysNode)){
                                                sysNode.setOperatingBooklet(null);
                                                sysNodeList.add(sysNode);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(CollectionUtil.isNotEmpty(dataDictList)) {
                            Gson gson = new Gson();
                            String result = gson.toJson(dataDictList);
                            String post = HttpKit.post(dataDictUrl, result);
                            if (result.contains("10000")) {
                                JSONObject jsonObjectAuth = JSONObject.parseObject(post);
                                String returnCode = jsonObjectAuth.getString("code");
                                if (returnCode.equals("10000")) {//请求成功
                                    reissuedState = 1;
                                }
                            } else {
                                reissuedState = -1;
                            }
                        }
                        if(CollectionUtil.isNotEmpty(sysDeftList)){
                            Gson gson = new Gson();
                            String result1 = gson.toJson(sysDeftList);
                            HttpKit.post(deftUrl, result1);
                        }
                        if(CollectionUtil.isNotEmpty(moveAppList)){
                            Gson gson = new Gson();
                            String result2 = gson.toJson(moveAppList);
                            HttpKit.post(moveAppUrl, result2);
                        }
                        if(CollectionUtil.isNotEmpty(sysNodeList)){
                            Gson gson = new Gson();
                            String result3 = gson.toJson(sysNodeList);
                            HttpKit.post(nodeUrl, result3);
                        }
                    }
                }
            }
        }
        return reissuedState;
    }
}
