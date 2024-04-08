package com.mz.common.util;


import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WriteImgUtil {
    /**
     * 把图片写入excel中
     *
     * @param row        写入的行
     * @param col        写入的列
     * @param pictureUrl 可打开的图片的地址
     * @param workbook   工作簿
     * @param patriarch  绘图对象
     */
    public static void writeImg(Integer row, Integer col, String pictureUrl, Workbook workbook, Drawing<?> patriarch) {
        try {
            if (StringUtils.isNotBlank(pictureUrl)) {
                URL url = new URL(pictureUrl);
                //打开链接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置请求方式为"GET"
                conn.setRequestMethod("GET");
                //超时响应时间为5秒  q
                conn.setConnectTimeout(5 * 1000);
                //通过输入流获取图片数据
                InputStream inStream = conn.getInputStream();
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                byte[] data = readInputStream(inStream);
                //anchor主要用于设置图片的属性
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 1023, col.shortValue(), row.shortValue(), col.shortValue() + 1, row.shortValue() + 1);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                patriarch.createPicture(anchor, workbook.addPicture(data, XSSFWorkbook.PICTURE_TYPE_JPEG));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过输入流获得二进制图片数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}

