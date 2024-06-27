package com.mz.common.util.file;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.mz.common.util.AttaVo;
import com.mz.common.util.AudioUtil;
import com.mz.common.util.StringFormatUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

public class OssUpLoadFile {

    public static AttaVo uploadFile(String endpoint, String accessId, String accessKey, MultipartFile file, HttpServletRequest request, String osshost, String bucketName, String basicUrl) throws Exception {
        AttaVo attaVo = new AttaVo();
        boolean flag = false;// 判断上传文件格式是否正确
        String fileName = file.getOriginalFilename();
        attaVo.setOldName(fileName);
        String imgurl = "";
        String fileRawName = fileName.substring(0, fileName.lastIndexOf("."));
        String extName = fileName.substring(fileName.lastIndexOf("."));

        if (!file.isEmpty()) {
            String serverPath = findServerPath(request);
            String tempPath = serverPath + "temp" + File.separator;
            File tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            // 用uuid作为文件名，防止生成的临时文件重复
            final File tempfile = File.createTempFile(fileRawName + "_" + new Date().getTime(), extName, tempFile);
            // MultipartFile to File
            file.transferTo(tempfile);

            InputStream in;
            File tempYstp = null;
            tempYstp = tempfile;

            // 获取文件长宽
            BufferedImage bufferedImage = ImageIO.read(tempfile); // 通过临时文件获取图片流
            if (bufferedImage == null) {
                FileInputStream filestr = new FileInputStream(tempfile);
                byte[] bytes = new byte[30];
                filestr.read(bytes, 0, bytes.length);
                int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
                int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
                attaVo.setWidth(width);
                attaVo.setHeight(height);
                // 证明上传的文件不是图片，获取图片流失败，不进行下面的操作
//					attaVo.setWidth(0);
//					attaVo.setHeight(0);
            } else {
                attaVo.setWidth(bufferedImage.getWidth());
                attaVo.setHeight(bufferedImage.getHeight());
            }
            //
            in = new FileInputStream(tempfile);

            // 抛异常出来后，直接不压缩上传
            // 判断上传文件格式
            String wjhz = fileName.substring(fileName.lastIndexOf(".") + 1);// 上传文件后缀
            EnumForm[] form = EnumForm.values();
            for (EnumForm e : form) {
                if (e.toString().equalsIgnoreCase(wjhz)) {
                    flag = true;
                    break;
                }
            }
            if (wjhz.equalsIgnoreCase("MP3")) {
//                Encoder encoder = new Encoder();
//                MultimediaInfo m;
//                m = encoder.getInfo(tempYstp);
//                long duration = m.getDuration() / 1000;
//                attaVo.setTimeLong(duration);
//                attaVo.setTimeLongStr(secondsToFormat(duration + ""));
                Float mp3Duration = AudioUtil.getMp3Duration(tempYstp.getAbsolutePath());
                attaVo.setTimeLong(mp3Duration.longValue());
                attaVo.setTimeLongStr(DateUtil.secondToTime(mp3Duration.intValue()));
            }

            if (!flag) {
                System.out.println("不存在此格式");
            } else {
                // 上传文件
                if (tempfile == null) {
                    System.out.println("tempfile为空");
                } else {
                    fileName = getSoleFileName(fileName);
//							String osshost = "https://lgcdn.lsligeng.com/";
//							String bucketName = "lgoss";
//							String basicUrl = "3301/lgcms/upload/images/";
                    imgurl = OssUpLoadFile.uploadFile(in, fileName, osshost, basicUrl, bucketName);
                }
            }

            in.close(); // 关闭流

            if (tempfile.exists()) {
                tempfile.delete();
            }

            if (tempYstp != null && tempYstp.exists()) {
                tempYstp.delete();
            }
        }

        if (!flag) {// 文件格式不正确
            return null;
        } else {
            attaVo.setUrl(imgurl);
            return attaVo;
        }
    }

