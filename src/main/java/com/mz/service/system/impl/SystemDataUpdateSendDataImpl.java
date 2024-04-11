package com.mz.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.mz.common.util.StringUtils;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.system.SystemDataUpdateSendDataMapper;
import com.mz.model.base.*;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.SystemDataUpdateSendData;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.model.system.vo.SystemDataUpdateSendDataVO;
import com.mz.service.base.SysDataDictService;
import com.mz.service.base.SysDeftService;
import com.mz.service.base.SysNodeService;
import com.mz.service.base.TabBasicMoveAppService;
import com.mz.service.system.SystemDataUpdateSendDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 版本更新记录下发数据表(SystemDataUpdateSendData)表服务实现类
 *
 * @author makejava
 * @since 2024-04-11 17:07:56
 */
@Service("systemDataUpdateSendDataService")
public class SystemDataUpdateSendDataImpl extends ServiceImpl<SystemDataUpdateSendDataMapper, SystemDataUpdateSendData> implements SystemDataUpdateSendDataService {
    @Autowired
    private RedisUtil myRedisUtil;
    @Autowired
    private SysDataDictService sysDataDictService;
    @Autowired
    private SysDeftService sysDeftService;
    @Autowired
    private TabBasicMoveAppService tabBasicMoveAppService;
    @Autowired
    private SysNodeService sysNodeService;

    @Override
    public SystemDataUpdateSendData insert(SystemDataUpdateSendData pojo, String loginID){
        String baseUserStr = myRedisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if(pojo.getId()==null){
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);
            save(pojo);
        }else{
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public List<SystemDataUpdateSendData> queryAll(SystemDataUpdateSendDataVO vo) {
        LambdaQueryChainWrapper<SystemDataUpdateSendData> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SystemDataUpdateSendData::getDelState, ConstantsUtil.IS_DONT_DEL);
        if(ObjectUtil.isNotEmpty(vo.getRecordId())){
            lambdaQuery.eq(SystemDataUpdateSendData::getRecordId,vo.getRecordId());
        }
        List<SystemDataUpdateSendData> list = lambdaQuery.orderByDesc(SystemDataUpdateSendData::getCreateTime).list();
        return list;
    }

    @Override
    public void batchInsert(SystemDataUpdateRecord systemDataUpdateRecord, String jsonStr) {
        if (!org.apache.commons.lang3.StringUtils.isEmpty(jsonStr)) {
            LambdaQueryChainWrapper<SystemDataUpdateSendData> lambdaQuery = lambdaQuery();
            lambdaQuery.eq(SystemDataUpdateSendData::getDelState, ConstantsUtil.IS_DONT_DEL);
            lambdaQuery.eq(SystemDataUpdateSendData::getRecordId, systemDataUpdateRecord.getId());
            List<SystemDataUpdateSendData> tabExamineScoringPersonList = lambdaQuery.list();
            removeBatchByIds(tabExamineScoringPersonList);
            List<SystemDataUpdateSendData> addlist = JSONUtil.toList(jsonStr, SystemDataUpdateSendData.class);
            if (CollectionUtil.isNotEmpty(addlist)) {//判断是否有数据
                IdWorker idWorker = new IdWorker(0L, 0L);
                for (SystemDataUpdateSendData pojo : addlist) {
                    pojo.setId(idWorker.nextId());
                    pojo.setRecordId(systemDataUpdateRecord.getId());
                    if (ObjectUtil.isNotEmpty(pojo.getSendDataTypeCode())) {
                        SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.UPDATE_SEND_DATA_TYPE_CODE, pojo.getSendDataTypeCode());
                        if (sysDataDict != null) {
                            pojo.setSendDataTypeName(sysDataDict.getDictName());
                        }
                        if (ObjectUtil.isNotEmpty(pojo.getSendDataId())) {
                            Long sendDataId = pojo.getSendDataId();
                            if (pojo.getSendDataTypeCode().equals("1")) {
                                SysDataDict sysDataDict1 = sysDataDictService.queryById(String.valueOf(sendDataId));
                                if (ObjectUtil.isNotEmpty(sysDataDict1) && ObjectUtil.isNotEmpty(sysDataDict1.getDictName())) {
                                    pojo.setSendDataName(sysDataDict1.getDictName());
                                }
                            }
                            if (pojo.getSendDataTypeCode().equals("2")) {
                                SysDeft sysDeft = sysDeftService.queryById(String.valueOf(sendDataId));
                                if (ObjectUtil.isNotEmpty(sysDeft) && ObjectUtil.isNotEmpty(sysDeft.getSysName())) {
                                    pojo.setSendDataName(sysDeft.getSysName());
                                }
                            }
                            if (pojo.getSendDataTypeCode().equals("3")) {
                                TabBasicMoveApp moveApp = tabBasicMoveAppService.getById(sendDataId);
                                if (ObjectUtil.isNotEmpty(moveApp) && ObjectUtil.isNotEmpty(moveApp.getAppName())) {
                                    pojo.setSendDataName(moveApp.getAppName());
                                }
                            }
                            if (pojo.getSendDataTypeCode().equals("4")) {
                                SysNode sysNode = sysNodeService.getById(sendDataId);
                                if (ObjectUtil.isNotEmpty(sysNode) && ObjectUtil.isNotEmpty(sysNode.getNodeName())) {
                                    pojo.setSendDataName(sysNode.getNodeName());
                                }
                            }
                        }
                    }
                    pojo.setCreateUser(systemDataUpdateRecord.getModifyUser());
                    pojo.setCreateTime(DateUtil.now());
                    pojo.setModifyUser(systemDataUpdateRecord.getModifyUser());
                    pojo.setModifyTime(systemDataUpdateRecord.getModifyTime());
                    pojo.setState(ConstantsUtil.STATE_NORMAL);
                    pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
                    pojo.setRemarks(null);
                }
                saveBatch(addlist);
            }
        }
    }
}
