package com.mz.common.util;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 微信工具类
 */
public class WxUtils {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final Logger logger = LoggerFactory.getLogger(WxUtils.class);

    /**
     * 数据转换为xml格式
     *
     * @param object
     * @param obj
     * @return
     */
    public static String truncateDataToXML(Class<?> object, Object obj) {
        XStream xStream = new XStream(new XppDriver(new NoNameCoder()));
        xStream.alias("xml", object);
        return xStream.toXML(obj);
    }

    /**
     * 数据转换为对象
     *
     * @param object
     * @param str
     * @return
     */
    public static Object truncateDataFromXML(Class<?> object, String str) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("xml", object);
        return xstream.fromXML(str);
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String makeNonStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String map2XmlString(Map<String, String> map) {
        String xmlResult = "";

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
            System.out.println();
        }
        sb.append("</xml>");
        xmlResult = sb.toString();

        return xmlResult;
    }

    /**
     * 拼接签名数据
     *
     * @return
     */
    public static String makeSign(BeanMap beanMap, String mchKey, String signType) throws Exception {
        SortedMap<String, String> signMaps = Maps.newTreeMap();

        for (Object key : beanMap.keySet()) {
            Object value = beanMap.get(key);

            // 排除空数据
            if (value == null) {
                continue;
            }
            signMaps.put(key + "", String.valueOf(value));
        }
        if (signType.equals("MD5")) {
            // 生成签名
            return generateSign(signMaps, mchKey);
        } else if (signType.equals("SHA256")) {
            return generateSignSHA256(signMaps, mchKey);
        } else {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param signMaps
     * @return
     * @throws Exception
     */
    public static String generateSign(SortedMap<String, String> signMaps, String mchKey) {
        StringBuffer sb = new StringBuffer();

        // 字典序
        for (Map.Entry signMap : signMaps.entrySet()) {
            String key = (String) signMap.getKey();
            String value = (String) signMap.getValue();

            // 为空不参与签名、参数名区分大小写
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        // 拼接key
        sb.append("key=").append(mchKey);
        // MD5加密
        String sign = MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    public static String generateSignSHA256(SortedMap<String, String> signMaps, String mchKey) throws Exception {
        StringBuffer sb = new StringBuffer();

        // 字典序
        for (Map.Entry signMap : signMaps.entrySet()) {
            String key = (String) signMap.getKey();
            String value = (String) signMap.getValue();

            // 为空不参与签名、参数名区分大小写
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        // 拼接key
        sb.append("key=").append(mchKey);
        // MD5加密
        String sign = HMACSHA256(sb.toString(), mchKey).toUpperCase();
        return sign;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static HashMap<String, String> sortAsc(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }

    private static String SHA1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash.toUpperCase();
    }
}

