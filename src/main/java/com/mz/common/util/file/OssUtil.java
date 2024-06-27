package com.mz.common.util.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.util.AttaVo;
import com.mz.common.util.AudioUtil;
import com.mz.common.util.StringFormatUtil;
import com.mz.common.util.WebUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Component
public class OssUtil {
    private static final Logger log = LogManager.getLogger(OssUtil.class);
    private static final String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static final String accessId = "LTAI5tKrjAwJCiLmoFWkkx4S";
    private static final String accessKey = "vVx6xFfGSEa4wh4fLM5OJraMoUFYtZ";
    private static OSSClient client = null;

    static {
        try {
            client = new OSSClient(endpoint, accessId, accessKey);
            log.info(" -------- OSSClient  初始化 success --------");
        } catch (java.lang.Exception e) {
            log.error("OSSClient 初始化失败，原因：", e);
        }
    }

    @Value("${aliyun.oss.osshost}")
    private String osshost;
    @Value("${aliyun.oss.bucketname}")
    private String bucketName;
    @Value("${aliyun.oss.basicurl}")
    private String basicUrl;
    @Value("${file.filePath}")// nginx路径
    private String filePath;
    @Value("${file.fileFolder}")// 文件夹路径
    private String fileFolder;
    @Value("${spring.profiles.active}")
    private String profile;
    //设置统一图片后缀名
    private String suffixName;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(ImageIO.getReaderFileSuffixes()));
    }

    public static String getSoleFileName(String fileName) {
        String fileRawName = "", extName = "";
        if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0)) {
            fileRawName = fileName.substring(0, fileName.lastIndexOf("."));
            extName = fileName.substring(fileName.lastIndexOf("."));
        }
