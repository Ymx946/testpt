package com.mz.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 * <p>
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
@Component //此处注解不能省却（0）
public class AliSmsUtil {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI5tPwatTyRyVNiW9bX25j";
    static final String accessKeySecret = "MxJQEWpckq3nIpfO1ARKgB7pjuO9IC";
    /**
     * 党建模板编码---待确定
     */
    public static Map<String, String> templateCodeMap = new HashMap<String, String>();//模板代码
    public static Map<String, String> msgErrCodeMap = new HashMap<String, String>();//短信发送失败代码
    private static AliSmsUtil aliSmsUtil;

    static {
        templateCodeMap = new HashMap<String, String>();
        templateCodeMap.put("1", "SMS_229465040");//        1	党组织派人谈话
        templateCodeMap.put("2", "SMS_229470100");//        2	推荐和确定入党积极分子
        templateCodeMap.put("3", "SMS_229465098");//        3	上级党委备案
        templateCodeMap.put("4", "SMS_229480100");//        4	培养教育考察培养联系人
        templateCodeMap.put("5", "SMS_229096838");//        5	培养教育考察支部--待定
        templateCodeMap.put("6", "SMS_229470110");//        6	确定发展对象
        templateCodeMap.put("7", "SMS_229465106");//        7	报上级党委备案
        templateCodeMap.put("8", "SMS_229465107");//        8	进行政治审查
        templateCodeMap.put("9", "SMS_229480104");//        9	开展集中培训
        templateCodeMap.put("10", "SMS_229475102");//        10	支部大会讨论接收预备党员
        templateCodeMap.put("11", "SMS_229465110");//        11	上级党委审批
        templateCodeMap.put("12", "SMS_229465112");//        12	入党誓言
        templateCodeMap.put("13", "SMS_229465114");//        13	提出转正申请
        templateCodeMap.put("14", "SMS_229480108");//        14	支部大会讨论
        templateCodeMap.put("15", "SMS_229096838");//        15	上级党委审批--待定

        templateCodeMap.put("SZXC_DY_SR", "SMS_263060021");//        SZXC_DY_SR	党员生日祝福
        templateCodeMap.put("SZXC_CM_SR", "SMS_263035010");//        SZXC_CM_SR	 村民生日祝福

        msgErrCodeMap.put("MOBILE_IN_BLACK", "手机号在黑名单（平台或运营商）");//            MOBILE_IN_BLACK	手机号在黑名单（平台或运营商）
        msgErrCodeMap.put("USER_REJECT", "用户手机退订此业务、产品未开通");//            USER_REJECT	用户手机退订此业务、产品未开通
        msgErrCodeMap.put("SP_UNKNOWN_ERROR", "运营商未知错误");//            SP_UNKNOWN_ERROR	运营商未知错误
        msgErrCodeMap.put("MOBILE_NOT_ON_SERVICE", "停机、空号、暂停服务、关机、不在服务区");//            MOBILE_NOT_ON_SERVICE	停机、空号、暂停服务、关机、不在服务区
        msgErrCodeMap.put("MOBLLE_TERMINAL_ERROR", "手机终端问题、内存满、SIM卡满、非法设备等");//            MOBLLE_TERMINAL_ERROR	手机终端问题、内存满、SIM卡满、非法设备等
        msgErrCodeMap.put("MOBILE_ACCOUNT_ABNORMAL", "用户账户异常、携号转网、欠费等");//            MOBILE_ACCOUNT_ABNORMAL	用户账户异常、携号转网、欠费等
        msgErrCodeMap.put("CONTENT_KEYWORD", "内容关键字拦截");//            CONTENT_KEYWORD	内容关键字拦截
        msgErrCodeMap.put("SIGNATURE_BLACKLIST", "签名黑名单");//            SIGNATURE_BLACKLIST	签名黑名单
        msgErrCodeMap.put("INVALID_NUMBER", "号码不合法");//            INVALID_NUMBER	号码不合法
        msgErrCodeMap.put("NO_ROUTE", "无路由器");//            NO_ROUTE	无路由器
        msgErrCodeMap.put("CONTENT_ERROR", "模板内容无退订");//            CONTENT_ERROR	模板内容无退订
    }

