package com.mz.common.util;


public class FileTypeUtil {
    //   文件类型 1图片2文档3视频4音乐0无法识别
    public static Integer fileType(String fileName) {
        if (fileName == null) {
//            fileName = "文件名为空！";
            return 0;
        } else {
// 获取文件后缀名并转化为写，用于后续比较
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            System.out.println("--------fileType-----------------" + fileType);
// 创建图片类型数组
            String[] img = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
                    "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};
            for (int i = 0; i < img.length; i++) {
                if (img[i].equals(fileType)) {
//                    return "图片";
                    return 1;
                }
            }
// 创建文档类型数组
            String[] document = {"txt", "doc", "docx", "xls", "htm", "html", "jsp", "rtf", "wpd", "pdf", "ppt"};
            for (int i = 0; i < document.length; i++) {
                if (document[i].equals(fileType)) {
//                    return "文档";
                    return 2;
                }
            }
// 创建视频类型数组
            String[] video = {"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};
            for (int i = 0; i < video.length; i++) {
                if (video[i].equals(fileType)) {
//                    return "视频";
                    return 3;
                }
            }
// 创建音乐类型数组
            String[] music = {"mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg",
                    "m4a", "vqf"};
            for (int i = 0; i < music.length; i++) {
                if (music[i].equals(fileType)) {
//                    return "音乐";
                    return 4;
                }
            }


        }
//        return "其他";//无法识别
        return 0;
    }
}
