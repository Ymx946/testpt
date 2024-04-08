package com.mz.common.util.baiduai;

import com.mz.common.util.SSLUtils;
import org.springframework.http.MediaType;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 */
public class HttpUtil {
    public static final int CONNECTION_TIMEOUT = 5 * 20000 * 1000;// 连接超时

    public static String post(String requestUrl, String accessToken, String params) throws Exception {
//        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, MediaType.APPLICATION_FORM_URLENCODED_VALUE, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params) throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding) throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding) throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        connection.disconnect();
        System.err.println("result:" + result);
        return result;
    }

    public static String getConnByGet(String url) {
        StringBuilder result = new StringBuilder();
        try {
            // 建立连接
            URL link = new URL(url);
            /* 获取客户端向服务器端传送数据所依据的协议名称 */
            String protocol = link.getProtocol();
            if ("https".equalsIgnoreCase(protocol)) {
                /* 获取HTTPS请求的SSL证书 */
                try {
                    SSLUtils.ignoreSsl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            HttpsURLConnection connection = (HttpsURLConnection) link.openConnection();
            //连接服务器
            connection.connect();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            // 取得输入流，并使用Reader读取
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getConnByGet(String url, Charset charSet) {
        StringBuilder result = new StringBuilder();
        try {
            // 建立连接
            URL link = new URL(url);
            /* 获取客户端向服务器端传送数据所依据的协议名称 */
            String protocol = link.getProtocol();
            if ("https".equalsIgnoreCase(protocol)) {
                /* 获取HTTPS请求的SSL证书 */
                try {
                    SSLUtils.ignoreSsl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            HttpsURLConnection connection = (HttpsURLConnection) link.openConnection();
            //连接服务器
            connection.connect();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            // 取得输入流，并使用Reader读取
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charSet));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
