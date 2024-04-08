package com.mz.framework.util.weixin;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpSSLRequest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 表示请求器是否已经做了初始化工作
    private boolean hasInit = false;

    // HTTP请求器
    private CloseableHttpClient httpClient;

    private void init(String certLocalPath, String certPassword) {
        try {
            logger.info("---------start2------");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(certLocalPath));// 加载本地的证书进行https加密传输
            try {
                keyStore.load(instream, certPassword.toCharArray());// 设置证书密码
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                instream.close();
            }

            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            hasInit = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("退款请求出错init：" + e.getMessage());
        }
    }

    /**
     * 通过Https往API post xml数据
     *
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public String sendPost(String url, Object xmlObj, String certLocalPath, String certPassword) {
        logger.info("---------start------");
        if (certLocalPath.split(".").length > 2) {
            logger.info("文件路径不合法，防止类似xx.sh.png攻击");
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (!hasInit) {
                init(certLocalPath, certPassword);
            }
            // 解决XStream对出现双下划线的bug
            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            xStreamForRequestPostData.alias("xml", xmlObj.getClass());
            // 将要提交给API的数据对象转换成XML格式数据Post给API
            String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
            logger.info("postDataXML:" + postDataXML);
            // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            // 设置请求器的配置
            // httpPost.setConfig(requestConfig);
            logger.info("executing request" + httpPost.getRequestLine());
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error("退款请求出错：" + e.getMessage());
        } finally {
            httpPost.abort();
        }
        return result;
    }

}
