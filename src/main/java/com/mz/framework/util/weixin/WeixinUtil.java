package com.mz.framework.util.weixin;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.util.StringUtil;
import com.mz.framework.util.weixin.json.JSONException;
import com.mz.framework.util.weixin.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信开发相关工具类
 *
 * @author yangh
 */
@Slf4j
public class WeixinUtil {
    public static String access_token = null;
    public static String time = null;
    public static String jsapi_ticket = null;
    public static String customer_info = null;

    /**
     * 获取接口访问凭证
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    private synchronized static String getAccess_token(String appid, String appsecret) {
        // 凭证获取(GET)
        String requestUrl = Constants.TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        String jsonObjectStr = jsonObject.toString();
//        log.info("===jsonObjectStr: " + jsonObjectStr);
        String token = null;
        if (jsonObject == null || StringUtil.isEmpty(jsonObjectStr) || jsonObjectStr.equals("[]") || !jsonObject.has("access_token")) {
            log.info("获取token失败, jsonObject为空");
            return null;
        }
        try {
            token = jsonObject.getString("access_token");
        } catch (JSONException e) {
            // 获取token失败
            log.error("获取token失败 :", e);
        }
        return token;
    }

    public synchronized static Map<String, String> getAccessToken(String appid, String appsecret, Date signtime, String accessToken) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("accessToken", "");
        resultMap.put("dateflag", "N");
        String accessTokenMap = accessToken;
        String dateflag = "N";
        if (StringUtils.isBlank(accessToken)) {
            accessTokenMap = getAccessToken(appid, appsecret);
            dateflag = "Y";
        } else {
            if (System.currentTimeMillis() - signtime.getTime() >= 60 * 60 * 1000) { // 每小时刷新一次
                accessTokenMap = getAccessToken(appid, appsecret);
                dateflag = "Y";
            }
        }
        resultMap.put("accessToken", accessTokenMap);
        log.info("=======accessToken=====" + accessToken);
        resultMap.put("dateflag", dateflag);
        return resultMap;
    }

    /**
     * 公众号统一请求
     * 不同的appid注意区别
     */
    public static String getAccessToken(String appid, String appsecret) {
        if (access_token == null) {
            access_token = getAccess_token(appid, appsecret);
            time = getTimeStr();
        } else {
            if (!time.substring(0, 13).equals(getTimeStr().substring(0, 13))) { // 每小时刷新一次
                access_token = null;
                access_token = getAccess_token(appid, appsecret);
                time = getTimeStr();
            }
        }
        return access_token;
    }

