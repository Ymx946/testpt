package com.mz.common.util;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Slf4j
@Component
public class LocalUploadUtil {
    @Value("${localUrl}")
    private String localUrl;

    @Value("${spring.profiles.active:dev}")
    private String profile;

    //设置统一图片后缀名
    private String suffixName;

    public static String getSoleFileName(String fileName) {
        String extName = "";
        if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0)) {
            extName = fileName.substring(fileName.lastIndexOf("."));
        }
        fileName = UUID.randomUUID().toString().replace("-", "") + extName;
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

    public AttaVo uploadFile(MultipartFile clientFile, String tenantId, String filePath, String fileFolder) throws Exception {
        clientFile.getSize();
        AttaVo attaVo = new AttaVo();
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = "defaulturl";
        }
        boolean flag = false;//判断上传文件格式是否正确
        String fileName = clientFile.getOriginalFilename();//文件名称
        attaVo.setOldName(fileName);
        InputStream inputStream = null;
        FileOutputStream fileOut = null;
        Path tempfile = null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        //验证拓展名
        String wjhz = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀

        tempfile = Files.createTempFile(getUUID(), prefix);
        clientFile.transferTo(tempfile);
        File tempYstp = null;
        tempYstp = tempfile.toFile();
        BufferedImage bufferedImage = ImageIO.read(tempYstp); // 通过临时文件获取图片流
        EnumForm[] form = EnumForm.values();
        for (EnumForm e : form) {
            if (e.toString().equalsIgnoreCase(wjhz)) {
                flag = true;
                break;
            }
        }
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
            log.info("不存在此格式");
        } else {
            // 上传文件
            if (tempfile == null) {
                log.info("tempfile为空");
            } else {
                try {
                    fileName = getSoleFileName(fileName);//生成新的文件名-避免文件名重复覆盖
                    String baseUrl = filePath + fileFolder + tenantId + File.separator;//文件路径
                    File baseFile = new File(baseUrl);//路径创建
                    if (!baseFile.exists()) {
                        baseFile.mkdirs();
                    }
                    File uploadFile = new File(baseUrl + fileName);//完整文件路径
                    //使用输入流读取前台的file文件
                    InputStream is = new FileInputStream(tempYstp);
                    //循环读取输入流文件内容，通过输出流将内容写入新文件
                    OutputStream os = new FileOutputStream(uploadFile);
                    byte[] buffer = new byte[1024];
                    int cnt = 0;
                    while ((cnt = is.read(buffer)) > 0) {
                        os.write(buffer, 0, cnt);
                    }
                    //关闭输入输出流
                    os.close();
                    is.close();
                    attaVo.setUrl(localUrl + fileFolder + tenantId + File.separator + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOut != null) {
                            fileOut.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return attaVo;
    }


    public enum EnumForm {
        JPG, PNG, ZIP, RAR, TXT, PPT, DOC, DOCX, PDF, XLS, XLSX, PPTX, JPEG, GIF, MP3, MP4, M4A, OGG, FLV, AVI, WMV, RMVB, MOV, MPEG,AMR ,AWB, JSON,WAV,AAC
    }

    public enum EnumForm1 {
        MP3, MP4, M4A
    }

    public enum EnumForm2 {
        JPG, PNG, JPEG, GIF
    }
}
