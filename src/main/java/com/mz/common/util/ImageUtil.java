package com.mz.common.util;

import cn.afterturn.easypoi.entity.ImageEntity;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 本地连接地址
     * @return
     */
    public static byte[] getImageFromLocalByUrl(String strUrl) {
        try {
            File imageFile = new File(strUrl);
            InputStream inStream = new FileInputStream(imageFile);
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws IOException
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

//    public static String base64Image2Img(String base64ImgData) {
//        String ext = base64ImgData.substring(base64ImgData.indexOf("data:image/"), base64ImgData.indexOf(";"));
//        ext = ext.substring(ext.indexOf("/") + 1);
//        base64ImgData = base64ImgData.replace("data:image/" + ext + ";base64,", "");
//        if ("jpeg".equalsIgnoreCase(ext)) {// data:image/jpeg;base64,base64编码的jpeg图片数据
//            ext = "jpg";
//        } else if ("x-icon".equalsIgnoreCase(ext)) {// data:image/x-icon;base64,base64编码的icon图片数据
//            ext = "ico";
//        }
//        String fileName = UUIDGenerator.generate() + "." + ext;// 待存储的文件名
//        try {
////            String path = this.getClass().getResource("/").getPath().replace("/WEB-INF/classes/", "");
//            String pathname = "/upload/" + DateUtil.getNowStringDate();
//            File targetFile = new File(path + pathname);
//            if (!targetFile.exists()) {
//                targetFile.mkdirs();
//            }
//            String filePath = path + pathname + "/" + fileName;// 图片路径
//            FileOutputStream os = new FileOutputStream(filePath);
//            os.write(Base64.decodeBase64(base64ImgData));
//            os.close();
////            String basePath = request.getRequestURL().toString().replace(request.getRequestURI().substring(request.getContextPath().length()), "");
//            return  pathname + "/" + fileName;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * 网络图片转换Base64的方法
     *
     * @param netImagePath
     */
    public static String netImageToBase64(String netImagePath) {
        String strNetImageToBase64 = null;
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(netImagePath);
            final byte[] by = new byte[1024];
            // 创建链接
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);


            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            strNetImageToBase64 = encoder.encode(data.toByteArray());
//            System.out.println("网络图片转换Base64:" + strNetImageToBase64);
            // 关闭流
            is.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return strNetImageToBase64;
    }


    /***
     * 将base64封装成ImageEntity对象
     * @param base64CodeStr
     * @return
     */
    private static ImageEntity getImage(String base64CodeStr) {
        if (StringUtils.isBlank(base64CodeStr)) {
            return null;
        }
        ImageEntity image = new ImageEntity();
        image.setHeight(150);
        image.setWidth(550);
        base64CodeStr = base64CodeStr.replaceAll("data:image/png;base64,", "");
        try {
            BASE64Decoder decode = new BASE64Decoder();
            byte[] b = decode.decodeBuffer(base64CodeStr.trim());
            image.setData(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setType(ImageEntity.Data);
        return image;
    }


    /***
     * 根据图片网络地址获取ImageEntity
     * imageUrl 图片网址
     * @return
     */
    public static ImageEntity getImageByURL(String imageUrl) {
        String base64CodeStr = netImageToBase64(imageUrl);
        ImageEntity image = getImage(base64CodeStr);
        return image;

    }

    public static void main(String[] args) {
        String url = "https://images0.cnblogs.com/blog/536814/201412/051633343733092.png";
        byte[] b = getImageFromNetByUrl(url);
        System.out.println(b);
    }


}