    /**
     * 获取已关注该公众号的客户列表
     *
     * @param ACCESS_TOKEN
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getWeixinSubscribedUsers(String ACCESS_TOKEN, String NEXTOPENID, List<String> userOpenidList) {
        if (null == userOpenidList || userOpenidList.size() == 0) {
            userOpenidList = new ArrayList<String>();
        }
        String requestUrl = Constants.SUBSCRIBED_USERS_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN);
        if (StringUtils.isNoneBlank(NEXTOPENID)) {
            requestUrl += "&next_openid=" + NEXTOPENID;
        }
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        if (null != jsonObject) {
            if (jsonObject.has("total") && jsonObject.has("count")) {
                int total = Integer.valueOf(jsonObject.get("total").toString());
                int count = Integer.valueOf(jsonObject.get("count").toString());
                if (total > 10000 && count == 10000) {
                    String nextOpenid = jsonObject.get("next_openid").toString();
                    // 再次调用获取已关注该公众号的用户openid
                    String dataStr = jsonObject.get("data").toString();
                    Map<String, Object> retMap = (Map<String, Object>) JSON.parse(dataStr);
                    List<String> list = JSONArray.parseArray(retMap.get("openid").toString(), String.class);
                    userOpenidList.addAll(list);
                    getWeixinSubscribedUsers(ACCESS_TOKEN, nextOpenid, userOpenidList);
                } else {
                    String dataStr = jsonObject.get("data").toString();
                    Map<String, Object> retMap = (Map<String, Object>) JSON.parse(dataStr);
                    List<String> list = JSONArray.parseArray(retMap.get("openid").toString(), String.class);
                    userOpenidList.addAll(list);
                }
            }
        }
        return userOpenidList;
    }


    /**
     * 调用微信JS接口的临时票据
     *
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getJsApiTicket(String access_token) {
        String requestUrl = Constants.TICKET_URL.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取token失败 :", e);
            }
        }
        return ticket;
    }

    private static String getUrl(HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        String queryString = request.getQueryString();
        String url = "";
        if (StringUtils.isNoneBlank(queryString)) {
            url = requestUrl + "?" + queryString;
        }
        // 原始的url默认80端口，request.getRequestURL()会带有tomcat端口，一定要去掉，否则签名会验证失败
        url = url.replaceFirst(":\\d{4}", "");
        return url;
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String str;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        log.info("==========url===" + str);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes(StandardCharsets.UTF_8));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ret.put("url", url);

        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        log.info("==========url===" + url);
        log.info("==========jsapi_ticket===" + jsapi_ticket);
        log.info("==========timestamp===" + timestamp);
        log.info("==========signature===" + signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis());
    }

    // 获取当前系统时间 用来判断access_token是否过期
    public static Date getTime() {
        Date dt = new Date();
        return dt;
    }

    // 获取当前系统时间 用来判断access_token是否过期
    public synchronized static String getTimeStr() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }


    public static com.alibaba.fastjson.JSONObject fetchWeixinInfo(String code, String appId, String secret) {
        String urlRet = HttpUtil.get(Constants.OAUTH2_URL.replace("APPID", appId).replace("SECRET", secret).replace("CODE", code));
        return com.alibaba.fastjson.JSONObject.parseObject(urlRet);
    }

    // 登录凭证校验，获取微信小程序的openid
    public static com.alibaba.fastjson.JSONObject fetchAppletInfo(String js_code, String appId, String secret) {
        String urlRet = HttpUtil.get(Constants.OAUTH2_URL_APPLET.replace("APPID", appId).replace("SECRET", secret).replace("JSCODE", js_code));
        return com.alibaba.fastjson.JSONObject.parseObject(urlRet);
    }

    /**
     * @param appId     公账号appId
     * @param appSecret
     * @return
     */
    public static Map<String, String> getParam(String appId, String appSecret, HttpServletRequest request) {
        if (access_token == null) {
            access_token = getAccess_token(appId, appSecret);
            jsapi_ticket = getJsApiTicket(access_token);
            time = getTimeStr();
        } else {
            if (!time.substring(0, 13).equals(getTimeStr().substring(0, 13))) { // 每小时刷新一次
                access_token = null;
                access_token = getAccess_token(appId, appSecret);
                jsapi_ticket = getJsApiTicket(access_token);
                time = getTimeStr();
            }
        }
        String url = getUrl(request);
        Map<String, String> params = sign(jsapi_ticket, url);
        params.put("appid", appId);
        return params;
    }

    public static Map<String, String> getCustomerInfo(String appId, String appSecret, HttpServletRequest request) {
        if (access_token == null) {
            access_token = getAccess_token(appId, appSecret);
            customer_info = getCustomerInfo(access_token);
            time = getTimeStr();
        } else {
            if (!time.substring(0, 13).equals(getTimeStr().substring(0, 13))) { // 每小时刷新一次
                access_token = null;
                access_token = getAccess_token(appId, appSecret);
                customer_info = getCustomerInfo(access_token);
                time = getTimeStr();
            }
        }
        String url = getUrl(request);
        Map<String, String> params = sign(customer_info, url);
        params.put("appid", appId);
        return params;
    }

