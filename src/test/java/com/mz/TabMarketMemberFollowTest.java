package com.mz;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.mz.common.context.PageInfo;
import com.mz.common.util.DateUtil;
import com.mz.common.util.PasswordUtil;
import com.mz.configuration.weixin.WeixinNoticeUtil;
import com.mz.mapper.localhost.TabGardenBaseMapper;
import com.mz.mapper.localhost.TabMarketMemberMapper;
import com.mz.model.market.TabMarketMember;
import com.mz.model.market.vo.TabMarketMemberVO;
import com.mz.model.supply.vo.TabGoodsSupplyVO;
import com.mz.service.garden.TabGardenBaseService;
import com.mz.service.market.TabMarketMemberService;
import com.mz.service.supply.TabGoodsSupplyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FutureRuralApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TabMarketMemberFollowTest {
    @Resource
    private TabMarketMemberMapper tabMarketMemberMapper;
    @Resource
    private TabMarketMemberService tabMarketMemberService;
    @Autowired
    private TabGardenBaseService tabGardenBaseService;
    @Autowired
    private TabGardenBaseMapper tabGardenBaseMapper;
    @Autowired
    private TabGoodsSupplyService tabGoodsSupplyService;
    @Autowired
    private WeixinNoticeUtil weixinNoticeUtil;


    @Test
    public void testMybatisPlus() {
//        tabGardenBaseService.queryById(1529388372307476480l);
//        TabGardenBaseVO tabGardenBaseVO = new TabGardenBaseVO();
//        tabGardenBaseVO.setPageNo(2);
//        tabGardenBaseVO.setPageSize(10);
//        PageInfo<TabGardenBase> tabGardenBasePageInfo = tabGardenBaseService.queryAllByLimit(tabGardenBaseVO);

//        tabGardenBaseMapper.selectById(1529388372307476480l);
//        List<TabGardenBase> tabGardenBases = tabGardenBaseMapper.selectList(null);
//        System.out.println(tabGardenBases);

        try {
            PageInfo<TabGoodsSupplyVO> tabGoodsSupplyVOPageInfo = tabGoodsSupplyService.queryLimitByWechat(3, 5, "1478277449404907520", null,null, "1", null, null, null, null, null, null, null, null, null, null);
            System.out.println(JSON.toJSON(tabGoodsSupplyVOPageInfo));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    //    @Test
    @Test
    public void test() {
        List<TabMarketMember> tabMarketMembers = tabMarketMemberMapper.queryAll(new TabMarketMember());
        for (TabMarketMember tabMarketMember : tabMarketMembers) {
            TabMarketMember updateTabMarketMember = new TabMarketMember();
            updateTabMarketMember.setId(tabMarketMember.getId());
            try {
                String memberPhone = tabMarketMember.getMemberPhone();
                if (!StringUtils.isEmpty(memberPhone)) {
                    String pwdSalt = PasswordUtil.salt();
                           String password = PasswordUtil.md5(memberPhone.substring(5));
                        String newPwdMD5 = PasswordUtil.md5(password.substring(0, 3) + pwdSalt + password.substring(3));
                        updateTabMarketMember.setPassword(newPwdMD5);
                        updateTabMarketMember.setPwdSalt(pwdSalt);
                        tabMarketMemberMapper.update(updateTabMarketMember);

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Test
    public void testMembeName() {
        try {
            TabMarketMemberVO tabMarketMemberVO = tabMarketMemberService.queryByOpenId("oi7lx5ZL7jn4T7o28fLlIV2kUT0M", "1478277449404907520", null);
            System.out.println(tabMarketMemberVO.getMemberName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWX() {
//        String code = "083wREFa1StwiD03EkIa1bcwg03wREFI";
//
//        String weixinOpenId = "", unionid = "";
//        String url = Constants.OAUTH2_URL.replace("APPID", Constants.APP_ID_WX).replace("SECRET", Constants.APP_SECRET_WX).replace("CODE", code);
//        log.info("url---------" + url);
//        String urlRet = cn.hutool.http.HttpUtil.get(url);
//        log.info("===============urlRet : " + urlRet);
//        com.alibaba.fastjson.JSONObject objectjson = com.alibaba.fastjson.JSONObject.parseObject(urlRet);
//        String openId = objectjson.get("openid").toString();
//        log.info("openId---------" + openId);
//
//        JSONObject jsonObject = JSONUtil.parseObj(urlRet);
//        log.info(jsonObject.toString());

//        String access_token = WeixinUtil.getAccessTokenApplet(Constants.APP_ID_WX, Constants.APP_SECRET_WX);
//        List<String> userOpenidList = new ArrayList<String>();
//        List<String> weixinSubscribedUserList = WeixinUtil.getWeixinSubscribedUsers(access_token, null, userOpenidList);
//        for (String openid : weixinSubscribedUserList) {
//            log.info("===============urlRet : "  + openid);
//        }


        String js_code = "013jR71w3JxGCY2DOp2w3fBQjy3jR716";
        String iv = "pRzk36fy0NCS2pS7REHL8g==";
        String encryptedData = "E5CHl5m6XMWw6jLH2mQdnUIGxYz2+Ct62HqscnycAWAJOh8ABoGBPkdJcpxTA/SyAnWNPjdXbcDT0rKhJiKZ0Xy+x9X08ThTD21pUTog9hV45svXvIyC58Y7JI7wAY5t/K53PwtQHiVU2ztdL4baaNnGR5QX7emqmlxKd4NUJlwcIEtNsqfcBiS2zKyRY0JIcsjtai09vGtMH+wNPXxT8BiHSB2iBcJN41R0SnpBe112f922qlZ5t8fdmUEx+pX+UH/vqg+EiJEQbIk0qByEUBxszUgUZNgH4170LI4GskH1CPTtKUcFNiBmwpCjSO94N4Wri9FMVfclH1s/n5kdsC3V/NZ1SxTaVENQ6nS3IkZNcyJkmgWbyHF97Nkcl8XV2S3vl4Cn0fVh/yWQ3kEPKny6fMU6qNz95mmzbSIQMsomH/KxrJa1xSmctSSfPV1G";
//        JSONObject session_keyJsonObject = WXAppletUserInfo.getSessionKeyOropenid(Constants.APP_ID_APPLET, Constants.APP_SECRET_APPLET, code);
//        String session_key = (String) session_keyJsonObject.get("session_key");
//        String openid = (String) session_keyJsonObject.get("openid");
//        JSONObject jsonObject = WXAppletUserInfo.getUserInfo(encryptedData, session_key, iv);
//        Map<String, Object> wxAppletUserInfo = WeixinUtil.getWXAppletUserInfo(encryptedData, iv, js_code, Constants.APP_ID_APPLET, Constants.APP_SECRET_APPLET);


//        String appId = "wx3a8bdd56f81e5dff";
//        String appSecret = "729a9e0a6693a464907ca70e8eddd9f1";
//        String result = cn.hutool.http.HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
//        JSONObject jsonObject = JSONUtil.parseObj(result);
//        log.info(jsonObject.toString());
    }

    @Test
    public void wxMpSend() {
        try {
            String pagepath = "/shareGarden/notice/notice?msgId=1&busId=2&busName=浇水";
            JSONObject data = new JSONObject();

            JSONObject first = new JSONObject();
//            first.put("value", "禹上稻园");
//            first.put("color", "#173177");
//            data.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "浇水服务");
            keyword1.put("color", "#173177");
            data.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", DateUtil.getStringDate(new Date(), DateUtil.DATE_FORMAT_YMD));
            keyword2.put("color", "#173177");
            data.put("keyword2", keyword2);

            JSONObject remark = new JSONObject();
            remark.put("value", "温馨提示，");
            remark.put("color", "#173177");
            data.put("remark", remark);

            String appId = "wx3a8bdd56f81e5dff";
            String appIdWx ="wxc858f15e13603e4d";
            String appSecretWx ="98df5f5ee375e3ba570ca52b827ce1ac";

            weixinNoticeUtil.sendMsg("ogR0B550yidrzrns7rQm64opfZwo",  appId, appIdWx, appSecretWx,"q7uhJB5KYYxBlMLl6IfVU4yPCsl4c-jFMsn09BNJV1w", pagepath, data);
        } catch (Exception e) {
            log.error("微信公众号推送异常---{}s", e.getMessage());
        }
    }

}