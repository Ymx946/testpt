package com.mz.common.util;


import com.mz.common.ConstantsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 读取excel中的图片工具类
 */
@Slf4j
public class ReadImgUtil {

    public static Map<String, PictureData> getPictures(MultipartFile clientFile) throws IOException {
        String fileName = clientFile.getOriginalFilename();
        InputStream is = clientFile.getInputStream();
        Workbook workbook = null;
        if (fileName.endsWith(ConstantsUtil.EXCEL2003)) {
            workbook = new HSSFWorkbook(is);
        }
        if (fileName.endsWith(ConstantsUtil.EXCEL2007)) {
            workbook = new XSSFWorkbook(is);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, PictureData> picMap = new HashMap<>();
        Drawing<?> drawing = sheet.getDrawingPatriarch();
        if (drawing != null) {
            List<Picture> pictureList = StreamSupport.stream(sheet.getDrawingPatriarch().spliterator(), false)
                    .filter(Picture.class::isInstance)
                    .map(Picture.class::cast)
                    .collect(Collectors.toList());
            for (Picture picture : pictureList) {
                ClientAnchor clientAnchor = picture.getClientAnchor();
                PictureData pdata = picture.getPictureData();
                // 行号-列号
                String key = clientAnchor.getRow1() + "-" + clientAnchor.getCol1();
                log.info("key数据:{}", key);
                picMap.put(key, pdata);
            }
        }
        return picMap;
    }

    public static Map<String, PictureData> getPicturesMul(MultipartFile clientFile) throws IOException {
        String fileName = clientFile.getOriginalFilename();
        InputStream is = clientFile.getInputStream();
        Workbook workbook = null;
        if (fileName.endsWith(ConstantsUtil.EXCEL2003)) {
            workbook = new HSSFWorkbook(is);
        }
        if (fileName.endsWith(ConstantsUtil.EXCEL2007)) {
            workbook = new XSSFWorkbook(is);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, PictureData> picMap = new HashMap<>();
        Drawing<?> drawing = sheet.getDrawingPatriarch();
        if (drawing != null) {
            List<Picture> pictureList = StreamSupport.stream(sheet.getDrawingPatriarch().spliterator(), false)
                    .filter(Picture.class::isInstance)
                    .map(Picture.class::cast)
                    .collect(Collectors.toList());
            int i = 0;
            Map<String, Integer> map = new HashMap<>();
            for (Picture picture : pictureList) {
                ClientAnchor clientAnchor = picture.getClientAnchor();
                PictureData pdata = picture.getPictureData();
                Integer newI = map.get(clientAnchor.getRow1() + "-" + clientAnchor.getCol1());
                if (newI != null) {
                    i = newI.intValue() + 1;
                } else {
                    i = 0;
                }
                map.put(clientAnchor.getRow1() + "-" + clientAnchor.getCol1(), i);
                // 行号-列号
                String key = clientAnchor.getRow1() + "-" + clientAnchor.getCol1() + "-" + i;
                log.info("key数据:{}", key);
                picMap.put(key, pdata);
            }
        }
        return picMap;
    }

    public static Map<String, PictureData> getPictures(Sheet sheet) {
        Map<String, PictureData> picMap = new HashMap<>();
        List<Picture> pictureList = StreamSupport.stream(sheet.getDrawingPatriarch().spliterator(), false)
                .filter(Picture.class::isInstance)
                .map(Picture.class::cast)
                .collect(Collectors.toList());
        for (Picture picture : pictureList) {
            ClientAnchor clientAnchor = picture.getClientAnchor();
            PictureData pdata = picture.getPictureData();
            // 行号-列号
            String key = clientAnchor.getRow1() + "-" + clientAnchor.getCol1();
            log.info("key数据:{}", key);
            picMap.put(key, pdata);
        }
        return picMap;
    }
}