    /**
     * 单条发送
     *
     * @param mobiles      手机号
     * @param signName     签名
     * @param templateCode 模板代码
     * @throws Exception
     */
    public static SendSmsResponse sendSms(String mobiles, String signName, String templateCode, String dataOne, String dataTwo, String dataThree, String dataFour) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobiles);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为


//        商家：
//        接收新订单：您有新的酒店订单，请及时登录系统确定订单！   SMS_134311299
//
//    消费者：
//        订单确认通知：尊敬的宾客，您‘4/22’预订入住‘XXXXXX酒店’订单已确认（酒店地址：‘荆州市沙市区100号’T：‘0716-4126555’）SMS_134316597
//
//        取消确认通知：尊敬的宾客，我们非常抱歉的通知您，订单‘XXXXXX’已取消。（如有疑问请拨打‘0716-4126555’）  SMS_134316275

        request.setTemplateParam("{\"dataone\":\"" + dataOne + "\",\"datatwo\":\"" + dataTwo + "\",\"datathree\":\"" + dataThree + "\",\"datafour\":\"" + dataFour + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
//        request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");//暂时没用

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 单条发送
     *
     * @param content      发送内容
     * @param signName     签名
     * @param templateCode 模板代码
     * @throws Exception
     */
    public static void sendMessage(String signName, String templateCode, String userName, String shopsId, String content, String mobile, String data1, String data2, String data3, String data4) throws Exception {
        SendSmsResponse response = sendSms(mobile, signName, templateCode, data1, data2, data3, data4);

        if (response.getCode() != null && response.getCode().equals("OK")) {
            //保存发送记录

	        	/*BizSmsLog entity = new BizSmsLog();
	        	entity.setPhone(mobile);
	        	entity.setContent(content);
	        	entity.setShopsId(shopsId);
	        	aliSmsUtil.bizSmsLogService.saveBizSmsLog(entity);*/
        } else {
            System.out.println("短信发送失败信息：" + response.getMessage());
        }
    }


    public static SendSmsResponse sendSmses(String mobiles, String signName, String templateCode, String dataone) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobiles);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为


