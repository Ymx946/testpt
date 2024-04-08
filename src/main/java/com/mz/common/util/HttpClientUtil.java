package com.mz.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 实现Https请求 https=http+ssl安全加密通信
 */
public class HttpClientUtil {
    public static final int http_ok = 200;// 返回状态码正常
    public static final int CONNECTION_TIMEOUT = 5000;// 连接超时
    public static final int READDATA_TIMEOUT = 10000;// 数据读取等待超时
    public static final int DEFAULT_HTTP_PORT = 80;// http端口
    public static final int DEFAULT_HTTPS_PORT = 443;// https端口
    private static final Log log = LogFactory.getLog(HttpClientUtil.class);

    /**
     * 无需本地证书keyStore的SSL https带参数请求
     *
     * @param url
     * @param reqMap
     * @param encoding
     * @return
     */
    public static String postSSLUrlWithParams(String url, Map<String, String> reqMap, String encoding) {
        log.info("httpsClient访问开始...");
        CloseableHttpClient httpClient = HttpClientUtil.createSSLInsecureClient();
        HttpPost post = new HttpPost(url);
        // 添加参数
        List<NameValuePair> params = new ArrayList<>();
        if (reqMap != null && reqMap.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iter = reqMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entity = iter.next();
                params.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            // 设置客户端请求的头参数getParams已经过时,现在用requestConfig对象替换
            // httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(READDATA_TIMEOUT).build();
            post.setConfig(requestConfig);
            // 设置编码格式
            post.setEntity(new UrlEncodedFormEntity(params, encoding));
            HttpResponse response = httpClient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), encoding));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("编码格式输入错误", e);
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + encoding);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("读取流文件异常", e);
            throw new RuntimeException("读取流文件异常", e);
        } catch (Exception e) {
            log.error("通讯未知系统异常", e);
            throw new RuntimeException("通讯未知系统异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("关闭br异常" + e);
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 无需本地证书keyStore的SSL https带参数请求
     *
     * @param url
     * @param reqMap
     * @param encoding
     * @return
     */
    public static String getSSLUrlWithParams(String url, Map<String, String> reqMap, Map<String, String> headers, String encoding) {
        log.info("httpsClient访问开始...");
        CloseableHttpClient httpClient = HttpClientUtil.createSSLInsecureClient();
        // 添加参数
        List<NameValuePair> params = new ArrayList<>();
        if (reqMap != null && reqMap.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iter = reqMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entity = iter.next();
                params.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            //设置参数
            for (Map.Entry<String, String> entry : reqMap.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            HttpGet get = new HttpGet(uriBuilder.build());
            // 设置客户端请求的头参数getParams已经过时,现在用requestConfig对象替换
            // httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(READDATA_TIMEOUT).build();
            get.setConfig(requestConfig);
            // 设置header
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
            get.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(get);
            HttpEntity httpEntity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), encoding));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("编码格式输入错误", e);
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + encoding);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("读取流文件异常", e);
            throw new RuntimeException("读取流文件异常", e);
        } catch (Exception e) {
            log.error("通讯未知系统异常", e);
            throw new RuntimeException("通讯未知系统异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("关闭br异常" + e);
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 无需本地证书keyStore的SSL https带参数请求
     *
     * @param url
     * @param json
     * @param encoding
     * @return
     */
    public static String postSSLUrlWithJson(String url, String json, Map<String, String> headers, String encoding) {
        if (StringUtils.isEmpty(json)) {
            json = "{}";
        }
        log.info("httpsClient访问开始...");
        CloseableHttpClient httpClient = HttpClientUtil.createSSLInsecureClient();
        HttpPost post = new HttpPost(url);
        // 添加参数
        List<NameValuePair> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            // 设置客户端请求的头参数getParams已经过时,现在用requestConfig对象替换
            // httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(READDATA_TIMEOUT).build();
            post.setConfig(requestConfig);
            // 设置参数和编码格式
            post.setEntity(new StringEntity(json, encoding));
            // 设置header
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
            post.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), encoding));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("编码格式输入错误", e);
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + encoding);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("读取流文件异常", e);
            throw new RuntimeException("读取流文件异常", e);
        } catch (Exception e) {
            log.error("通讯未知系统异常", e);
            throw new RuntimeException("通讯未知系统异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("关闭br异常" + e);
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 创建一个SSL信任所有证书的httpClient对象
     *
     * @return
     */
    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 默认信任所有证书
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            // AllowAllHostnameVerifier: 这种方式不对主机名进行验证，验证功能被关闭，是个空操作(域名验证)
            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return HttpClients.custom().setSSLSocketFactory(sslcsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
