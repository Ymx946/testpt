package com.mz.common.util.wxaes;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * https 请求 微信为https的请求
 *
 * @author andy
 * @date 2015-10-9 下午2:40:19
 */
@Slf4j
public class HttpKit {
    private static final String DEFAULT_CHARSET = "UTF-8";
    //请求超时时间,这个时间定义了socket读数据的超时时间，也就是连接到服务器之后到从服务器获取响应数据需要等待的时间,发生超时，会抛出SocketTimeoutException异常。
    private static final int SOCKET_TIME_OUT = 60000;
    //连接超时时间,这个时间定义了通过网络与服务器建立连接的超时时间，也就是取得了连接池中的某个连接之后到接通目标url的连接等待时间。发生超时，会抛出ConnectionTimeoutException异常
    private static final int CONNECT_TIME_OUT = 60000;
    private static final int CONNECT_REQUEST_TIMEOUT = 60000;

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: get 请求
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.prepareGet(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        if (headers != null) {
            headers.forEach((key, val) -> {
                builder.setHeader(key, val);
            });
        }
        if (params != null) {
            params.forEach((key, val) -> {
                builder.addQueryParam(key, val);
            });
        }
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: get 请求
     */
    public static String get(String url) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException, ExecutionException, InterruptedException {
        return get(url, null);
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @description 功能描述: get 请求
     */
    public static String get(String url, Map<String, String> params) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException, IOException, ExecutionException, InterruptedException {
        return get(url, params, null);
    }

    /**
     * @return 返回类型:
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @description 功能描述: POST 请求
     */
    public static String post(String url, Map<String, String> params) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.addQueryParam(key, params.get(key));
            }
        }
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    public static String post(String url, String s) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        builder.setBody(s);
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    public static String post(String url, String s, String headName, String headValue) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setHeader(headName, headValue);
        builder.setBodyEncoding(DEFAULT_CHARSET);
        builder.setBody(s);
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    public static String postHead(String url, String s, Map<String, String> headMap) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        headMap.forEach((key, val) -> {
            builder.setHeader(key, val);
        });
        builder.setBodyEncoding(DEFAULT_CHARSET);
        builder.setBody(s);
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    /**
     * 设置请求头的post(移动公司对接)
     *
     * @param url
     * @param s
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String postHead(String url, String s) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient http = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = http.preparePost(url);
        builder.setHeader("x-no-encrypt", "true");
        builder.setBodyEncoding(DEFAULT_CHARSET);
        builder.setBody(s);
        Future<Response> f = builder.execute();
        String body = f.get().getResponseBody(DEFAULT_CHARSET);
        http.close();
        return body;
    }

    public static String sendRequest(String url, String params, Map<String, String> headMap, String method) {
        log.info("url： " + url + "，params： " + params + "，headMap： " + JSON.toJSONString(headMap) + "，method： " + method);
        try {
            URL conurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) conurl.openConnection();
            if (CollectionUtil.isNotEmpty(headMap)) {
                headMap.forEach((key, val) -> {
                    connection.setRequestProperty(key, val);
                });
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if (StringUtils.isEmpty(method)) {
                method = "POST";
            }
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            if (StringUtils.isNoneBlank(params)) {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                JSONObject obj = JSONObject.parseObject(params);
//                out.writeBytes(obj.toString());
                out.write(params.getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
            }
            StringBuffer strBuffer = new StringBuffer();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                strBuffer = new StringBuffer();
                String readLine = "";
                //读取响应
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
            }
            connection.disconnect();
            String retObj = strBuffer.toString();
            return retObj;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        log.info("retObj：返回空字符串");
        return "";
    }

    public static String sendClientPostHttp(String url, String params, Map<String, String> headMap) {
        log.info("url： " + url + "，params： " + params + "，headMap： " + JSON.toJSONString(headMap));
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.custom().disableAutomaticRetries().setRedirectStrategy(new LaxRedirectStrategy()).build();
//            httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
            HttpPost httpPost = new HttpPost(url);
            if (headMap != null && headMap.size() > 0) {
                headMap.forEach((k, v) -> {
                    httpPost.setHeader(k, v);
                });
            }
            RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIME_OUT).build();
            httpPost.setConfig(config);
            httpPost.setEntity(new StringEntity(params, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.HTTP_OK) {
//                log.error(result + "-----------success------------------");
                return result;
            } else {
                log.error(httpResponse.getStatusLine().getStatusCode() + "------------------fail-----------");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取token出现连接/超时异常");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendSSLRequest(String url, String params, Map<String, String> headMap, String method) {
        log.info("url： " + url + "，params： " + params + "，headMap： " + JSON.toJSONString(headMap) + "，method： " + method);
        try {
            URL conurl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) conurl.openConnection();
            //  直接通过主机认证
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            headMap.forEach((key, val) -> {
                connection.setRequestProperty(key, val);
            });
            connection.setDoOutput(true);
//            connection.setDoInput(true);
            if (StringUtils.isEmpty(method)) {
                method = "POST";
            }
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            JSONObject obj = JSONObject.parseObject(params);
            out.writeBytes(obj.toString());
            out.flush();
            out.close();

            StringBuffer strBuffer = new StringBuffer();
            log.info("====responseCode：" + connection.getResponseCode());
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                log.info("====HTTP_OK：" + HttpURLConnection.HTTP_OK);
                strBuffer = new StringBuffer();
                String readLine = "";
                //读取响应
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
            }
            connection.disconnect();
            String retObj = strBuffer.toString();
            log.info("retObj：" + retObj);
            return retObj;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        log.info("retObj：返回空字符串");
        return "";
    }

    public static String sendHeadRequest(String url, String params, Map<String, String> headMap) {
        log.info("url： " + url + "，params： " + params + "，headMap： " + JSON.toJSONString(headMap));
        try {
            cn.hutool.http.HttpResponse response = HttpRequest.post(url).header("Content-Type", "application/json").headerMap(headMap, false).body(params).timeout(5 * 60 * 1000).execute();
            StringBuffer strBuffer = new StringBuffer();
            if (HttpURLConnection.HTTP_OK == response.getStatus()) {
                strBuffer = new StringBuffer();
                String readLine = "";
                InputStream inputStream = response.bodyStream();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
                inputStream.close();
            }
            return strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        log.info("retObj：返回空字符串");
        return "";
    }

    public static String sendPostHttp(String url, String authorization, String data) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNoneBlank(authorization)) {
                httpPost.setHeader("Authorization", authorization);
            }
            HttpEntity entityParam = new StringEntity(data, ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(entityParam);
            HttpResponse response = httpClient.execute(httpPost);
            StringBuffer strBuffer = new StringBuffer();
            if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode()) {
                strBuffer = new StringBuffer();
                String readLine = "";
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
                inputStream.close();
            }
            return strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        return "";
    }

    /**
     * form表单提交
     *
     * @param url
     * @param map
     * @return
     */
    public static String doPostForm(String url, Map<String, String> map) {
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + "charset=UTF-8");
        // 3. 创建参数队列（键值对列表）
        List<NameValuePair> paramPairs = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object val = map.get(key);
            paramPairs.add(new BasicNameValuePair(key, val.toString()));
        }
        UrlEncodedFormEntity entity;
        try {
            // 4. 将参数设置到entity对象中
            entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            // 5. 将entity对象设置到httppost对象中
            httpPost.setEntity(entity);
            // 6. 发送请求并回去响应
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                // 9. 关闭响应对象
                resp.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 10. 关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}  