//        fileName = UUID.randomUUID().toString().replace("-", "") + extName;
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

    public static OSSClient getInstance() {
        return client;
    }

    /**
     * 秒转hh:mm:ss格式
     *
     * @param secString 秒字符串
     * @return hh:mm:ss
     * String
     */
    public String secondsToFormat(String secString) {
        int seconds = Integer.parseInt(secString);
        int hour = 0;
        int min = 0;
        int second = seconds;
        String result = "";
        if (seconds > 60) {   //是否大于零
            min = seconds / 60;  //分钟
            second = seconds % 60;  //秒
            if (min > 60) {   //存在时
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

        } else {
            result += "00:";
        }
        String secondtr = StringFormatUtil.stringCompl(String.valueOf(second), 2);
        result += secondtr;
        return result;
    }

    public AttaVo uploadFile(MultipartFile file, String tenantId, Boolean needWH, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = "defaulturl";
        }
        AttaVo attaVo = new AttaVo();
        boolean flag = false;//判断上传文件格式是否正确
        String fileName = file.getOriginalFilename();
        attaVo.setOldName(fileName);
        String imgurl = "";
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        Path tempfile = null;
        if (!file.isEmpty()) {
//            String tempPath = findServerPath(request) + "temp" + File.separator;
//            File tempFile = new File(tempPath);
//            if (!tempFile.exists()) {
//                tempFile.mkdirs();
//            }
//            final File tempfile = File.createTempFile(getUUID(), prefix, tempFile);
//            tempfile = Files.createTempFile(getUUID(), prefix);
            tempfile = Files.createTempFile(fileName + "_" + new Date().getTime(), prefix);
            file.transferTo(tempfile);

            InputStream in;
            File tempYstp = tempfile.toFile();
            if (Boolean.TRUE.equals(needWH)) {
                BufferedImage bufferedImage = ImageIO.read(tempYstp); // 通过临时文件获取图片流
                if (bufferedImage == null) {
                    FileInputStream filestr = new FileInputStream(tempYstp);
                    byte[] bytes = new byte[30];
                    filestr.read(bytes, 0, bytes.length);
                    int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
                    int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
                    attaVo.setWidth(width);
                    attaVo.setHeight(height);
                } else {
                    attaVo.setWidth(bufferedImage.getWidth());
                    attaVo.setHeight(bufferedImage.getHeight());
                }
            }
            in = new FileInputStream(tempYstp);
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀
            try {
                // 拿不到枚举值说明不存在
                OssUpLoadFile.EnumForm enumForm = OssUpLoadFile.EnumForm.valueOf(ext.toUpperCase());
                if (ObjectUtil.isNotEmpty(enumForm)) {
                    flag = true;
                }
            } catch (IllegalArgumentException e) {
                flag = false;
            }
            if (ext.equalsIgnoreCase("MP3") ||
                    ext.equalsIgnoreCase("MP4") ||
                    ext.equalsIgnoreCase("AVI") ||
                    ext.equalsIgnoreCase("MKV") ||
                    ext.equalsIgnoreCase("FLV") ||
                    ext.equalsIgnoreCase("WMV")
            ) {
                Float mp3Duration = AudioUtil.getMp3Duration(tempYstp.getAbsolutePath());
                attaVo.setTimeLong(mp3Duration.longValue());
                attaVo.setTimeLongStr(DateUtil.secondToTime(mp3Duration.intValue()));
            }

            if (!flag) {
                log.info("不存在此格式");
            } else {
                // 上传文件
                if (tempfile == null) {
                    log.info("tempfile为空");
                } else {
                    fileName = getSoleFileName(fileName);
                    imgurl = OssUpLoadFile.uploadFile(in, fileName, osshost, basicUrl + tenantId + "/", bucketName);
                }
            }
            in.close(); // 关闭流
            if (tempYstp.exists()) {
                tempYstp.delete();
            }
            if (tempYstp != null && tempYstp.exists()) {
                tempYstp.delete();
            }
        }

        if (!flag) {
            return null;
        } else {
            attaVo.setUrl(imgurl);
            return attaVo;
        }
    }

    public AttaVo uploadFileForFill(MultipartFile file, String tenantId, String setId, Integer type, String linkId, Boolean needWH, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = "defaulturl";
        }
        AttaVo attaVo = new AttaVo();
        boolean flag = false;//判断上传文件格式是否正确
        String fileName = file.getOriginalFilename();
        attaVo.setOldName(fileName);
        String imgurl = "";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        Path tempfile = null;
        if (!file.isEmpty()) {
//            String tempPath = findServerPath(request) + "temp" + File.separator;
//            File tempFile = new File(tempPath);
//            if (!tempFile.exists()) {
//                tempFile.mkdirs();
//            }
//            final File tempfile = File.createTempFile(getUUID(), prefix, tempFile);
            tempfile = Files.createTempFile(fileName + "_" + new Date().getTime(), suffix);
            file.transferTo(tempfile);

            InputStream in;
            File tempYstp = tempfile.toFile();
            if (Boolean.TRUE.equals(needWH)) {
                BufferedImage bufferedImage = ImageIO.read(tempYstp); // 通过临时文件获取图片流
                if (bufferedImage == null) {
                    FileInputStream filestr = new FileInputStream(tempYstp);
                    byte[] bytes = new byte[30];
                    filestr.read(bytes, 0, bytes.length);
                    int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
                    int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
                    attaVo.setWidth(width);
                    attaVo.setHeight(height);
                } else {
                    attaVo.setWidth(bufferedImage.getWidth());
                    attaVo.setHeight(bufferedImage.getHeight());
                }
            }
            in = new FileInputStream(tempYstp);
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀
            try {
                // 拿不到枚举值说明不存在
                OssUpLoadFile.EnumForm enumForm = OssUpLoadFile.EnumForm.valueOf(ext.toUpperCase());
                if (ObjectUtil.isNotEmpty(enumForm)) {
                    flag = true;
                }
            } catch (IllegalArgumentException e) {
                flag = false;
            }
            if (ext.equalsIgnoreCase("MP3") ||
                    ext.equalsIgnoreCase("MP4") ||
                    ext.equalsIgnoreCase("AVI") ||
                    ext.equalsIgnoreCase("MKV") ||
                    ext.equalsIgnoreCase("FLV") ||
                    ext.equalsIgnoreCase("WMV")
            ) {
                Float mp3Duration = AudioUtil.getMp3Duration(tempYstp.getAbsolutePath());
                attaVo.setTimeLong(mp3Duration.longValue());
                attaVo.setTimeLongStr(DateUtil.secondToTime(mp3Duration.intValue()));
            }

            if (!flag) {
                log.info("不存在此格式");
            } else {
                // 上传文件
                if (tempfile == null) {
                    log.info("tempfile为空");
                } else {
                    fileName = getSoleFileName(fileName);
//                    mzsz/server/images/
                    String basicurltemp = "brand/" + tenantId + "/" + setId + "/" + "type" + type + "/" + linkId + "/";
                    imgurl = OssUpLoadFile.uploadFile(in, fileName, osshost, basicurltemp, bucketName);
                }
            }

            in.close(); // 关闭流
            if (tempYstp.exists()) {
                tempYstp.delete();
            }
            if (tempYstp != null && tempYstp.exists()) {
                tempYstp.delete();
            }
        }
        if (!flag) {
            return null;
        } else {
            attaVo.setUrl(imgurl);
            return attaVo;
        }
    }


    //变更大小上传
    public AttaVo uploadFileChangeSize(MultipartFile file, String tenantId, HttpServletRequest request) throws Exception {
        AttaVo attaVo = new AttaVo();
        boolean flag = false;//判断上传文件格式是否正确
        long size = file.getSize(); //判断文件大小
        String fileName = file.getOriginalFilename();
        attaVo.setOldName(fileName);
        String imgurl = "";
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        Path tempfile = null;
        if (!file.isEmpty()) {
//            tempfile = Files.createTempFile(getUUID(), prefix);
            tempfile = Files.createTempFile(fileName + "_" + new Date().getTime(), prefix);
            file.transferTo(tempfile);
            InputStream resIn;
            InputStream in;
            File tempYstp = null;
            tempYstp = tempfile.toFile();
//           //验证拓展名
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀
            if ("png".equalsIgnoreCase(ext)) {
                suffixName = "png";
            } else {
                suffixName = "jpg";
            }
            String fileLocalPath = "";
            if ("dev".equalsIgnoreCase(profile)) {
                fileLocalPath = System.getProperty("user.dir") + "/src/main/resources/templates/" + "temp." + suffixName;
            } else {
                fileLocalPath = this.getClass().getResource("/").getPath() + "temp." + suffixName;
            }

            try {
                // 拿不到枚举值说明不存在
                OssUpLoadFile.EnumForm enumForm = OssUpLoadFile.EnumForm.valueOf(ext.toUpperCase());
                if (ObjectUtil.isNotEmpty(enumForm)) {
                    flag = true;
                }
            } catch (IllegalArgumentException e) {
                flag = false;
            }

            if (ext.equalsIgnoreCase("MP3") ||
                    ext.equalsIgnoreCase("MP4") ||
                    ext.equalsIgnoreCase("AVI") ||
                    ext.equalsIgnoreCase("MKV") ||
                    ext.equalsIgnoreCase("FLV") ||
                    ext.equalsIgnoreCase("WMV")
            ) {
                Float mp3Duration = AudioUtil.getMp3Duration(tempYstp.getAbsolutePath());
                attaVo.setTimeLong(mp3Duration.longValue());
                attaVo.setTimeLongStr(DateUtil.secondToTime(mp3Duration.intValue()));
            }

            if (!flag) {
                log.info("不存在此格式");
            } else {
                fileName = getSoleFileName(fileName);
                BufferedImage bufferedImage = ImageIO.read(tempYstp); // 通过临时文件获取图片流
                if (bufferedImage == null) {//不是图片
                    in = new FileInputStream(tempYstp);
                    byte[] bytes = new byte[30];
                    in.read(bytes, 0, bytes.length);
                    int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
                    int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
                    attaVo.setWidth(width);
                    attaVo.setHeight(height);
                    imgurl = OssUpLoadFile.uploadFile(in, fileName, osshost, basicUrl + tenantId + "/", bucketName);
                    in.close(); // 关闭流
                } else {//是图片。则压缩
                    File tempFile = new File(fileLocalPath); //上传是从项目中拿到图片
                    FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
                    if (size >= 1048576) {
                        //压缩图片// 按照比例进行缩小和放大
                        Thumbnails.of(fileLocalPath).scale(0.8f).outputQuality(0.25f).toFile(fileLocalPath);
                        //压缩图片// 按照宽高进行缩小和放大（会遵循原图高宽比例）
                        //Thumbnails.of(fileLocalPath).size(500,500);
                        //压缩图片// 按照指定大小进行缩放（不遵循原图比例）
                        //Thumbnails.of(fileLocalPath).keepAspectRatio(false).toFile(fileLocalPath);
                    }
                    //获取压缩后的图片
                    File file1 = new File(fileLocalPath);
                    BufferedImage bufferedImage1 = ImageIO.read(file1); // 通过临时文件获取图片流
                    attaVo.setWidth(bufferedImage1.getWidth());
                    attaVo.setHeight(bufferedImage1.getHeight());
                    resIn = new FileInputStream(file1);
                    imgurl = OssUpLoadFile.uploadFile(resIn, fileName, osshost, basicUrl + tenantId + "/", bucketName);
                    resIn.close(); // 关闭流
                    if (file1 != null && file1.exists()) {
                        file1.delete();
                    }
                }
            }
            if (tempYstp.exists()) {
                tempYstp.delete();
            }
            if (tempYstp != null && tempYstp.exists()) {
                tempYstp.delete();
            }
        }
        if (!flag) {
            return null;
        } else {
            attaVo.setUrl(imgurl);
            return attaVo;
        }
    }

    public BasePicVo batchUploadFile(MultipartFile[] clientFile, String tenantId, Boolean needWH, HttpServletRequest request) throws Exception {
        BasePicVo basePicVo = new BasePicVo();
        String[] urlArr = new String[clientFile.length];
        String[] oldNameArr = new String[clientFile.length];
        Float[] sizeArr = new Float[clientFile.length];
        Integer[] heightArr = new Integer[clientFile.length];
        Integer[] widthArr = new Integer[clientFile.length];
        int i = 0;
        for (MultipartFile file : clientFile) {
//            boolean flag = false;
            String fileName = file.getOriginalFilename();
            oldNameArr[i] = fileName;
            String imgurl = "";
//            String prefix = fileName.substring(fileName.lastIndexOf("."));

            Float size = Float.parseFloat(String.valueOf(file.getSize())) / 1024;
            BigDecimal b = new BigDecimal(size);
            size = b.setScale(2, RoundingMode.HALF_UP).floatValue();
            sizeArr[i] = size;

            if (!file.isEmpty()) {
//                String tempPath = findServerPath(request) + "temp" + File.separator;
//                File tempFile = new File(tempPath);
//                if (!tempFile.exists()) {
//                    tempFile.mkdirs();
//                }
//                final File tempfile = File.createTempFile(getUUID(), prefix, tempFile);
//                // MultipartFile to File
//                file.transferTo(tempfile);

//                InputStream in;
//                File tempYstp = null;
//                tempYstp = tempfile;
//                in = file.getInputStream();

//                String wjhz = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀
//                EnumForm[] form = EnumForm.values();
//                for (EnumForm e : form) {
//                    if (e.toString().equalsIgnoreCase(wjhz)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    //log.info("不存在此格式");
//                } else {
                // 上传文件
//                    if (tempfile == null) {
//                    } else {
//                        fileName = getSoleFileName(fileName);
                AttaVo attaVo = uploadFile(file, tenantId, needWH, request);
                if (ObjectUtil.isNotEmpty(attaVo) && ObjectUtil.isNotEmpty(attaVo.getUrl())
                        && ObjectUtil.isNotEmpty(attaVo.getWidth()) && ObjectUtil.isNotEmpty(attaVo.getHeight())) {
                    imgurl = attaVo.getUrl();
                    heightArr[i] = attaVo.getHeight();
                    widthArr[i] = attaVo.getWidth();
                }
            }
//                    }
//                }

//                in.close(); // 关闭流
//                if (tempfile.exists()) {
//                    tempfile.delete();
//                }
//
//                if (tempYstp != null && tempYstp.exists()) {
//                    tempYstp.delete();
//                }
//            }

//            if (!flag) {
//                urlArr[i] = "";
//            } else {
            urlArr[i] = imgurl;
//            }
            i++;
        }
        basePicVo.setUrlArr(urlArr);
        basePicVo.setOldNameArr(oldNameArr);
        basePicVo.setSizeArr(sizeArr);
        basePicVo.setWidthArr(widthArr);
        basePicVo.setHeightArr(heightArr);
        return basePicVo;
    }

    /**
     * 上传OSS服务器文件 @Title: uploadFile
     *
     * @throws Exception 设定文件 @return String 返回类型 返回oss存放路径 @throws
     */
    public String uploadFile(InputStream in, String tenantId, String fileName) throws Exception {
        OSSClient ossClient = getInstance();
        try {
            ossClient.putObject(bucketName, basicUrl + tenantId + "/" + fileName, in);
            OSSObject object = ossClient.getObject(bucketName, basicUrl + tenantId + "/" + fileName);
            log.info("Size of the empty folder '" + object.getKey() + "' is " + object.getObjectMetadata().getContentLength());
            object.getObjectContent().close();
//            ossClient.shutdown();
            in.close();
            return osshost + basicUrl + tenantId + "/" + fileName;
            // return
        } catch (OSSException oe) {
            log.error("Error Message: " + oe.getErrorCode());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Error Message: " + ce.getMessage());
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
        }
        return null;
    }

    /**
     * 获取指定目录下的文件列表
     *
     * @param folderKeyPrefix
     * @return
     */
    public List<String> getDirFileList(String folderKeyPrefix) {
        List<String> urlList = new ArrayList<>();
        OSSClient ossClient = getInstance();
        try {
            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(folderKeyPrefix));
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String key = objectSummary.getKey();
                log.info("File: " + key);
                urlList.add(osshost + key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlList;
    }

    /**
     * 检查oss文件是否存在
     *
     * @param url
     * @return
     */
    public boolean doesObjectExist(String url) {
        url = url.replace(osshost, "");
        OSSClient ossClient = getInstance();
        boolean result = ossClient.doesObjectExist(bucketName, url);
        // 关闭OSSClient。
//        ossClient.shutdown();
        return result;
    }

    /**
     * 删除单个图片
     *
     * @param url
     */
    public void deleteImg(String url) {
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。（这里根据自己的地址改）
        url = url.replace(osshost, "");
        log.info("拆分后的路径" + url);
        // 创建OSSClient实例。
        OSSClient ossClient = getInstance();
        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, url);
        // 关闭OSSClient。
//        ossClient.shutdown();
    }

    /**
     * 获取文件类型
     * 0未知1音频2图片
     *
     * @param url
     */
    public int getFileType(String url) {
        int fileType = 0;
// 填写文件完整路径。文件完整路径中不能包含Bucket名称。（这里根据自己的地址改）
        String fileName = url.replace(osshost, "");
        String extName = "";
        if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0)) {
            extName = fileName.substring(fileName.lastIndexOf("."));
        }
        boolean flag1 = false;
        EnumForm1[] form = EnumForm1.values();
        for (EnumForm1 e : form) {
            if (e.toString().equalsIgnoreCase(extName)) {
                flag1 = true;
                break;
            }
        }
        if (flag1) {
            fileType = 1;
        }
        boolean flag2 = false;
        EnumForm2[] form2 = EnumForm2.values();
        for (EnumForm2 e : form2) {
            if (e.toString().equalsIgnoreCase(extName)) {
                flag2 = true;
                break;
            }
        }
        if (flag2) {
            fileType = 2;
        }
        return fileType;
    }

    /**
     * 根据指定目录生成zip文件下载
     *
     * @param folderKeyPrefix
     * @param zipFileName
     * @param delFileList
     * @param request
     * @return
     * @throws IOException
     */
    public String generateFileUrlZip(String folderKeyPrefix, String zipFileName, List<String> delFileList, HttpServletRequest request) throws IOException {
        OSSClient ossClient = getInstance();
        // 删除 OSS 上不存在的文件
        for (String url : delFileList) {
            ossClient.deleteObject(bucketName, url);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(outputStream);
        try {
            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(folderKeyPrefix));
            while (objectListing != null) {
                for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    String key = objectSummary.getKey();
                    InputStream inputStream = ossClient.getObject(bucketName, key).getObjectContent();
                    key = key.replace(folderKeyPrefix, "");
                    ArchiveEntry entry = new ZipArchiveEntry(key);
                    archiveOutputStream.putArchiveEntry(entry);
                    IOUtils.copy(inputStream, archiveOutputStream);
                    IOUtils.closeQuietly(inputStream);
                    archiveOutputStream.closeArchiveEntry();
                }
                objectListing = objectListing.isTruncated() ?
                        ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(folderKeyPrefix).withMarker(objectListing.getNextMarker())) : null;
            }
        } finally {
            IOUtils.closeQuietly(archiveOutputStream);
        }
        ossClient.putObject(bucketName, zipFileName, new ByteArrayInputStream(outputStream.toByteArray()));
        // 下载压缩文件到本地
        String tmpPath = "temp" + File.separator + zipFileName;
        String localServerPath = filePath + fileFolder + tmpPath;
        log.info("localServerPath: " + localServerPath);
        Path path = Paths.get(localServerPath);
        Files.copy(ossClient.getObject(bucketName, zipFileName).getObjectContent(), path, StandardCopyOption.REPLACE_EXISTING);
        String localServerPathUrl = fileFolder + tmpPath;
        String zipUrl = WebUtil.getBaseNoContextPath(request) + localServerPathUrl;
        log.info("zipUrl: " + zipUrl);
        return zipUrl;
    }

    public void generateFileUrlZipOssStream(String folderKeyPrefix, String zipFileName, List<String> delFileList,
                                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        OSSClient ossClient = getInstance();
        // 删除 OSS 上不存在的文件
        for (String url : delFileList) {
            ossClient.deleteObject(bucketName, url);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(outputStream);
        try {
            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(folderKeyPrefix));
            while (objectListing != null) {
                for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    String key = objectSummary.getKey();
                    InputStream inputStream = ossClient.getObject(bucketName, key).getObjectContent();
                    key = key.replace(folderKeyPrefix, "");
                    ArchiveEntry entry = new ZipArchiveEntry(key);
                    archiveOutputStream.putArchiveEntry(entry);
                    IOUtils.copy(inputStream, archiveOutputStream);
                    IOUtils.closeQuietly(inputStream);
                    archiveOutputStream.closeArchiveEntry();
                }
                objectListing = objectListing.isTruncated() ?
                        ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(folderKeyPrefix).withMarker(objectListing.getNextMarker())) : null;
            }
        } finally {
            IOUtils.closeQuietly(archiveOutputStream);
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ServletOutputStream out = response.getOutputStream();
        byte[] buff = new byte[10240];
        int length = 0;
        while ((length = byteArrayInputStream.read(buff)) > 0) {
            out.write(buff, 0, length);
        }
        byteArrayInputStream.close();
        out.close();
        out.flush();
        IoUtil.close(out);
    }

    public void generateFileZipStream(String folderKeyPrefix, String zipFileName, List<String> delFileList,
                                      HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        try {
            String zipUrl = generateFileUrlZip(folderKeyPrefix, zipFileName, delFileList, request);
            if (StringUtil.isEmpty(zipUrl)) {
                return;
            }
            URL url = new URL(zipUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            ServletOutputStream out = response.getOutputStream();
            byte[] buff = new byte[10240];
            int length = 0;
            while ((length = inStream.read(buff)) > 0) {
                out.write(buff, 0, length);
            }
            inStream.close();
            out.close();
            out.flush();
            IoUtil.close(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * @param max_wi 图片修改后最大的宽
//     * @param max_he 图片修改后最大的高
//     * @return
//     * @throws Exception
//     * @Description 更改图片内容的大小 byte【】 类型
//     */
//    public File imageChangeSize(BufferedImage bufImg, int max_wi,int max_he,String prefix) throws Exception {
//        float rate = 1;
//        /*图片的原始宽 120*/
//        int oldwi;
//        /*图片的原始高 120*/
//        int oldhe;
//        /*图片修改后的宽 0*/
//        int new_wi = 0;
//        /*图片修改后的高 0*/
//        int new_he = 0;
//        /*图片的原始宽度*/
//        oldwi = bufImg.getWidth();
//        /*图片的原始高度*/
//        oldhe = bufImg.getHeight();
//        //
//        rate = (float) oldwi / (float) oldhe;
//        /*如果图片的原宽大于最大宽度，并且原高小于等于最大高度。则证明图片过宽了，将图片宽度设置为最大宽度，此时需要等比例减小高度*/
//        if (oldwi > max_wi) {
//            new_wi = max_wi;
//            new_he = new Float((float) new_wi / rate).intValue();
//        }else if (oldhe > max_he) {
//            new_he = max_he;
//            new_wi = new Float((float) new_wi * rate).intValue();
//        }else{
//            new_wi = oldwi;
//            new_he = oldhe;
//        }
//        System.err.println("原宽度：" + oldwi + "原高度：" + oldhe + "_" + rate);
//        /*开始改变大小*/
//        BufferedImage bf = new BufferedImage(new_wi, new_he, BufferedImage.TYPE_INT_RGB);
//        bf.getGraphics().drawImage(bufImg, 0, 0, new_wi, new_he, null);
//        String fileLocalPath = "";
//        if ("dev".equalsIgnoreCase(profile)) {
//            fileLocalPath = System.getProperty("user.dir") + "/src/main/resources/templates/" + "temp."+prefix;
//        }else{
//           fileLocalPath = this.getClass().getResource("/").getPath() + "temp."+prefix;
//        }
//        File file = new File(fileLocalPath);
//        ImageIO.write(bf, prefix, file);
//
//        return file;
//    }

//    public String  commpressPicForScaleSize(String srcPath, String desPath,
//                                                  long desFileSize, double accuracy) {
//        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {
//            return null;
//        }
//        if (!new File(srcPath).exists()) {
//            return null;
//        }
//        try {
//            File srcFile = new File(srcPath);
//            long srcFileSize = srcFile.length();
//            System.out.println("源图片：" + srcPath + "，大小：" + srcFileSize / 1024
//                    + "kb");
//
//            // 1、先转换成jpg
//            Thumbnails.of(srcPath).scale(1f).toFile(desPath);
//            //按照比例进行缩放
//            imgScaleSize(desPath, desFileSize, accuracy);
//
//            File desFile = new File(desPath);
//            System.out.println("目标图片：" + desPath + "，大小" + desFile.length()
//                    / 1024 + "kb");
//            System.out.println("图片压缩完成！");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return desPath;
//    }
//    /**
//     * 图片尺寸不变，压缩文件大小
//     * @param desPath
//     *               目标图片地址
//     * @param desFileSize
//     *               指定图片大小,单位kb
//     * @param accuracy
//     *               精度,递归压缩比率,建议小于0.9
//     */
//    private static void imgScaleSize(String desPath, long desFileSize,
//                                     double accuracy) throws IOException {
//        File fileName=new File(desPath);
//        long fileNameSize=fileName.length();
//        //判断大小，如果小于指定大小，不压缩；如果大于等于指定大小，压缩
//        if(fileNameSize<=desFileSize*1024){
//            return;
//        }
//        //图片尺寸不变，压缩图片文件大小
//        //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
//        Thumbnails.of(desPath).scale(1f).outputQuality(accuracy).toFile(desPath);
//        System.out.println("图片尺寸不变，压缩文件大小");
//        imgScaleSize(desPath, desFileSize, accuracy);
//    }

    public enum EnumForm {
        JPG, PNG, ZIP, RAR, TXT, PPT, DOC, DOCX, PDF, XLS, XLSX, PPTX, JPEG, GIF, MP3, MP4, M4A, OGG, FLV, AVI, WMV, RMVB, MOV, MPEG, AMR, AWB, JSON, WAV, AAC
    }

    public enum EnumForm1 {
        MP3, MP4, M4A
    }

    public enum EnumForm2 {
        JPG, PNG, JPEG, GIF
    }


}