    public static BasePicVo batchUploadFile(String endpoint, String accessId, String accessKey, MultipartFile[] batchfile, HttpServletRequest request, String osshost, String bucketName, String basicUrl) throws Exception {
        BasePicVo basePicVo = new BasePicVo();
        String[] urlArr = new String[batchfile.length];
        String[] oldNameArr = new String[batchfile.length];
        Float[] sizeArr = new Float[batchfile.length];
        int i = 0;
        for (MultipartFile file : batchfile) {
            boolean flag = false;// 判断上传文件格式是否正确
            String fileName = file.getOriginalFilename();
            oldNameArr[i] = fileName;
            String imgurl = "";
            String fileRawName = fileName.substring(0, fileName.lastIndexOf("."));
            String extName = fileName.substring(fileName.lastIndexOf("."));

            Float size = Float.parseFloat(String.valueOf(file.getSize())) / 1024;
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, RoundingMode.HALF_UP).floatValue();
            sizeArr[i] = size;

            if (!file.isEmpty()) {
                String serverPath = findServerPath(request);
                String tempPath = serverPath + "temp" + File.separator;
                File tempFile = new File(tempPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                // 用uuid作为文件名，防止生成的临时文件重复
                final File tempfile = File.createTempFile(fileRawName + "_" + new Date().getTime(), extName, tempFile);
                // MultipartFile to File
                file.transferTo(tempfile);

                InputStream in;
                File tempYstp = null;
                tempYstp = tempfile;
                in = new FileInputStream(tempfile);

                // 抛异常出来后，直接不压缩上传
                // 判断上传文件格式
                String wjhz = fileName.substring(fileName.lastIndexOf(".") + 1);// 上传文件后缀
                EnumForm[] form = EnumForm.values();
                for (EnumForm e : form) {
                    if (e.toString().equalsIgnoreCase(wjhz)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    // System.out.println("不存在此格式");
                } else {
                    // 上传文件
                    if (tempfile == null) {
                    } else {
                        fileName = getSoleFileName(fileName);
//							String osshost = "https://lgcdn.lsligeng.com/";
//							String bucketName = "lgoss";
//							String basicUrl = "3301/lgcms/upload/images/";
                        imgurl = OssUpLoadFile.uploadFile(in, fileName, osshost, basicUrl, bucketName);
                    }
                }

                in.close(); // 关闭流
                if (tempfile.exists()) {
                    tempfile.delete();
                }

                if (tempYstp != null && tempYstp.exists()) {
                    tempYstp.delete();
                }
            }

            if (!flag) {// 文件格式不正确
                urlArr[i] = "";
            } else {
                urlArr[i] = imgurl;
            }
            i++;
        }
        basePicVo.setUrlArr(urlArr);
        basePicVo.setOldNameArr(oldNameArr);
        basePicVo.setSizeArr(sizeArr);
        return basePicVo;
    }

    /**
     * 上传OSS服务器文件 @Title: uploadFile
     *
     * @param fileContent spring 上传的文件 remotePath @param oss服务器二级目录
     * @throws Exception 设定文件 @return String 返回类型 返回oss存放路径 @throws
     */
    public static String uploadFile(InputStream fileContent, String fileName, String ossHost, String dir, String bucket) throws Exception {
        OSSClient ossClient = OssUtil.getInstance();
        try {
            // 随机名处理
            // fileName = new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
            ossClient.putObject(bucket, dir + fileName, fileContent);
            OSSObject object = ossClient.getObject(bucket, dir + fileName);
            System.out.println("Size of the empty folder '" + object.getKey() + "' is " + object.getObjectMetadata().getContentLength());
            object.getObjectContent().close();
            // 关闭OSSClient
//            ossClient.shutdown();
            // 关闭io流
            fileContent.close();
            return ossHost + dir + fileName;
            // return
        } catch (OSSException oe) {
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Exception e) {
            System.out.println("Error Message: " + e.getMessage());
        }
        return null;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType @Version1.0
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String contentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".apk")) {
            return "application/octet-stream";
        }
        return "text/html";
    }

    public static String getSoleFileName(String fileName) {
//        UUID uuid = UUID.randomUUID();
        String fileRawName = "", extName = "";
        if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0)) {
            fileRawName = fileName.substring(0, fileName.lastIndexOf("."));
            extName = fileName.substring(fileName.lastIndexOf("."));
        }
//        fileName = uuid.toString().replace("-", "") + extName;
        fileName = fileRawName + "_" + new Date().getTime() + extName;
        return fileName;
    }

    public static String findServerPath(HttpServletRequest request) {
        String classPath = request.getSession().getServletContext().getRealPath("");
        if (classPath.endsWith(File.separator)) {
            classPath = classPath.substring(0, classPath.length() - 1);
        }
        int i = classPath.lastIndexOf(File.separator);
        classPath = classPath.substring(0, i) + File.separator;
        if ("/".equals(File.separator)) {
            classPath = File.separator + classPath;
        }
        return classPath;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 秒转hh:mm:ss格式
     *
     * @param secString 秒字符串
     * @return hh:mm:ss
     * String
     */
    public static String secondsToFormat(String secString) {
        Integer seconds = Integer.parseInt(secString);
        Integer hour = 0;
        Integer min = 0;
        Integer second = seconds;
        String result = "";
        if (seconds > 60) {   // 是否大于零
            min = seconds / 60;  // 分钟
            second = seconds % 60;  // 秒
            if (min > 60) {   // 存在时
                hour = min / 60;
                min = min % 60;
            }
        }
        if (hour > 0) {
            String hourstr = StringFormatUtil.stringCompl(String.valueOf(hour), 2);
            result = hourstr + ":";
        } else {
            result = "00:";
        }
        if (min > 0) {
            String minstr = StringFormatUtil.stringCompl(String.valueOf(min), 2);
            result += minstr + ":";

        } else {  // 当分为0时,但是时有值,所以要显示,避免出现2时0秒现象
            result += "00:";
        }
        String secondtr = StringFormatUtil.stringCompl(String.valueOf(second), 2);
        result += secondtr;   // 秒必须出现无论是否大于零
        return result;
    }

    //    jpeg, jpg, png, zip, rar, txt, ppt, doc, docx, pdf, xls, xlsx, pptx, gif, mp3, mp4, m4a, ddm, rmvb, flv
    public enum EnumForm {
        JPG, PNG, BMP, ZIP, RAR, TXT, PPT, DOC, DOCX, PDF, XLS, XLSX, PPTX, JPEG, GIF, MP3, MP4, M4A, OGG, FLV, AVI, WMV, RMVB, MOV, MPEG, AMR, AWB, JSON, WAV, AAC
    }
}
