package com.mz.common.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.lang3.RegExUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class WxCertHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(WxCertHttpUtil.class);
    private static final int socketTimeout = 10000;// 连接超时时间，默认10秒
    private static final int connectTimeout = 30000;// 传输超时时间，默认30秒
    private static boolean hasInit = false;
    private static RequestConfig requestConfig;// 请求配置
    private static CloseableHttpClient httpClient;// HTTP请求

    /**
     * @param url    API地址
     * @param xmlObj 要提交的XML
     * @param mchId  服务商商户ID
     * @return
     */
    public static String postData(String url, String jsonStr, String mchId, String certPath) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 加载证书
        try {
            if (!hasInit) {
                loadCert(certPath, mchId);
            }

            StringEntity postEntity = new StringEntity(jsonStr, "UTF-8");
//            httpPost.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
            httpPost.setEntity(postEntity);

            // 设置请求器的配置
//            requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
//            httpPost.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error("微信请求出错：" + e.getMessage());
            e.printStackTrace();
        } finally {
            httpPost.abort();
        }
        return result;
    }

    /**
     * 加载证书
     *
     * @param certLocalPath 证书路径
     * @param certPassword  证书密钥
     * @throws Exception
     */
    private static void loadCert(String certLocalPath, String certPassword) {
        try {
            //  证书路径
            String path = RegExUtils.removeFirst(certLocalPath, "classpath:");
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            //  指定证书格式为PKCS12
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //  读取PKCS12证书文件
            FileInputStream instream = new FileInputStream(path);
            try {
                // 指定PKCS12的密碼(商戶ID)
                keyStore.load(instream, certPassword.toCharArray()); // 设置证书密码
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                instream.close();
            }
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);

            httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
            hasInit = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载证书请求出错init：" + e.getMessage());
        }
    }
}
