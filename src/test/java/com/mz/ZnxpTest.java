package com.mz;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.DateUtil;
import com.mz.common.util.PasswordUtil;
import com.mz.common.util.URIEncoder;
import com.mz.common.util.wxaes.HttpKit;
import com.mz.controller.fengqiao.znxp.oldinfo.Constants;
import com.mz.controller.fengqiao.znxp.oldinfo.util.ZnzpUtil;
import com.mz.model.fengqiao.znxp.TabZnxpOldinfo;
import com.mz.model.fengqiao.znxp.ZnxpOldinfo;
import com.mz.service.device.TabPublicDeviceService;
import com.mz.service.fengqiao.znxp.oldInfo.TabZnxpOldinfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FutureRuralApplication.class, TabPublicDeviceService.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZnxpTest {
    @Resource
    private TabZnxpOldinfoService tabZnxpOldinfoService;

    public static void main(String[] args) {
        String secret_account = "mangzhong";
        String secret = "ab003d5616594de492c35c3696fd0e9a";

        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("rows", 10);
        String data = JSON.toJSONString(map);
        log.info("=== data：" + URIEncoder.encodeURIComponent(data));

        String timestamp = DateUtil.getNowStringYMDHMSDate();
        log.info("=== timestamp：" + timestamp);
        String sign = PasswordUtil.md5(secret + data + timestamp + secret).toUpperCase();
        log.info("=== sign：" + sign);
    }

    @Test
    public void testOldInfo() {
        try {
            String retStr = HttpKit.post(Constants.DOMAIN_URL + Constants.OLDINFO_LIST_URL, ZnzpUtil.getParamMap(new HashMap<String, Object>() {{
                put("page", 1);
                put("rows", 1000);
            }}));
            log.info("retStr：" + retStr);
            Map retMap = JSON.parseObject(retStr, Map.class);
            if (!Boolean.TRUE.equals(retMap.get("success"))) {
                log.info("获取老人列表失败" + retMap.get("msg"));
            }

            List<ZnxpOldinfo> oldInfos = JSON.parseArray(retMap.get("obj") + "", ZnxpOldinfo.class);
            for (ZnxpOldinfo znxpOldinfo : oldInfos) {
                TabZnxpOldinfo tabZnxpOldinfo = new TabZnxpOldinfo();
                BeanUtils.copyProperties(znxpOldinfo, tabZnxpOldinfo);

                tabZnxpOldinfo.setOldId(znxpOldinfo.getOld_id());
                tabZnxpOldinfo.setXpNo(znxpOldinfo.getXp_no());
                tabZnxpOldinfo.setHealthStatus(znxpOldinfo.getHealth_status());
                tabZnxpOldinfo.setIsZl(znxpOldinfo.getIs_zl());
                tabZnxpOldinfo.setIsPbxp(znxpOldinfo.getIs_pbxp());

                String idcard = znxpOldinfo.getIdcard();
                if (StringUtil.isNotEmpty(idcard)) {
                    tabZnxpOldinfo.setAge(IdcardUtil.getAgeByIdCard(idcard + ""));
                    tabZnxpOldinfo.setSex(IdcardUtil.getGenderByIdCard(idcard + "") == 1 ? 1 : 2);
                }

                List<TabZnxpOldinfo> tabZnxpOldinfos = tabZnxpOldinfoService.lambdaQuery()
                        .eq(TabZnxpOldinfo::getTenantId, ConstantsUtil.FQ_TENANTID)
                        .eq(TabZnxpOldinfo::getSiteId, ConstantsUtil.FQ_SITEID)
                        .eq(TabZnxpOldinfo::getMobile, znxpOldinfo.getMobile())
                        .eq(TabZnxpOldinfo::getDelState, ConstantsUtil.IS_DONT_DEL)
                        .list();
                if (CollectionUtils.isEmpty(tabZnxpOldinfos)) {
                    tabZnxpOldinfo.setTenantId(ConstantsUtil.FQ_TENANTID);
                    tabZnxpOldinfo.setSiteId(ConstantsUtil.FQ_SITEID);
                    tabZnxpOldinfo.setDelState(ConstantsUtil.IS_DONT_DEL);
                    tabZnxpOldinfo.setState(ConstantsUtil.STATE_NORMAL);
                    tabZnxpOldinfo.setCreateTime(new Date());
                    tabZnxpOldinfoService.save(tabZnxpOldinfo);
                } else {
                    tabZnxpOldinfo.setOldId(tabZnxpOldinfos.get(0).getOldId());
                    tabZnxpOldinfo.setModifyTime(new Date());
                    tabZnxpOldinfoService.updateById(tabZnxpOldinfo);
                }
            }
        } catch (Exception e) {
            log.error("同步智能胸牌老人数据失败{}", e);
        }
    }

}
