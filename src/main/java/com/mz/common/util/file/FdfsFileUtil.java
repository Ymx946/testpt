package com.mz.common.util.file;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.util.AttaVo;
import com.mz.common.util.AudioUtil;
import com.mz.common.util.StringFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;


@Slf4j
public class FdfsFileUtil {
    private static StorageClient storageClient = null;

    /**
     * 只加载一次.
     */
    static {
        try {
//            String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "fdfs_config.conf";
//            ClientGlobal.init(filePath);
            Properties properties = new Properties();
            InputStream resourceAsStream = FdfsFileUtil.class.getClassLoader().getResourceAsStream("fdfs_config.properties");
            properties.load(resourceAsStream);
//            ClientGlobal.initByProperties("fdfs_config.properties");
            ClientGlobal.initByProperties(properties);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param inputStream 上传的文件输入流
     * @param fileName    上传的文件原始名
     * @return
     */
    public static String[] uploadFile(InputStream inputStream, String fileName) {
        try {
            // 文件的元数据
            NameValuePair[] meta_list = new NameValuePair[2];
            // 第一组元数据，文件的原始名称
            meta_list[0] = new NameValuePair("file name", fileName);
            // 第二组元数据
            meta_list[1] = new NameValuePair("file length", String.valueOf(inputStream.available()));
            // 准备字节数组
            byte[] file_buff = null;
            if (inputStream != null) {
                // 查看文件的长度
                int len = inputStream.available();
                // 创建对应长度的字节数组
                file_buff = new byte[len];
                // 将输入流中的字节内容，读到字节数组中。
                inputStream.read(file_buff);
            }
            // 上传文件。参数含义：要上传的文件的内容（使用字节数组传递），上传的文件的类型（扩展名），元数据
            String[] fileids = storageClient.upload_file(file_buff, getFileExt(fileName), meta_list);
            return fileids;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param file     文件
     * @param fileName 文件名
     * @return 返回Null则为失败
     */
    public static String[] uploadFile(File file, String fileName) {
        FileInputStream fis = null;
        try {
            NameValuePair[] meta_list = null;
            fis = new FileInputStream(file);
            byte[] file_buff = null;
            if (fis != null) {
                int len = fis.available();
                file_buff = new byte[len];
                fis.read(file_buff);
            }
            String[] fileids = storageClient.upload_file(file_buff, getFileExt(fileName), meta_list);
            return fileids;
        } catch (Exception ex) {
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据组名和远程文件名来删除一个文件
     *
     * @param groupName      例如 "group1" 如果不指定该值，默认为group1
     * @param remoteFileName 例如"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg"
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String groupName, String remoteFileName) {
        try {
            int result = storageClient.delete_file(Optional.ofNullable(groupName).orElse("group1"), remoteFileName);
            return result;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 修改一个已经存在的文件
     *
     * @param oldGroupName 旧的组名
     * @param oldFileName  旧的文件名
     * @param file         新文件
     * @param fileName     新文件名
     * @return 返回空则为失败
     */
    public static String[] modifyFile(String oldGroupName, String oldFileName, File file, String fileName) {
        String[] fileids = null;
        try {
            // 先上传
            fileids = uploadFile(file, fileName);
            if (fileids == null) {
                return null;
            }
            // 再删除
            int delResult = deleteFile(oldGroupName, oldFileName);
            if (delResult != 0) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        return fileids;
    }

    /**
     * 文件下载
     *
     * @param groupName      卷名
     * @param remoteFileName 文件名
     * @return 返回一个流
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) {
        try {
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return inputStream;
        } catch (Exception ex) {
            return null;
        }
    }

    public static NameValuePair[] getMetaDate(String groupName, String remoteFileName) {
        try {
            NameValuePair[] nvp = storageClient.get_metadata(groupName, remoteFileName);
            return nvp;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件后缀名（不带点）.
     *
     * @return 如："jpg" or "".
     */
    private static String getFileExt(String fileName) {
        if (StringUtil.isEmpty(fileName) || !fileName.contains(".")) {
            return "";
        } else {
            return fileName.substring(fileName.lastIndexOf(".") + 1); // 不带最后的点
        }
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

    public static String getSoleFileName(String fileName) {
        String extName = "";
        if ((fileName.lastIndexOf(".") != -1) && (fileName.lastIndexOf(".") != 0)) {
            extName = fileName.substring(fileName.lastIndexOf("."));
        }
        fileName = UUID.randomUUID().toString().replace("-", "") + extName;
        return fileName;
    }

    public static AttaVo uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
        AttaVo attaVo = new AttaVo();
        boolean flag = false;//判断上传文件格式是否正确
        String fileName = file.getOriginalFilename();
        attaVo.setOldName(fileName);
        String imgurl = "";
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        if (!file.isEmpty()) {
            String tempPath = findServerPath(request) + "temp" + File.separator;
            File tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            final File tempfile = File.createTempFile(getUUID(), prefix, tempFile);
            file.transferTo(tempfile);

            InputStream in;
            File tempYstp = null;
            tempYstp = tempfile;

            //获取文件长宽
            BufferedImage bufferedImage = ImageIO.read(tempfile); // 通过临时文件获取图片流
            if (bufferedImage == null) {
                FileInputStream filestr = new FileInputStream(tempfile);
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
            in = new FileInputStream(tempfile);
            String wjhz = fileName.substring(fileName.lastIndexOf(".") + 1);//上传文件后缀
            OssUpLoadFile.EnumForm[] form = OssUpLoadFile.EnumForm.values();
            for (OssUpLoadFile.EnumForm e : form) {
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
                log.info("不存在此格式");
            } else {
                // 上传文件
                if (tempfile == null) {
                    log.info("tempfile为空");
                } else {
                    byte[] file_buff = null;
                    if (in != null) {
                        int len = in.available();
                        file_buff = new byte[len];
                        in.read(file_buff);
                    }
                    String[] fdsResult = storageClient.upload_file(file_buff, wjhz, null);
                    log.info("===fdsResult： " + JSON.toJSONString(fdsResult));
                    if (!StringUtils.isEmpty(fdsResult)) {
                        imgurl = fdsResult[0] + "/" + fdsResult[1];
                    }
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

        if (!flag) {
            return null;
        } else {
            attaVo.setUrl(imgurl);
            return attaVo;
        }
    }

    /**
     * 秒转hh:mm:ss格式
     *
     * @param secString 秒字符串
     * @return hh:mm:ss
     * String
     */
    public static String secondsToFormat(String secString) {
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
}