//        商家：
//        接收新订单：您有新的酒店订单，请及时登录系统确定订单！   SMS_134311299
//
//    消费者：
//        订单确认通知：尊敬的宾客，您‘4/22’预订入住‘XXXXXX酒店’订单已确认（酒店地址：‘荆州市沙市区100号’T：‘0716-4126555’）SMS_134316597
//
//        取消确认通知：尊敬的宾客，我们非常抱歉的通知您，订单‘XXXXXX’已取消。（如有疑问请拨打‘0716-4126555’）  SMS_134316275

        request.setTemplateParam("{\"code\":\"" + dataone + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
//        request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");//暂时没用

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 群发短信demo
     *
     * @return
     * @throws ClientException
     */
    public static SendBatchSmsResponse sendBatchSms() throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumberJson("[\"15088652027\",\"15088652027\"]");
        //必填:短信签名-支持不同的号码发送不同的短信签名
        request.setSignNameJson("[\"芒种数字\",\"芒种数字\"]");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_215735521");
        //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParamJson("[{\"code\":\"456123\"},{\"code\":\"354321\"}]");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCodeJson("[\"90997\",\"90998\"]");
        //请求失败这里会抛ClientException异常
        SendBatchSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
        }
        return sendSmsResponse;
    }

    /**
     * 群发短信传参数
     *
     * @return
     * @throws ClientException
     */
    public static SendBatchSmsResponse sendBatchSmsTemplate(String mobiles, String signNames, String templateCode, String data) throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumberJson(mobiles);
        //必填:短信签名-支持不同的号码发送不同的短信签名
        request.setSignNameJson(signNames);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParamJson(data);
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCodeJson("[\"90997\",\"90998\"]");
        //请求失败这里会抛ClientException异常
        SendBatchSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
        }
        return sendSmsResponse;
    }

    /**
     * 群发短信传参数
     *
     * @return
     * @throws ClientException
     */
    public static QuerySendDetailsResponse querySendDetails(String phoneNumber, String bizId, String sendDate, Long pageSize, Long currentPage) throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-调用发送短信接口时返回的BizId
        request.setBizId(bizId);
        //必填-短信发送的日期支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
        request.setSendDate(sendDate);
        //必填-页大小
        request.setPageSize(pageSize);
        //必填-当前页码从1开始计数
        request.setCurrentPage(currentPage);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        //获取返回结果
        if (querySendDetailsResponse.getCode() != null && querySendDetailsResponse.getCode().equals("OK")) {
            //代表请求成功
        }
        return querySendDetailsResponse;
    }

    //测试
    public static void main(String[] args) {

        //批量发送短信 测试demo
        try {
            //"[\"15088652027\",\"15088652027\"]"
            //"[\"芒种数字\",\"芒种数字\"]"
            //"[{\"code\":\"456123\"},{\"code\":\"354321\"}]"
//            SendBatchSmsResponse sendSmsResponse = sendBatchSmsTemplate("[\"15088652027\",\"15958160227\"]", "[\"芒种数字\",\"芒种数字\"]", "SMS_229096838", "[{\"name\":\"王猛\"},{\"name\":\"李全\"}]");
//            SendBatchSmsResponse sendSmsResponse = sendBatchSmsTemplate("[\"13675848786\"]", "[\""+ ConstantsUtil.SMS_SIGN+"\"]", "SMS_247885236", "");
//            SendBatchSmsResponse sendSmsResponse = sendBatchSmsTemplate("[\"159581602\"]", "[\"" + ConstantsUtil.SMS_SIGN + "\"]", "SMS_247885236", "[{\"name\":\"徐凯\"}]");
//            System.out.println("短信接口返回的数据----------------");
//            System.out.println("Code=" + sendSmsResponse.getCode());
//            System.out.println("Message=" + sendSmsResponse.getMessage());
//            System.out.println("RequestId=" + sendSmsResponse.getRequestId());
//            System.out.println("BizId=" + sendSmsResponse.getBizId());
//            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
//                System.out.println("成功");
//            }

//            //延迟查询发送结果
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails("159581602277", "", DateUtil.dateFormat(new Date(), "yyyyMMdd"), 1L, 1L);
            System.out.println("查询返回结果----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            System.out.println("smsSendDetailDTOs=" + querySendDetailsResponse.getSmsSendDetailDTOs());
            List<QuerySendDetailsResponse.SmsSendDetailDTO> smsSendDetailDTOList = querySendDetailsResponse.getSmsSendDetailDTOs();
            for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : smsSendDetailDTOList) {
                System.out.println("sendStatus=" + smsSendDetailDTO.getSendStatus());//1：等待回执，2：发送失败，3：发送成功。
                System.out.println("errCode=" + smsSendDetailDTO.getErrCode());//1：等待回执，2：发送失败，3：发送成功。
                System.out.println("errMsg=" + msgErrCodeMap.get(smsSendDetailDTO.getErrCode()));//1：等待回执，2：发送失败，3：发送成功。
            }
        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //批量发送短信 测试demo
//    	try {
//			SendBatchSmsResponse sendSmsResponse=sendBatchSms();
//            System.out.println("短信接口返回的数据----------------");
//            System.out.println("Code=" + sendSmsResponse.getCode());
//            System.out.println("Message=" + sendSmsResponse.getMessage());
//            System.out.println("RequestId=" + sendSmsResponse.getRequestId());
//            System.out.println("BizId=" + sendSmsResponse.getBizId());
//			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
//		    	System.out.println("成功");
//		    	}
//		} catch (ClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}
