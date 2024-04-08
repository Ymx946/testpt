package com.mz.common.util.baiduai;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.mz.common.exception.ServiceException;
import com.mz.common.util.ImageUtil;
import com.mz.common.util.baiduai.Base64Util;
import com.mz.common.util.baiduai.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取token类
 */
@Slf4j
public class BaiduAiUtil {
    private static final int DPI = 300;

    public static Map<String, Object> getDefinedOCRDataPdf(String pdfUrl) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        String accessToken = BaiduAiUtil.getAuth(BaiduConstant.BAIDU_AK, BaiduConstant.BAIDU_SK);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(pdfUrl, byte[].class);
        PDDocument pd = PDDocument.load(new ByteArrayInputStream(response.getBody()));
        PDFRenderer pdfRenderer = new PDFRenderer(pd);
        if (pd.getNumberOfPages() > 0) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, DPI, ImageType.RGB);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bim, "png", bos);
            String imgStr = Base64Util.encode(bos.toByteArray());

            // 请求模板参数
            String recogniseParams = "templateSign=" + BaiduConstant.TEMPLATE_SIGN_ID + "&image=" + URLEncoder.encode(imgStr, "UTF-8");
            // 请求模板识别
            String result = "";
            try {
                result = HttpUtil.post(BaiduConstant.RECOGNISE_URL, accessToken, recogniseParams);
            } catch (Exception e) {
                accessToken = BaiduAiUtil.getAuth(BaiduConstant.BAIDU_AK, BaiduConstant.BAIDU_SK);
                result = HttpUtil.post(BaiduConstant.RECOGNISE_URL, accessToken, recogniseParams);
            }
            log.info(result);

            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
            int errorCode = jsonObject.getInteger("error_code");
            if (0 == errorCode) {
                com.alibaba.fastjson.JSONObject dataObj = jsonObject.getJSONObject("data");
                Boolean isStructured = dataObj.getBoolean("isStructured");
                if (Boolean.TRUE.equals(isStructured)) {
                    String ret = dataObj.getString("ret");
                    List<Map> mapList = JSONArray.parseArray(ret, Map.class);
                    for (Map map : mapList) {
                        String wordName = String.valueOf(map.get("word_name"));
                        if ("exam_date".equalsIgnoreCase(wordName)) {
                            retMap.put(wordName, String.valueOf(map.get("word")).replace("年", "-").replace("月", "-").replace("日", ""));
                        } else {
                            retMap.put(wordName, map.get("word"));
                        }
                    }
                }
            } else {
                String errorMsg = jsonObject.getString("error_msg");
                log.error("errorMsg：" + errorMsg);
                throw new ServiceException("OCR自定义识别错误，原因：" + errorMsg);
            }
        }
        return retMap;
    }

    public static Map<String, Object> getDefinedOCRData(String imgUrl) {
        Map<String, Object> retMap = new HashMap<>();
        String accessToken = BaiduAiUtil.getAuth(BaiduConstant.BAIDU_AK, BaiduConstant.BAIDU_SK);
        try {
//            byte[] imgData = FileUtil.readFileByBytes(filePath);
            byte[] imgData = ImageUtil.getImageFromNetByUrl(imgUrl);
            String imgStr = Base64Util.encode(imgData);
            // 请求模板参数
            String recogniseParams = "templateSign=" + BaiduConstant.TEMPLATE_SIGN_ID + "&image=" + URLEncoder.encode(imgStr, "UTF-8");
            // 请求模板识别
            String result = HttpUtil.post(BaiduConstant.RECOGNISE_URL, accessToken, recogniseParams);
            log.info(result);

            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
            int errorCode = jsonObject.getInteger("error_code");
            if (0 == errorCode) {
                com.alibaba.fastjson.JSONObject dataObj = jsonObject.getJSONObject("data");
                Boolean isStructured = dataObj.getBoolean("isStructured");
                if (Boolean.TRUE.equals(isStructured)) {
                    String ret = dataObj.getString("ret");
                    List<Map> mapList = JSONArray.parseArray(ret, Map.class);
                    for (Map map : mapList) {
                        String wordName = String.valueOf(map.get("word_name"));
                        if ("exam_date".equalsIgnoreCase(wordName)) {
                            map.put(wordName, wordName.replace("年", "-").replace("月", "-").replace("日", ""));
                        } else {
                            retMap.put(wordName, map.get("word"));
                        }
                    }
                }
            } else {
                String errorMsg = jsonObject.getString("error_msg");
                log.error("errorMsg：" + errorMsg);
            }
        } catch (Exception e) {
            log.error("OCR自定义识别错误，原因：", e);
        }
        return retMap;
    }

    /**
     * 情感倾向分析
     *
     * @return sentiment 表示情感极性分类结果，0:负向，1:中性，2:正向
     */
    public static Integer emotionalTendencyAnalyse(String text) {
        String accessToken = getAuth(BaiduConstant.CLIENT_AK, BaiduConstant.CLIENT_SK);
        try {
            if (ObjectUtil.isNotEmpty(text)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", text);
                String jsonString = jsonObject.toString();
                if (ObjectUtil.isNotEmpty(jsonString)) {
                    String result = HttpUtil.post(BaiduConstant.SENTIMENT_CLASSIFY_URL, accessToken, jsonString);
                    // 解析JSON字符串
                    JSONObject jsonResult = new JSONObject(result);
                    if (ObjectUtil.isNotEmpty(jsonResult)) {
                        // 获取items数组中的第一个元素
                        if (jsonResult.has("items")) {
                            JSONObject item = jsonResult.getJSONArray("items").getJSONObject(0);
                            // 获取sentiment字段的值
                            int sentiment = item.getInt("sentiment");
                            return sentiment;
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取API访问token（access_token的有效期为30天，需要每30天进行定期更换）
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        String getAccessTokenUrl = BaiduConstant.AUTH_HOST_URL
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            log.info("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.print("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 相似图库检索
     * <p>
     * accessToken 访问凭证
     * imgStr  Base64处理后的图片
     */
    public static String similarSearch(String accessToken, String imgStrBase64) {
        try {
            String imgParam = URLEncoder.encode(imgStrBase64, "UTF-8");
            String param = "image=" + imgParam + "&pn=" + 1 + "&rn=" + 100;
            String result = HttpUtil.post(BaiduConstant.SIMILAR_SEARCH_URL, accessToken, param);
            log.info("相似=====" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 相同图库检索
     * <p>
     * imgStr  Base64处理后的图片
     */
    public static String sameSearch(String accessToken, String imgStrBase64) {
        try {
            String imgParam = URLEncoder.encode(imgStrBase64, "UTF-8");
            String param = "image=" + imgParam + "&pn=" + 1 + "&rn=" + 100;
            String result = HttpUtil.post(BaiduConstant.SAME_SEARCH_URL, accessToken, param);
            log.info("相同======" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 相似图片入库
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     * linkId  关联信息
     */
    public static String similarAdd(String accessToken, String imgUrl, String linkId) {
        try {
            byte[] imgData = ImageUtil.getImageFromNetByUrl(imgUrl);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "brief=" + "{'linkId':" + linkId + "}" + "&image=" + imgParam + "&tags=" + "1,1";
            String result = HttpUtil.post(BaiduConstant.SIMILAR_ADD_URL, accessToken, param);
            log.info(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 相同图片入库
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     * linkId  关联信息
     */
    public static String sameAdd(String accessToken, String imgUrl, String linkId) {
        try {
            byte[] imgData = ImageUtil.getImageFromNetByUrl(imgUrl);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "brief=" + "{'linkId':" + linkId + "}" + "&image=" + imgParam + "&tags=" + "1,1";
            String result = HttpUtil.post(BaiduConstant.SAME_ADD_URL, accessToken, param);
            log.info(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 相似图片删除
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     * linkId  关联信息
     */
    public static String similarDelete(String accessToken, String imgUrl) {
        try {
            byte[] imgData = ImageUtil.getImageFromNetByUrl(imgUrl);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            String result = HttpUtil.post(BaiduConstant.SIMILAR_DELETE_URL, accessToken, param);
            log.info(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 相同图片删除
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     */
    public static String sameDelete(String accessToken, String imgUrl) {
        try {
            byte[] imgData = ImageUtil.getImageFromNetByUrl(imgUrl);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            String result = HttpUtil.post(BaiduConstant.SAME_DELETE_URL, accessToken, param);
            log.info(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 图片入库(外部场景调用)
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     * linkId  关联信息
     */
    public static String imgAdd(String accessToken, String imgUrl, String linkId) {
        String returnSimlarData = similarAdd(accessToken, imgUrl, linkId);//相似搜索库入库
        String returnSameData = sameAdd(accessToken, imgUrl, linkId);//相同搜索库入库
        return returnSimlarData + returnSameData;
    }

    /**
     * 图片删除(外部场景调用)
     * <p>
     * accessToken 访问凭证
     * imgUrl  网络请求图
     * linkId  关联信息
     */
    public static String imgDelete(String accessToken, String imgUrl) {
        String returnSimlarData = similarDelete(accessToken, imgUrl);//相似搜索库删除
        String returnSameData = sameDelete(accessToken, imgUrl);//相同搜索库删除
        return returnSimlarData + returnSameData;
    }

    /**
     * 图片搜索(外部场景调用)
     * <p>
     * accessToken 访问凭证
     * imgStr  Base64处理后的图片
     * <p>
     * 返回关联ID
     */
    public static String imgSearch(String accessToken, String imgStr) {
        String linkId = "";
        String returnSameData = sameSearch(accessToken, imgStr);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(returnSameData);
        //先匹配相同库,如果不存在，则匹配
        if (jsonObject.getInteger("result_num") > 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            com.alibaba.fastjson.JSONObject json = jsonArray.getJSONObject(0);
            com.alibaba.fastjson.JSONObject briefJson = json.getJSONObject("brief");
            linkId = briefJson.getString("linkId");
        } else {
            String returnSimlarData = similarSearch(accessToken, imgStr);
            jsonObject = com.alibaba.fastjson.JSONObject.parseObject(returnSimlarData);
            if (jsonObject.getInteger("result_num") > 0) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                com.alibaba.fastjson.JSONObject json = jsonArray.getJSONObject(0);
                if (json.getDouble("score") > 0.5) {
                    com.alibaba.fastjson.JSONObject briefJson = json.getJSONObject("brief");
                    linkId = briefJson.getString("linkId");
                }
            }
        }
        return linkId;
    }

    public static void main(String[] args) {
//         System.out.println(getAuth());
//        System.out.println("==========="+imgAdd("24.1e976c5054761c95dbac79158de56881.2592000.1635318574.282335-24854197","https://img.mzszxc.com/mzsz/test/images/IMG20210914091111.jpg","11"));
//        System.out.println("===========" + imgSearch("24.1e976c5054761c95dbac79158de56881.2592000.1635318574.282335-24854197", "https://img.mzszxc.com/mzsz/test/images/IMG20210928164057.png"));
//        System.out.println("==========="+imgDelete("24.1e976c5054761c95dbac79158de56881.2592000.1635318574.282335-24854197","https://img.mzszxc.com/mzsz/test/images/IMG20210914091056.jpg"));
        System.out.println(emotionalTendencyAnalyse("祖国繁荣昌盛"));
    }

    /**
     * 获取权限token（access_token的有效期为30天，需要每30天进行定期更换）
     *
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public String getAuth() {
        return getAuth(BaiduConstant.CLIENT_ID, BaiduConstant.CLIENT_SECRET);
    }
}