    /**
     * 获取客服基本信息
     *
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getCustomerInfo(String access_token) {
        String requestUrl = Constants.KFLIST_URL.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取token失败 :", e);
            }
        }
        return ticket;
    }

    /**
     * 获取客户基本信息
     *
     * @param ACCESS_TOKEN
     * @param OPENID
     * @return
     */
    public static JSONObject getWeixinUserInfo(String ACCESS_TOKEN, String OPENID) {
        String requestUrl = Constants.USER_INFO_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN).replace("OPENID", OPENID);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        return jsonObject;
    }

    /**
     * 获取客户基本信息(未关注公众号)
     *
     * @param OPENID
     * @return
     */
    public static JSONObject getWeixinUserInfoByNoCare(String ACCESS_TOKEN, String OPENID) {
        String requestUrl = Constants.USER_INFO_NOCARE_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN).replace("OPENID", OPENID);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);
        log.info("getWeixinUserInfoByNoCare : " + JSONObject.valueToString(jsonObject));
        return jsonObject;
    }

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     * @author zhy
     */
    public static Map<String, Object> getSessionKeyOropenid(String code, String appId, String secret) {
        // 微信端登录code值
        String wxCode = code;
        String requestUrl = Constants.OAUTH_URL_APPLET; // 请求地址
        // https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", appId); // 开发者设置中的appId
        requestUrlParam.put("secret", secret); // 开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); // 小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code"); // 默认参数

        // 发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session
        // 接口获取openid用户唯一标识
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(HttpUtil.post(requestUrl, requestUrlParam.toString()));
        return jsonObject;
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     * @author zhy
     */
    public static com.alibaba.fastjson.JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     * @author zhy
     */
    public static com.alibaba.fastjson.JSONObject getSessionKeyOropenidJson(String code, String appId, String appSecret) {
        // 微信端登录code值
        String wxCode = code;
        String requestUrl = Constants.OAUTH_URL_APPLET; // 请求地址
        // https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", appId); // 开发者设置中的appId
        requestUrlParam.put("secret", appSecret); // 开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); // 小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code"); // 默认参数

        // 发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session
        // 接口获取openid用户唯一标识
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(HttpUtil.post(requestUrl, requestUrlParam.toString()));
        return jsonObject;
    }

    public static String getMediaId(String accessToken, String medid) {
        // 凭证获取(GET)
        String requestUrl = Constants.MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", medid);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);

        return jsonObject.toString();
    }

    // 获取微信小程序的用户信息
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getWXAppletUserInfo(String encryptedData, String iv, String js_code, String appId, String secret) {
        String urlRet = HttpUtil.get(Constants.OAUTH2_URL_APPLET.replace("APPID", appId).replace("SECRET", secret).replace("JSCODE", js_code));
        Map<String, Object> oauthResult = com.alibaba.fastjson.JSONObject.parseObject(urlRet);
        if ((oauthResult != null) && oauthResult.size() > 0) {
            if (oauthResult.containsKey("session_key")) {
                String session_key = oauthResult.get("session_key").toString();
                log.info("session_key : " + session_key);
                try {
                    String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
                    if (null != result && result.length() > 0) {
                        log.info("用户信息解密成功!");
                        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(result);
                        retMap.putAll(oauthResult);
                        return retMap;
                    }
                } catch (Exception e) {
                    log.error("用户信息解密失败!");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // 获取微信小程序的用户手机号
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getWXAppletUserPhone(String encryptedData, String iv, String js_code, String appId, String secret) {
        String urlRet = HttpUtil.get(Constants.OAUTH2_URL_APPLET.replace("APPID", appId).replace("SECRET", secret).replace("JSCODE", js_code));
        Map<String, Object> oauthResult = com.alibaba.fastjson.JSONObject.parseObject(urlRet);
        if ((oauthResult != null) && oauthResult.size() > 0) {
            if (oauthResult.containsKey("session_key")) {
                String session_key = oauthResult.get("session_key").toString();
                log.info("session_key : " + session_key);
                try {
                    String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
                    if (null != result && result.length() > 0) {
                        log.error("用户手机号解密成功!");
                        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(result);
                        retMap.putAll(oauthResult);
                        return retMap;
                    }
                } catch (Exception e) {
                    log.error("用户手机号解密失败!");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /* 发送 post请求 用HTTPclient 发送请求 */
    public static byte[] getUnlimited(String URL, String json) {
        String obj = null;
        InputStream inputStream = null;
        Buffer reader = null;
        byte[] data = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(URL);
        httppost.addHeader("Content-type", "application/json; charset=utf-8");
        httppost.setHeader("Accept", "application/json");
        try {
            StringEntity s = new StringEntity(json, StandardCharsets.UTF_8);
            s.setContentEncoding("UTF-8");
            httppost.setEntity(s);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 获取相应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    inputStream = entity.getContent();
                    data = readInputStream(inputStream);
                }
                return data;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 将流 保存为数据数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    private static JSONObject httpsGetRequest(String url) {
        HttpClient httpclient = new HttpClient();
        GetMethod getMethod = null;
        try {
            getMethod = new GetMethod(url);
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            int code = httpclient.executeMethod(getMethod);
            if (code == HttpURLConnection.HTTP_OK) {
                String ret = getMethod.getResponseBodyAsString();
                JSONObject retJson = new JSONObject(ret);
                return retJson;
            } else {
                log.info("连接微信服务器失败");
            }
        } catch (Exception e) {
            log.error("httpsGetRequest error:", e);
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
        }
        return new JSONObject();
    }


    public static String getWXDirectRoomList(String access_token) {
        String requestUrl = Constants.LIVEINFO_URL.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsGetRequest(requestUrl);

        return jsonObject.toString();
    }

    /**
     * 生成微信小程序二维码
     *
     * @param accessToken 接口调用凭证，该参数为 URL 参数，非 Body 参数。access_token和cloudbase_access_token二选一
     *                    其中access_token可通过getAccessToken接口获得；
     *                    如果是第三方代调用请传入authorizer_access_token；
     *                    cloudbase_access_token可通过getOpenData接口获得
     * @param path        扫码进入的小程序页面路径，最大长度 128 字节，不能为空；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"，即可在 wx.getLaunchOptionsSync 接口中的 query 参数获取到 {foo:"bar"}。
     * @param width       二维码的宽度，单位 px。最小 280px，最大 1280px;默认是430
     * @return
     */
    public static ByteArrayInputStream createQRCode(String appid, String secret, String path, int width) {
        String accessToken = getAccessToken(appid, secret);
        String requestUrl = Constants.WX_APPLET_WXAQRCODE.replace("ACCESS_TOKEN", accessToken);
        byte[] result = getUnlimited(requestUrl, JSON.toJSONString(new HashMap<String, Object>() {{
            put("path", path);
            put("width", width);
        }}));
        return new ByteArrayInputStream(result);
    }

    public static void main(String[] args) {

//        String access_token = WeixinUtil.getAccess_token("wx4fb2e6c589367b1f", "5109dabb6f8f23fef7a98589a22428b2");
//        String access_token = WeixinUtil.getAccess_token("wx3a8bdd56f81e5dff", "729a9e0a6693a464907ca70e8eddd9f1");
//        System.out.println(access_token);

//        String access_token = WeixinUtil.getAccess_token("wx3a8bdd56f81e5dff", "729a9e0a6693a464907ca70e8eddd9f1");
//        System.out.println(access_token);

        String appid = "wxc858f15e13603e4d";
        String appsecret = "98df5f5ee375e3ba570ca52b827ce1ac";

        String access_token = WeixinUtil.getAccessToken(appid, appsecret);
        System.out.println(access_token);

        List<String> weixinSubscribedUsers = WeixinUtil.getWeixinSubscribedUsers(access_token, null, null);
        System.out.println(JSON.toJSON(weixinSubscribedUsers));
        for (String wxOpenId : weixinSubscribedUsers) {
            com.mz.framework.util.weixin.json.JSONObject jsonObject = WeixinUtil.getWeixinUserInfo(access_token, wxOpenId);
            System.out.println(jsonObject);
        }


////		String  access_token= "56_zklSbqqgc4EuoMfQmVCy-5QzLrKghJFmUiREO9x95aapsymOALe1qwpRj511SZC1Bj38oYePiqYSsYFRw-EBtM270ABvpjJo4xrHAZs5sTatG17Y47kPkDvc3ksuTVpXBjrhFF29-XUYpVgQQWJcAAAJPC";
//		String  weixinOpenid= "ouesG6B4hj2NeuIrpok4xrAtblgI";
//
//		JSONObject weixinUserInfo = WeixinUtil.getWeixinUserInfo(access_token, weixinOpenid);
//		System.out.println(weixinUserInfo);


//		try {
//			 String js_code = "011weSBZ0nsZ4V1b4lDZ0BWMBZ0weSBN";
//			 String iv = "W5/uNGpKLCL6pWO4dOc5Ww==";
//			 String encryptedData =
//			 "VabtxCLr15xDkd7W1qT10XKqKAzjCfT219VhbOLovXyLmJjaDYZ7lK7P+1UeUDpgNfbSgucCzY6urk72MtNyyzdr1jc+rdDFz1oBkt0nptwlpvGjSYc5denwZouYR/BqCmLQj2sCR/6a+pwWFXBytaHdbLg7Ap8s6AMXqtUrZJs05m9j1WNyD/VhGw9oUa08a3ecV3sZR0Y0tlK6du7svkLRiEZIs1vuLn5biMsITOhgbw8celBMbg1Onz1uD5axHKVTMpOdPMHb82P7iJdCpheXagXtsdFYVnVue+8ZUaJziLdesRV4PM9HugFklAr60zXy03NGdaSxfeM7bN381+Fac5a0/WuvDqj9maxF9BVWlblLdD/mYjspF6zLBnlLvc2SD/gzI9iZrTusEN5t5ogmSdY1h4nMeO+MssMxeF9AHj4X1Wwcdhg16X0OhY+TSriDupuG69bytKWD6pmIv5nZMM8PbXJojqfSLPRVnszSbG0vO532/+nioCDff6tAyHk513qR/FjphVPbN7eh8/Xj0r313W5z19aMGoM54gg=";
//			 Map<String, Object> wxAppletUserInfo = getWXAppletUserInfo(encryptedData, iv,
//			 js_code, "wx7689484709cc42cc", "21d14aa69672219bef5ce3a76c924a1d");
//
//			String wxDirectRoomList = getWXDirectRoomList(access_token);
//			System.out.println(wxDirectRoomList);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }

}
