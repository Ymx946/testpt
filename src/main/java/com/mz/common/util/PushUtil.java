package com.mz.common.util;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.mz.common.util.zjzw.entity.TabUniAppPush;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class PushUtil {
    //    数字燕崖APP
//    AppID：SoHxUxSQp361WmD98bQ4Y6  --app_id
//    AppKey：NeeOuLUidU8TF45nf5eKA7 --app_secret
//    AppSecret：ESmI05PvyL6utg9hrTCd35  --无
//    MasterSecret：DjJccObZEB6jnGcvrKFjN9 --merchant_key
    // 数字燕崖APP（测试）
//    AppID：c6zjuxDUxc5ECfEh2Xnvo8
//    AppKey：8FCxRg0DDU6wnQRLIvIFQ9
//    AppSecret：NnU6JRk15A89tHsY4VfQO4
//    MasterSecret：8c71zxGYOFAXsEk8xJOls2
//    private static String url = "https://sdk.open.api.igexin.com/apiex.htm";// https---有S
    private static final String url = "https://api.getui.com/apiex.htm";//

    /**
     * 消息推送(所有)
     *
     * @param title 通知栏标题
     * @param text  通知栏内容
     * @return ok-成功
     */
    public static String pushMsg(String title, String text, String appId, String appKey, String masterSecret) throws IOException {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        Style0 style = new Style0();
        // STEP2：设置推送标题、推送内容
        style.setTitle(title);//"请输入通知栏标题"
        style.setText(text);//"请输入通知栏内容"
        style.setLogo("push.png");  // 设置推送图标（好像没啥用）
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动

        // STEP4：选择通知模板
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(style);

        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒

        // STEP6：执行推送
        IPushResult ret = push.pushMessageToApp(message);
//        成功：  {result=ok, contentId=OSA-0427_5RlGsCI1NqAqrqr5Sl8JL3}
        //失败:   {result=RepeatedContent}
        System.out.println("--------result--------------" + ret.getResponse().get("result"));
        System.out.println("--------contentId--------------" + ret.getResponse().get("contentId"));
        return (String) ret.getResponse().get("result");
    }

    /**
     * 纯透传模板
     * 客户端集成SDK设置监听后，会收到透传消息，客户端可以自己灵活的选择处理方式
     */
    public static ITemplate buildTransmissionTemplate(String msg, String appId, String appKey) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(msg);
        template.setTransmissionType(1); // 这个Type为int型，填写1则自动启动app
        return template;
    }

    /**
     * 设置推送标题、推送内容
     */
    public static ITemplate buildNotificationTemplate(String title, String text, String appId, String appKey) {
        Style0 style = new Style0();
        // STEP2：设置推送标题、推送内容
        style.setTitle(title);//"请输入通知栏标题"
        style.setText(text);//"请输入通知栏内容"
        style.setLogo("push.png");  // 设置推送图标（好像没啥用）
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动

        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(style);
        return template;
    }

    /**
     * 批量推送（指定ClientId）
     */
    public static void pushToList(List<String> cids, String title, String text, String appId, String appKey, String masterSecret) {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        // System.setProperty("gexin_pushList_needAliasDetails", "true");
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        // 透传模板
//        ITemplate template = buildTransmissionTemplate(text);
        ITemplate template = buildNotificationTemplate(title, text, appId, appKey);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>();
        Target target = null;
        for (String cid : cids) {
            target = new Target();
            target.setAppId(appId);
            target.setClientId(cid);
            targets.add(target);
            // target.setAlias(Alias1);
        }
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message, "任务别名_toApp");
        // String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 批量推送（指定ClientId）
     */
    public static void uniAppPush(List<String> cids, String title, String content) {
        TabUniAppPush tabUniAppPush = new TabUniAppPush();
        tabUniAppPush.setCids(cids);
        tabUniAppPush.setTitle(title);
        tabUniAppPush.setContent(content);
        RestTemplate template = new RestTemplate();
        String url = "https://fc-mp-0c650d26-7456-4691-9325-1619b1e17b13.next.bspapp.com/test";
        try {
            String result = template.postForObject(url,tabUniAppPush,String.class);
            //System.out.println("result:" + result);
        }catch (Exception e) {
            log.error("推送失败"+e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        String appId = "c6zjuxDUxc5ECfEh2Xnvo8";
        String appKey = "8FCxRg0DDU6wnQRLIvIFQ9";
        String masterSecret = "8c71zxGYOFAXsEk8xJOls2";
        String title = "会议提推送测试4444";
        String text = "会议内容4444：批量推送测试内容20210601六一儿童节222";
//        pushMsg(title,text);
        List<String> cids = new ArrayList<String>();
        cids.add("ce91e05b07a5f383dfe0fe1eba92d87d");
        cids.add("6a104c293603e2757c61208fd17c0323");
        pushToList(cids, title, text, appId, appKey, masterSecret);
//        {result=NoValidPush, details={"84e37726cd14ae4858c43df7f34ee3ff":"appid_notmatch"}}
    }
}
