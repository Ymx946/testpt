package com.mz;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.DateUtil;
import com.mz.common.util.IdWorker;
import com.mz.controller.fengqiao.buffet.Constants;
import com.mz.controller.fengqiao.buffet.util.BuffetUtil;
import com.mz.model.fengqiao.buffet.TabFqBuffet;
import com.mz.service.device.TabPublicDeviceService;
import com.mz.service.fengqiao.buffet.TabFqBuffetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FutureRuralApplication.class, TabPublicDeviceService.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FqBuffetTest {
    @Autowired
    private TabFqBuffetService tabFqBuffetService;

    @Test
    public void testFqBuffet() {
        for (int i = 0; i < 10; i++) {
            String statisticsDate = DateUtil.dateFormat(DateUtil.dateAdd(DateUtil.DATE_INTERVAL_DAY, new Date(), -i), DateUtil.DATE_FORMAT_YMD);
            Constants.areaCodeOrganMap.forEach((k, v) -> {
                saveBuffet(k, 1, statisticsDate, statisticsDate, "prod");
                saveBuffet(k, 0, statisticsDate, statisticsDate, "prod");
            });
        }
    }

    private void saveBuffet(String areaCode, Integer orderStatus, String startDate, String endDate, String profile) {
        try {
            String[] liveComAry = Constants.areaCodeOrganMap.get(areaCode).split("@");
            String liveComCode = liveComAry[0];
            LambdaQueryChainWrapper<TabFqBuffet> tabFqBuffetLambdaQueryChainWrapper = tabFqBuffetService.lambdaQuery();
            Map<String, Long> tabFqBuffetMap = tabFqBuffetLambdaQueryChainWrapper
                    .eq(TabFqBuffet::getDelState, ConstantsUtil.IS_DONT_DEL)
                    .eq(TabFqBuffet::getState, ConstantsUtil.STATE_NORMAL)
                    .eq(TabFqBuffet::getTenantId, ConstantsUtil.FQ_TENANTID)
                    .eq(TabFqBuffet::getSiteId, ConstantsUtil.FQ_SITEID)
                    .eq(TabFqBuffet::getLiveComCode, liveComCode)
                    .list()
                    .parallelStream().collect(Collectors.toMap(TabFqBuffet::getMd5, TabFqBuffet::getId, (key1, key2) -> key2));

            List<TabFqBuffet> addFqBuffets = new ArrayList<>();
            List<TabFqBuffet> updateFqBuffets = new ArrayList<>();
            // 获取 订单状态（1：正常 、0：取消）
            String buffetData = BuffetUtil.getBuffetData(orderStatus + "", startDate, endDate, profile);
            List<TabFqBuffet> tabFqBuffets = JSON.parseArray(buffetData, TabFqBuffet.class);
            IdWorker idWorker = new IdWorker(0L, 0L);
            for (TabFqBuffet tabFqBuffet : tabFqBuffets) {
                if (liveComCode.equalsIgnoreCase(tabFqBuffet.getLiveComCode())) {
                    tabFqBuffet.setTenantId(ConstantsUtil.FQ_TENANTID);
                    tabFqBuffet.setSiteId(ConstantsUtil.FQ_SITEID);
                    tabFqBuffet.setLiveComCode(liveComCode);
                    tabFqBuffet.setLiveComTxt(liveComAry[1]);
                    tabFqBuffet.setOrderStatus(orderStatus);
                    String md5 = tabFqBuffet.fetchMd5();
                    tabFqBuffet.setMd5(md5);
                    if (tabFqBuffetMap.containsKey(md5)) {
                        tabFqBuffet.setId(tabFqBuffetMap.get(md5));
                        tabFqBuffet.setModifyTime(new Date());
                        updateFqBuffets.add(tabFqBuffet);
                    } else {
                        tabFqBuffet.setId(idWorker.nextId());
                        tabFqBuffet.setDelState(ConstantsUtil.IS_DONT_DEL);
                        tabFqBuffet.setState(ConstantsUtil.STATE_NORMAL);
                        tabFqBuffet.setCreateTime(new Date());
                        addFqBuffets.add(tabFqBuffet);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(addFqBuffets)) {
                tabFqBuffetService.saveBatch(addFqBuffets, 1000);
            }

            if (CollectionUtils.isNotEmpty(updateFqBuffets)) {
                tabFqBuffetService.updateBatchById(updateFqBuffets, 1000);
            }
        } catch (Exception e) {
            log.error("同步点餐失败{}", e);
        }
    }

}
