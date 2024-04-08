package com.mz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.controller.fengqiao.lafei.Constants;
import com.mz.controller.fengqiao.lafei.entity.LafeiAccountFlow;
import com.mz.controller.fengqiao.lafei.entity.LafeiAccountRank;
import com.mz.controller.fengqiao.lafei.util.LafeiIntegralUtil;
import com.mz.model.fengqiao.lafei.TabLafeiAccountFlow;
import com.mz.model.fengqiao.lafei.TabLafeiAccountRank;
import com.mz.service.fengqiao.lafei.TabLafeiAccountFlowService;
import com.mz.service.fengqiao.lafei.TabLafeiAccountRankService;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FutureRuralApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FengQiaoTest {

    @Resource
    private TabLafeiAccountFlowService tabLafeiAccountFlowService;

    @Resource
    private TabLafeiAccountRankService tabLafeiAccountRankService;


    @Test
    public void testLafeiAccount() {
        // testLafeiAccountRank();
//        testLafeiAccountFlow();
    }

    @Test
    public void testLafeiAccountFlow() {
        Constants.areaCodeOrganMap.forEach((k, v) -> {
            String[] areaCodeName = Constants.areaCodeOrganMap.get(k).split("@");
            List<TabLafeiAccountRank> tabLafeiAccountRanks = tabLafeiAccountRankService.lambdaQuery()
                    .eq(TabLafeiAccountRank::getDelState, ConstantsUtil.IS_DONT_DEL)
                    .eq(TabLafeiAccountRank::getState, ConstantsUtil.STATE_NORMAL)
                    .eq(TabLafeiAccountRank::getOrganNbr, areaCodeName[0])
                    .eq(TabLafeiAccountRank::getAreaCode, k)
                    .list();
            for (TabLafeiAccountRank tabLafeiAccountRank : tabLafeiAccountRanks) {
                String organNbr = tabLafeiAccountRank.getOrganNbr();
                String areaCode = tabLafeiAccountRank.getAreaCode();
                String areaName = tabLafeiAccountRank.getAreaName();

                String accountFlow = LafeiIntegralUtil.getAccountFlowList(organNbr, tabLafeiAccountRank.getUserNbr());
                if (StringUtils.isNotBlank(accountFlow)) {
                    Map<String, Long> tabLafeiAccountFlowMap = tabLafeiAccountFlowService.lambdaQuery()
                            .eq(TabLafeiAccountFlow::getDelState, ConstantsUtil.IS_DONT_DEL)
                            .eq(TabLafeiAccountFlow::getState, ConstantsUtil.STATE_NORMAL)
                            .isNull(TabLafeiAccountFlow::getModifyUser)
                            .list()
                            .parallelStream().collect(Collectors.toMap(TabLafeiAccountFlow::getNbr, TabLafeiAccountFlow::getId, (p1, p2) -> p2));

                    List<TabLafeiAccountFlow> addLafeiAccountFlowList = new ArrayList<>();
                    List<TabLafeiAccountFlow> updateLafeiAccountFlowList = new ArrayList<>();

                    List<LafeiAccountFlow> lafeiAccountFlows = JSON.parseArray(accountFlow, LafeiAccountFlow.class);
                    IdWorker idWorker = new IdWorker(0, 0);
                    for (LafeiAccountFlow lafeiAccountFlow : lafeiAccountFlows) {
                        String nbr = lafeiAccountFlow.getNbr();
                        if (tabLafeiAccountFlowMap.containsKey(nbr)) {
                            TabLafeiAccountFlow updateTabLafeiAccountFlow = new TabLafeiAccountFlow();
                            BeanUtil.copyProperties(lafeiAccountFlow, updateTabLafeiAccountFlow, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                            updateTabLafeiAccountFlow.setId(tabLafeiAccountFlowMap.get(nbr));
                            updateTabLafeiAccountFlow.setOrganNbr(organNbr);
                            updateTabLafeiAccountFlow.setAreaCode(areaCode);
                            updateTabLafeiAccountFlow.setAreaName(areaName);
                            updateTabLafeiAccountFlow.setModifyTime(new Date());
                            updateTabLafeiAccountFlow.setModifyUser(ConstantsUtil.FQ_USERNAME);
                            updateLafeiAccountFlowList.add(updateTabLafeiAccountFlow);
                        } else {
                            TabLafeiAccountFlow addTabLafeiAccountFlow = new TabLafeiAccountFlow();
                            BeanUtil.copyProperties(lafeiAccountFlow, addTabLafeiAccountFlow, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                            addTabLafeiAccountFlow.setId(idWorker.nextId());
                            addTabLafeiAccountFlow.setOrganNbr(organNbr);
                            addTabLafeiAccountFlow.setAreaCode(areaCode);
                            addTabLafeiAccountFlow.setAreaName(areaName);
                            addTabLafeiAccountFlow.setTenantId(ConstantsUtil.FQ_TENANTID);
                            addTabLafeiAccountFlow.setSiteId(ConstantsUtil.FQ_SITEID);
                            addTabLafeiAccountFlow.setDelState(ConstantsUtil.IS_DONT_DEL);
                            addTabLafeiAccountFlow.setState(ConstantsUtil.STATE_NORMAL);
                            addTabLafeiAccountFlow.setCreateUser(ConstantsUtil.FQ_USERNAME);
                            addLafeiAccountFlowList.add(addTabLafeiAccountFlow);
                        }
                    }
                    if (CollectionUtil.isNotEmpty(addLafeiAccountFlowList)) {
                        tabLafeiAccountFlowService.saveBatch(addLafeiAccountFlowList, ConstantsUtil.BATCH_COMMIT_NUM);
                    }
                    if (CollectionUtil.isNotEmpty(updateLafeiAccountFlowList)) {
                        tabLafeiAccountFlowService.updateBatchById(updateLafeiAccountFlowList, ConstantsUtil.BATCH_COMMIT_NUM);
                    }
                }
            }
        });
    }

    @Test
    public void testLafeiAccountRank() {
        Constants.areaCodeOrganMap.forEach((k, v) -> {
            String[] areaCodeName = Constants.areaCodeOrganMap.get(k).split("@");
            String organNbr = areaCodeName[0];
            String areaName = areaCodeName[1];
            String accountRank = LafeiIntegralUtil.getAccountRank(organNbr, "");
            if (StringUtils.isNotBlank(accountRank)) {
                Map<String, Long> tabLafeiAccountRankMap = tabLafeiAccountRankService.lambdaQuery()
                        .eq(TabLafeiAccountRank::getDelState, ConstantsUtil.IS_DONT_DEL)
                        .eq(TabLafeiAccountRank::getState, ConstantsUtil.STATE_NORMAL)
                        .eq(TabLafeiAccountRank::getOrganNbr, areaCodeName[0])
                        .eq(TabLafeiAccountRank::getAreaCode, k)
                        .list()
                        .parallelStream().collect(Collectors.toMap(TabLafeiAccountRank::getUserNbr, TabLafeiAccountRank::getId, (p1, p2) -> p2));

                List<TabLafeiAccountRank> addLafeiAccountRankList = new ArrayList<>();
                List<TabLafeiAccountRank> updateLafeiAccountRankList = new ArrayList<>();

                List<LafeiAccountRank> lafeiAccountRanks = JSON.parseArray(accountRank, LafeiAccountRank.class);
                IdWorker idWorker = new IdWorker(0, 0);
                for (LafeiAccountRank lafeiAccountRank : lafeiAccountRanks) {
                    String userNbr = lafeiAccountRank.getUserNbr();
                    if (tabLafeiAccountRankMap.containsKey(userNbr)) {
                        TabLafeiAccountRank updateTabLafeiAccountRank = new TabLafeiAccountRank();
                        BeanUtil.copyProperties(lafeiAccountRank, updateTabLafeiAccountRank, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                        updateTabLafeiAccountRank.setId(tabLafeiAccountRankMap.get(userNbr));
                        updateTabLafeiAccountRank.setOrganNbr(organNbr);
                        updateTabLafeiAccountRank.setAreaCode(k);
                        updateTabLafeiAccountRank.setAreaName(areaName);
                        updateTabLafeiAccountRank.setRankNum(lafeiAccountRank.getRank());
                        updateTabLafeiAccountRank.setModifyTime(new Date());
                        updateTabLafeiAccountRank.setModifyUser(ConstantsUtil.FQ_USERNAME);
                        updateLafeiAccountRankList.add(updateTabLafeiAccountRank);
                    } else {
                        TabLafeiAccountRank addTabLafeiAccountRank = new TabLafeiAccountRank();
                        BeanUtil.copyProperties(lafeiAccountRank, addTabLafeiAccountRank, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                        addTabLafeiAccountRank.setId(idWorker.nextId());
                        addTabLafeiAccountRank.setRankNum(lafeiAccountRank.getRank());
                        addTabLafeiAccountRank.setOrganNbr(organNbr);
                        addTabLafeiAccountRank.setAreaCode(k);
                        addTabLafeiAccountRank.setAreaName(areaName);
                        addTabLafeiAccountRank.setTenantId(ConstantsUtil.FQ_TENANTID);
                        addTabLafeiAccountRank.setSiteId(ConstantsUtil.FQ_SITEID);
                        addTabLafeiAccountRank.setDelState(ConstantsUtil.IS_DONT_DEL);
                        addTabLafeiAccountRank.setState(ConstantsUtil.STATE_NORMAL);
                        addTabLafeiAccountRank.setCreateTime(new Date());
                        addTabLafeiAccountRank.setCreateUser(ConstantsUtil.FQ_USERNAME);
                        addLafeiAccountRankList.add(addTabLafeiAccountRank);
                    }
                }
                if (CollectionUtil.isNotEmpty(addLafeiAccountRankList)) {
                    tabLafeiAccountRankService.saveBatch(addLafeiAccountRankList);
                }
                if (CollectionUtil.isNotEmpty(updateLafeiAccountRankList)) {
                    tabLafeiAccountRankService.updateBatchById(updateLafeiAccountRankList);
                }
            }
        });
    }
}
