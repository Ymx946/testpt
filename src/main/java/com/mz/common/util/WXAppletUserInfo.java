package com.mz.common.util;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.util.wxaes.HttpKit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 微信小程序信息获取
 *
 * @author zhy
 */
public class WXAppletUserInfo {
    private static final Logger log = LogManager.getLogger(WXAppletUserInfo.class);
    //	测试小程序
//    public static String WXXCX_APPID = "wx3a8bdd56f81e5dff";
//    public static String WXXCX_APPSECRET = "729a9e0a6693a464907ca70e8eddd9f1";
//    正式小程序(改为从站点表中获取值，这里的值作为默认值)
    public static String WXXCX_APPID = "wxa27fd271023adc42";
    public static String WXXCX_APPSECRET = "1b1a28c5cadf6d43ff14390fc46b89d9";

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     */
    public static JSONObject getSessionKeyOropenid(String appId, String appSecret, String code) {
        //微信端登录code值
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";    //请求地址 https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", appId);
        requestUrlParam.put("secret", appSecret);
        requestUrlParam.put("js_code", wxCode);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数
        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        //{session_key: "5gNu+G+4edmYoylXLEisyQ==", openid: "oJtsk0Qv6gyOI9FNgWWUWIAcCKRI"}
        JSONObject jsonObject = JSONObject.parseObject(sendGet(requestUrl, requestUrlParam));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     */
    public static String getUserPhoneNumber(String accessToken, String code) throws InterruptedException, ExecutionException, IOException {
        String requestUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;    //
        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put("code", code);
        JSONObject jsonObject1 = JSONObject.parseObject(HttpKit.post(requestUrl, jsonObjectData.toString()));
        String errcode = jsonObject1.getString("errcode");
        if (errcode.equals("0")) {
            JSONObject jsonObject2 = jsonObject1.getJSONObject("phone_info");
            System.out.println(jsonObject1);
            System.out.println(jsonObject1.getString("phone_info"));
            System.out.println(jsonObject2.getString("phoneNumber"));
            return jsonObject2.getString("phoneNumber");
        } else {
            return null;
        }
    }

    /**
     * 获取微信小程序 access_token
     *
     * @return
     */
    public static JSONObject getToken(String appId, String appSecret) {
        //微信端登录code值
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("grant_type", "client_credential");
        requestUrlParam.put("appid", appId);
        requestUrlParam.put("secret", appSecret);
        JSONObject jsonObject = JSONObject.parseObject(sendGet(requestUrl, requestUrlParam));
        return jsonObject;
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
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
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONObject.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param paramMap 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String data) {
        try {
            URL conurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) conurl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = JSONObject.parseObject(data);

            out.writeBytes(obj.toString());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), StandardCharsets.UTF_8);
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
            return sb.toString();
            // 断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static InputStream sendPostBuffered(String url, String data) {
        try {
            URL conurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) conurl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = JSONObject.parseObject(data);


            out.writeBytes(obj.toString());
            out.flush();
            out.close();
            InputStream is = connection.getInputStream();
            String contentType = connection.getContentType();
//            图片：image/jpeg
//            异常：application/json; charset=UTF-8
            if (contentType.equals("image/jpeg")) {//返回成功
                return is;
            } else {
                //读取响应---如果出错打印API返回
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String line = " ";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String tt = buffer.toString();
                System.out.println(tt);
                return null;
            }
            // 断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//
//    /**
//     * 小程序订阅消息-获取token(作废，有相同方法，业务端统一由WxService中获取)
//     */
//    public static String getAccessToken(String appId, String appSecret) {
//        String result = cn.hutool.http.HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
//        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result);
//        return jsonObject.getStr("access_token");
//    }

    /**
     * 小程序订阅消息-发送订阅消息
     */
    public static String sendSubscribeMsg(String accessToken, String body) {
        String post = cn.hutool.http.HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, body);
        return post;
    }
}