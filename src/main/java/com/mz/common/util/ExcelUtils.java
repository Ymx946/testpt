package com.mz.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.ExcelStringModel;
import com.mz.common.TitleModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangfang
 * @description
 * @date 2021/6/1 15:27
 **/
@Slf4j
public class ExcelUtils {
    private final static String EXCEL2003 = ".xls";
    private final static String EXCEL2007 = ".xlsx";

    /**
     * 使用表达式，去去掉参数前缀 $cglib_prop_
     */
    private static final Pattern COMPILE = Pattern.compile("^(.*)cglib_prop_(.*)$");

    /**
     * 生成excel文件-列表导出通用(传入List<实体类>取值正常)
     *
     * @param fieldList 表头
     * @param dataList  数据内容
     */
    public static void writeExcelRowSpanList(List<Object> dataList, List<String> fieldList, String rowSpanText, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("dataList length: " + dataList.size());
        int rowMaxCache = 100;
        //1、创建workbook
//        Workbook wb = new XSSFWorkbook();
//        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        XSSFWorkbook xssfWb = new XSSFWorkbook();
        SXSSFWorkbook wb = new SXSSFWorkbook(xssfWb, rowMaxCache);

        int pageIndex = 0, pageSize = 30000;
        int pages = dataList.size() / pageSize;
        do {
            List<Object> subList;
            if (dataList.size() >= pageSize) {
                subList = dataList.subList(0, pageSize);
            } else {
                subList = dataList.subList(0, dataList.size());
            }

            //2、创建sheet
            SXSSFSheet sheet = wb.createSheet("Sheet" + (pageIndex * pageSize + 1));
            if (StringUtils.isNoneBlank(rowSpanText)) {
                ///3、创建第一行标题
                Row titleRow = sheet.createRow(0);
                CellStyle rowSpanStyle = wb.createCellStyle();
                rowSpanStyle.setAlignment(HorizontalAlignment.LEFT);
                rowSpanStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                rowSpanStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                rowSpanStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                rowSpanStyle.setBorderBottom(BorderStyle.THIN);
                rowSpanStyle.setBorderLeft(BorderStyle.THIN);
                rowSpanStyle.setBorderTop(BorderStyle.THIN);
                rowSpanStyle.setBorderRight(BorderStyle.THIN);

                Font rowSpanFont = wb.createFont();
                rowSpanFont.setBold(true);
                rowSpanStyle.setFont(rowSpanFont);
                Cell cell = titleRow.createCell(0);
                cell.setCellStyle(rowSpanStyle);
                cell.setCellValue(rowSpanText);
                //第一行标题直接跨所有列
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fieldList.size() - 1));
            }

            AtomicInteger ai = new AtomicInteger();
            {
                if (StringUtils.isNoneBlank(rowSpanText)) {
                    ai = new AtomicInteger(1);
                }
                Row row = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
//            log.info("===fieldList===" + fieldList);
                int j = 0;
                for (String field : fieldList) {
//                    sheet.autoSizeColumn(j);
                    sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 20 / 10);
                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//1
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);//2
                    cellStyle.setBorderBottom(BorderStyle.THIN); //下边框//1
                    cellStyle.setBorderLeft(BorderStyle.THIN);//左边框//1
                    cellStyle.setBorderTop(BorderStyle.THIN);//上边框//1
                    cellStyle.setBorderRight(BorderStyle.THIN);//右边框//1
                    Font font = wb.createFont();
                    font.setBold(true);
                    cellStyle.setFont(font);

                    Cell cell = row.createCell(aj.getAndIncrement());
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(field);
                    j++;
                }
            }
            if (CollectionUtils.isNotEmpty(subList)) {
//            log.info("===fieldList：" + fieldList.size());
//            log.info("===dataList：" + dataList.size());
                for (Object t : subList) {
                    Class clazz = t.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    String[] strArray = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        String fieldName = field.getName();
                        field.setAccessible(true); //设置访问权限
                        String firstLetter = fieldName.substring(0, 1).toUpperCase();
                        String getter = "get" + firstLetter + fieldName.substring(1);
                        Method method = t.getClass().getMethod(getter);
                        Object value = method.invoke(t);
                        if (null != value) {
                            if (field.getType() == Date.class) {
                                strArray[i] = com.mz.common.util.DateUtil.dateFormat((Date) value, com.mz.common.util.DateUtil.DATE_FORMAT_YMDHMS);
                            } else {
                                strArray[i] = value.toString();
                            }
                        } else {
                            strArray[i] = "";
                        }
                    }

                    int andIncrement = ai.getAndIncrement();
                    Row row1 = sheet.createRow(andIncrement);
                    AtomicInteger aj = new AtomicInteger();
                    int i = 0;
                    for (String a : fieldList) {
                        String value = strArray[i];
                        Cell cell = row1.createCell(aj.getAndIncrement());
                        cell.setCellValue(value);
                        i++;
                    }
                    if (andIncrement / 100 == 0) {
                        try {
                            sheet.flushRows();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            dataList.removeAll(subList);
            log.info("last length: " + dataList.size());
            pageIndex++;
        } while (pageIndex <= pages);
        String fileName = cn.hutool.core.date.DateUtil.now() + ConstantsUtil.EXCEL2007;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        buildExcelDocument(fileName, wb, response);
    }

    /**
     * 生成excel文件-列表导出通用(传入List<实体类>取值正常)
     *
     * @param fieldList 表头
     * @param dataList  数据内容
     */
    public static void writeExcelList(List<Object> dataList, List<String> fieldList, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("dataList length: " + dataList.size());
        int rowMaxCache = 100;
        //1、创建workbook
//        Workbook wb = new XSSFWorkbook();
//        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        XSSFWorkbook xssfWb = new XSSFWorkbook();
        SXSSFWorkbook wb = new SXSSFWorkbook(xssfWb, rowMaxCache);

        int pageIndex = 0, pageSize = 30000;
        int pages = dataList.size() / pageSize;
        do {
            List<Object> subList;
            if (dataList.size() >= pageSize) {
                subList = dataList.subList(0, pageSize);
            } else {
                subList = dataList.subList(0, dataList.size());
            }

            //2、创建sheet
            SXSSFSheet sheet = wb.createSheet("Sheet" + (pageIndex * pageSize + 1));

            AtomicInteger ai = new AtomicInteger();
            {
                Row row = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
//            log.info("***fieldList：" + fieldList.toString());
                int j = 0;
                for (String field : fieldList) {
//                sheet.autoSizeColumn(j);
                    sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 35 / 10);
                    String columnName = field;
                    Cell cell = row.createCell(aj.getAndIncrement());

                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置填充方案
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);//2
                    cellStyle.setBorderBottom(BorderStyle.THIN); //下边框//1
                    cellStyle.setBorderLeft(BorderStyle.THIN);//左边框//1
                    cellStyle.setBorderTop(BorderStyle.THIN);//上边框//1
                    cellStyle.setBorderRight(BorderStyle.THIN);//右边框//1
                    Font font = wb.createFont();
                    font.setBold(true);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columnName);
                    j++;
                }
            }
            if (CollectionUtils.isNotEmpty(subList)) {
//            log.info("===fieldList：" + fieldList.size());
//            log.info("===dataList：" + dataList.size());
                for (Object t : subList) {
                    Class clazz = t.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    String[] strArray = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        String fieldName = field.getName();
                        field.setAccessible(true); //设置访问权限
                        String firstLetter = fieldName.substring(0, 1).toUpperCase();
                        String getter = "get" + firstLetter + fieldName.substring(1);
//                    log.info("===getter：" + getter);
                        Method method = t.getClass().getMethod(getter);
                        Object value = method.invoke(t);
//                    log.info("===getter：" + value);
                        if (null != value) {
                            if (field.getType() == Date.class) {
                                strArray[i] = com.mz.common.util.DateUtil.dateFormat((Date) value, com.mz.common.util.DateUtil.DATE_FORMAT_YMDHMS);
                            } else {
                                strArray[i] = value.toString();
                            }
                        } else {
                            strArray[i] = "";
                        }
                    }
                    int andIncrement = ai.getAndIncrement();
                    Row row1 = sheet.createRow(andIncrement);
                    AtomicInteger aj = new AtomicInteger();
                    int i = 0;
                    for (String a : fieldList) {
                        String value = strArray[i];
                        Cell cell = row1.createCell(aj.getAndIncrement());
                        cell.setCellValue(value);
                        i++;
                    }
                    if (andIncrement / 100 == 0) {
                        try {
                            sheet.flushRows();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            dataList.removeAll(subList);
            log.info("last length: " + dataList.size());
            pageIndex++;
        } while (pageIndex <= pages);
        //冻结窗格
//        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
//        //浏览器下载excel
        String fileName = cn.hutool.core.date.DateUtil.now() + ConstantsUtil.EXCEL2007;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        buildExcelDocument(fileName, wb, response);
//        //生成excel文件(测试查看用)
//        String fileNameOut = "E:/export/"+fileName;
//        buildExcelFile(fileNameOut,wb);
    }

    /**
     * 生成excel文件-列表导出通用(传入List<Object>取值的时候，顺序会逆，需要处理)
     *
     * @param fieldList 表头
     * @param dataList  数据内容
     */
    public static void writeExcelListObject(List<Object> dataList, List<String> fieldList, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Workbook wb = new XSSFWorkbook();
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet("Sheet1");

        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
//            log.info("%%% fieldList：" + fieldList.toString());
            int j = 0;
            for (String field : fieldList) {
//                sheet.autoSizeColumn(j);
                sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 35 / 10);
                String columnName = field;
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//1
                cellStyle.setAlignment(HorizontalAlignment.CENTER);//2
                cellStyle.setBorderBottom(BorderStyle.THIN); //下边框//1
                cellStyle.setBorderLeft(BorderStyle.THIN);//左边框//1
                cellStyle.setBorderTop(BorderStyle.THIN);//上边框//1
                cellStyle.setBorderRight(BorderStyle.THIN);//右边框//1
                Font font = wb.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
                j++;
            }
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (Object t : dataList) {
                Class clazz = t.getClass();
                Field[] fields = clazz.getDeclaredFields();
                String[] strArray = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    if (fieldName.contains("$cglib_prop_")) {
                        Matcher matcher = COMPILE.matcher(fieldName);
                        if (matcher.find()) {
                            fieldName = matcher.group(2);
                        }
                    }
                    field.setAccessible(true); //设置访问权限
                    String firstLetter = fieldName.substring(0, 1).toUpperCase();
                    String getter = "get" + firstLetter + fieldName.substring(1);
//                    log.info("%%%getter：" + getter);
                    Method method = t.getClass().getMethod(getter);
                    Object value = method.invoke(t);
//                    log.info("%%%value：" + value);
                    if (null != value) {
                        if (field.getType() == Date.class) {
                            strArray[i] = com.mz.common.util.DateUtil.dateFormat((Date) value, com.mz.common.util.DateUtil.DATE_FORMAT_YMDHMS);
                        } else {
                            strArray[i] = value.toString();
                        }
                    } else {
                        strArray[i] = "";
                    }
                }
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                int i = 1;
                int num = fieldList.size();
                for (String a : fieldList) {
                    String value = strArray[num - i];
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    cell.setCellValue(value);
                    i++;
                }
            }
        }
        //冻结窗格
//        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
//        //浏览器下载excel
        String fileName = cn.hutool.core.date.DateUtil.now() + ConstantsUtil.EXCEL2007;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        buildExcelDocument(fileName, wb, response);
//        //生成excel文件(测试查看用)
//        String fileNameOut = "E:/export/"+fileName;
//        buildExcelFile(fileNameOut,wb);
    }

    /**
     * 导入excel-通用
     *
     * @param file   excel文件
     * @param rowNum 从第几行读取 起始为0  （等于表头行数）
     */
    public static List<Object> readExcel(MultipartFile file, Object object, int rowNum) {
        rowNum = rowNum - 1;
        List<Object> dataList = new ArrayList<Object>();
        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (workbook != null) {
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                Class clazz = object.getClass();
                Field[] fields = clazz.getDeclaredFields();
                boolean firstRow = true;
                for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行
                    if (firstRow) {
                        firstRow = false;
                    } else {
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            //判断是否为空白行
                            boolean allBlank = true;
//                            HashMap addMap = new HashMap();
                            Object object1 = new Object();
                            BeanGenerator beanGenerator = new BeanGenerator();
                            for (int k = 0; k < fields.length; k++) {
                                Field field = fields[k];
                                String fieldName = field.getName();
                                beanGenerator.addProperty(fieldName, String.class);
                            }
                            object1 = beanGenerator.create();
                            Class clazz1 = object1.getClass();
                            Field[] fields1 = clazz1.getDeclaredFields();
                            int a = fields1.length;
                            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                                Cell cell = row.getCell(j);
                                String cellValue = getCellValue(cell);
                                if (StringUtils.isNotBlank(cellValue)) {
                                    allBlank = false;
                                }
                                if (j < a) {
                                    Field field = fields1[j];
                                    field.setAccessible(true);
                                    Object val = cellValue;
                                    field.set(object1, val);
                                }
                            }
                            if (!allBlank) {
                                dataList.add(object1);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                                break;
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse excel exception!", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("parse excel exception!", e);
                }
            }
        }
        return dataList;
    }


    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return DateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
//                return BigDecimal.valueOf(cell.getNumericCellValue()).toString();
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }

    /**
     * 浏览器下载excel
     *
     * @param fileName
     * @param wb
     * @param response
     */

    private static void buildExcelDocument(String fileName, SXSSFWorkbook wb, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setCharacterEncoding("UTF-8");
            wb.write(outputStream);
            outputStream.flush();
            wb.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 生成excel文件
     *
     * @param path 生成excel路径
     * @param wb
     */
    public static void buildExcelFile(String path, SXSSFWorkbook wb) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDefaultValue(Object object) {
        try {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                Class fieldClass = field.getType();
                field.setAccessible(true); //设置访问权限
                if (isFieldValueNull(fieldName, object)) {
                    if (fieldClass == Short.class || fieldClass == short.class) {
                        field.set(object, 0);
                    } else if (fieldClass == Integer.class || fieldClass == int.class) {
                        field.set(object, 0);
                    } else if (fieldClass == Long.class || fieldClass == long.class) {
                        field.set(object, 0L);
                    } else if (fieldClass == Double.class || fieldClass == double.class) {
                        field.set(object, 0.0);
                    } else if (fieldClass == Float.class || fieldClass == float.class) {
                        field.set(object, 0.0);
                    } else if (fieldClass == BigDecimal.class) {
                        field.set(object, 0.0);
                    }
//                    else if (fieldClass == Date.class) {
//                        field.set(object, "");
//                    }
                    else if (fieldClass == String.class) {
                        field.set(object, ""); // 设置值
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    //判断字段是否为空
    private static boolean isFieldValueNull(String fieldName, Object object) throws ClassNotFoundException {
        boolean isNUll = false;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter);
            Object value = method.invoke(object);
            if (value == null) {
                isNUll = true;
            }
            return isNUll;
        } catch (Exception e) {
            return isNUll;
        }
    }

    public static <E> List<Object> toObject(List<E> list) {
        List<Object> objlist = new ArrayList<Object>();
        for (Object e : list) {
            Object obj = e;
            objlist.add(obj);
        }
        return objlist;
    }


    /**
     * 获取对象中的所有属性名与属性值
     *
     * @param object
     * @return
     * @throws
     */
    public static Map<String, Object> getAllPropertyValue(Object object) {
        Map<String, Object> map = new HashMap<>();
        Class<?> aClass = object.getClass();
        if (aClass.equals(HashMap.class) || aClass.equals(JSONObject.class)) {
            return JSONObject.parseObject(JSONObject.toJSONString(object), new TypeReference<Map<String, Object>>() {
            });
        } else {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int index = 0; index < fields.length; index++) {
                Field field = fields[index];
                String propertyName = field.getName();
                if (propertyName.contains("$cglib_prop_")) {
                    Matcher matcher = COMPILE.matcher(propertyName);
                    if (matcher.find()) {
                        propertyName = matcher.group(2);
                    }
                }
                Object propertyValue = getPropertyValueByName(object, propertyName);
                map.put(propertyName, propertyValue);
            }
            return map;
        }
    }


    /**
     * 根据属性名获取对象中的属性值
     *
     * @param propertyName
     * @param object
     * @return
     */
    public static Object getPropertyValueByName(Object object, String propertyName) {
        String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Object value = null;
        try {
            Method method = object.getClass().getMethod(methodName);
            NumberFormat numberFormat = NumberFormat.getInstance();
            value = method.invoke(object);
            if (NumberUtils.isCreatable(value.toString()) && value.toString().indexOf(".") > -1) {
                value = Double.valueOf(value.toString());
            }
        } catch (Exception e) {
//            log.error(String.format("从对象%s获取%s的=属性值失败", object, propertyName));
        }
        return value;
    }

    public static void exportExcelList(Map<String, String> fieldMap, List<Object> dataList, String title, String fileName, String extName, HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //标题样式
        CellStyle headCellStyle = writer.getHeadCellStyle();
        //设置背景色
        headCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        //必须设置 否则背景色不生效
        headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);//增加水平居中样式
        headCellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);//增加垂直居中样式
        //创建标题字体
        Font headFont = writer.createFont();
        headFont.setFontName("宋体");
        //大小
        headFont.setFontHeightInPoints((short) 14);
        //加粗
        headFont.setBold(true);
        headCellStyle.setFont(headFont);

        CellStyle cellStyle = writer.getCellStyle();
        //设置下边框和颜色
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
        //设置左边框和颜色
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
        //设置右边框和颜色
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setRightBorderColor(IndexedColors.BLACK1.getIndex());
        //设置上边框边框和颜色
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setTopBorderColor(IndexedColors.BLACK1.getIndex());
        //设置文本是否换行
        cellStyle.setWrapText(false);

        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER); //水平左对齐，垂直中间对齐
        // 合并单元格后的标题行
        int lastColumn = fieldMap.size() - 1;
        if (StringUtil.isNotEmpty(title)) {
            writer.merge(lastColumn, title);
        }

        // 列别名
        fieldMap.forEach((key, val) -> {
            writer.addHeaderAlias(key, val);
        });

        Workbook workbook = writer.getWorkbook();
        Sheet sheet = writer.getSheet();
        //设置默认高度
        sheet.setDefaultRowHeight((short) 1000);
        int defaultColumnWidth = sheet.getDefaultColumnWidth();
        //构建画图对象
        Drawing<?> patriarch = sheet.createDrawingPatriarch();

        List<Map<String, String>> retList = new ArrayList<>();
        for (int i = 0; CollectionUtil.isNotEmpty(dataList) && i < dataList.size(); i++) {
            String jsonStr = JSON.toJSONString(dataList.get(i), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
            Map<String, String> retMap = JSON.parseObject(jsonStr, LinkedHashMap.class);
            if (CollectionUtil.isNotEmpty(retMap)) {
                int j = 0;
                for (Map.Entry<String, String> entry : retMap.entrySet()) {
                    String field = entry.getValue();
                    if (StringUtil.isNotEmpty(field)) {
                        if (field.endsWith(".jpeg") || field.endsWith(".JPEG") ||
                                field.endsWith(".jpg") || field.endsWith(".JPG") ||
                                field.endsWith(".png") || field.endsWith(".PNG") ||
                                field.endsWith(".gif") || field.endsWith(".GIF") ||
                                field.endsWith(".bmp") || field.endsWith(".BMP") ||
                                field.endsWith(".ico") || field.endsWith(".ICO")) {
                            sheet.setColumnWidth(j, 10 * 256);
                            WriteImgUtil.writeImg(i + 1, j, field, workbook, patriarch);
                            retMap.put(entry.getKey(), "");
                        } else {
                            sheet.setColumnWidth(j, defaultColumnWidth);
                        }
                    }
                    j++;
                }
                retList.add(retMap);
            }
        }

        List<Object> objList = ExcelUtils.toObject(retList);
        writer.write(objList, true);
        // 设置每列自定义宽度
        setSizeColumn(writer.getSheet(), lastColumn);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        if (StringUtils.isEmpty(extName)) {
            extName = ".xlsx";
        } else {
            extName = "." + extName;
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + extName, "UTF-8"));
        ServletOutputStream servletOutputStream = response.getOutputStream();
        writer.flush(servletOutputStream, true);
        // 关闭writer，释放内存
        writer.close();
        // 关闭输出Servlet流
        IoUtil.close(servletOutputStream);
    }

    /**
     * 自适应宽度(中文支持)
     *
     * @param sheet
     * @param size  因为for循环从0开始，size值为 列数-1
     */
    public static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    /**
     * 生成excel文件-列表导出通用(传入List<Object>取值的时候，顺序会逆，需要处理)
     *
     * @param fieldList 表头
     * @param dataList  数据内容
     */
    public static void writeExcelListMap(List<Map<String, String>> dataList, List<String> fieldList, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");

        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            System.out.println("--------------------fieldList---------------------------------" + fieldList.toString());
            int j = 0;
            for (String field : fieldList) {
                sheet.setDefaultColumnWidth(30);
                String columnName = field;
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//1
                cellStyle.setAlignment(HorizontalAlignment.CENTER);//2
                cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
                cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
                cellStyle.setBorderTop(BorderStyle.THIN);//上边框
                cellStyle.setBorderRight(BorderStyle.THIN);//右边框
                Font font = wb.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
                j++;
            }
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            System.out.println("--------------------fieldList---------------------------------" + fieldList.size());
            System.out.println("--------------------dataList---------------------------------" + dataList.size());
            for (Map<String, String> map : dataList) {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                for (String v : map.values()) {
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    cell.setCellValue(v);
                }
            }
        }
        //冻结窗格
//        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
//        //浏览器下载excel
        String fileName = cn.hutool.core.date.DateUtil.now() + ConstantsUtil.EXCEL2007;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        buildExcelDocument(fileName, wb, response);
//        //生成excel文件(测试查看用)
//        String fileNameOut = "E:/export/"+fileName;
//        buildExcelFile(fileNameOut,wb);
    }

    /**
     * 生成excel文件-列表导出通用
     * 两层表头，一层根据子表头数量合并单元格，无子表头合并上下两格。
     *
     * @param titleList 多层表头
     * @param dataList  数据内容
     */
    public static void writeExcelListMapMoreTitle(List<Map<String, String>> dataList, List<TitleModel> titleList, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet("Sheet1");
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//1
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//2
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setWrapText(true);
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            int j = 0;
            for (TitleModel titleModel : titleList) {
                List<TitleModel> sonList = titleModel.getSonTitle();
//                sheet.autoSizeColumn(j);
                if (sonList != null && sonList.size() > 0) {
                    for (TitleModel model : sonList) {
                        sheet.setDefaultColumnWidth(30);
                        Cell cell = row.createCell(aj.getAndIncrement());
//
                        Font font = wb.createFont();
                        font.setBold(true);
                        String columnName = titleModel.getName();
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(columnName);
                    }
                    if (titleModel.getSonTitle().size() > 1) {
                        CellRangeAddress region = new CellRangeAddress(0, 0, j, j + titleModel.getSonTitle().size() - 1);//起始行、结束行、起始列、结束列
                        sheet.addMergedRegion(region);
                    }
                    j = j + titleModel.getSonTitle().size();
                } else {
                    sheet.setDefaultColumnWidth(30);
                    Cell cell = row.createCell(aj.getAndIncrement());
                    Font font = wb.createFont();
                    font.setBold(true);
                    String columnName = titleModel.getName();
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columnName);
                    CellRangeAddress region = new CellRangeAddress(0, 1, j, j);//起始行、结束行、起始列、结束列
                    sheet.addMergedRegion(region);
                    j++;
                }

            }
            Row rowSon = sheet.createRow(ai.getAndIncrement());
            AtomicInteger ajSon = new AtomicInteger();
            int js = 0;
            for (TitleModel titleModel : titleList) {
//                sheet.autoSizeColumn(js);

                List<TitleModel> sonList = titleModel.getSonTitle();
                if (sonList != null && sonList.size() > 0) {
                    for (TitleModel son : sonList) {
                        sheet.setDefaultColumnWidth(30);
                        Cell cell = rowSon.createCell(ajSon.getAndIncrement());
                        Font font = wb.createFont();
                        font.setBold(true);
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                        String columnName = son.getName();
                        cell.setCellValue(columnName);
                        js++;
                    }
                } else {
                    sheet.setDefaultColumnWidth(30);
                    Cell cell = rowSon.createCell(ajSon.getAndIncrement());
                    Font font = wb.createFont();
                    font.setBold(true);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                    js++;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            System.out.println("--------------------dataList---------------------------------" + dataList.size());
            for (Map<String, String> map : dataList) {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                for (String v : map.values()) {
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    cell.setCellValue(v);
                }
            }
        }
        //冻结窗格
//        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
//        //浏览器下载excel
        String fileName = cn.hutool.core.date.DateUtil.now() + ConstantsUtil.EXCEL2007;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        buildExcelDocument(fileName, wb, response);
//        //生成excel文件(测试查看用)
//        String fileNameOut = "E:/export/"+fileName;
//        buildExcelFile(fileNameOut,wb);
    }

    /**
     * 时间格式字符串处理
     *
     * @return
     */
    public static String getTimeToString(Object propertyValue) throws ParseException {
        String name = null;
        if (propertyValue != null) {
            String tenm = propertyValue.toString();
            if (!com.mz.common.util.StringUtils.isEmpty(tenm)) {
                name = tenm;
                if (tenm.contains("CST")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    Date date = sdf.parse(tenm);
                    name = cn.hutool.core.date.DateUtil.formatDate(date);
                }
                if (tenm.contains(".")) {
                    name = tenm.replace(".", "-");
                    String[] dateStrArr = name.split("-");
                    if (dateStrArr.length == 3) {//年月日格式存在
                        name = cn.hutool.core.date.DateUtil.formatDate(cn.hutool.core.date.DateUtil.parseDate(name));
                    }
                }
                if (!StringFormatUtil.isDate(name)) {
                    name = null;
                }
            }
        }
        return name;
    }


    /**
     * 导入excel-不固定表头，返回每个单元格的值
     *
     * @param file   excel文件
     * @param rowNum 从第几行读取 起始为0  （等于表头行数）
     */
    public static List<ExcelStringModel> readExcelForCell(MultipartFile file, int rowNum) {
        List<ExcelStringModel> modelList = new ArrayList<>();
        rowNum = rowNum - 1;
        List<Object> dataList = new ArrayList<Object>();
        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (workbook != null) {
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
//                Class clazz = object.getClass();
//                Field[] fields = clazz.getDeclaredFields();
                boolean firstRow = true;
                for (int i = rowNum; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //忽略空白行
                    if (row == null) {
                        continue;
                    }
                    try {
                        List<CellRangeAddress> rangeAddressList = sheet.getMergedRegions();
                        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                            CellRangeAddress cellAddresses = getCellRangeAddress(rangeAddressList, row.getRowNum(), j);
                            if (cellAddresses == null) {//不是合并的单元和
                                ExcelStringModel model = new ExcelStringModel();
                                model.setArrangeNum(j);
                                model.setLineNum(i);
                                Cell cell = row.getCell(j);
                                String cellValue = getCellValue(cell);
                                model.setDataValue(cellValue);
                                modelList.add(model);
                            }
                        }
                    } catch (Exception e) {
                        log.error(String.format("parse row:%s exception!", i), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse excel exception!", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("parse excel exception!", e);
                }
            }
        }
        return modelList;
    }

    /**
     * 导入excel-不固定表头,两层表头，返回表头数据
     *
     * @param file   excel文件
     * @param rowNum 从第几行读取 （等于表头行数）
     */

    public static List<TitleModel> readExcelTitle(MultipartFile file, int rowNum) {
        rowNum = rowNum - 1;
        List<TitleModel> modelList = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (workbook != null) {
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                List<CellRangeAddress> rangeAddressList = sheet.getMergedRegions();
                //起始行==第一行表头
                Row row = sheet.getRow(rowNum);
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    TitleModel titleModel = new TitleModel();
                    Cell cell = row.getCell(j);
                    String cellValue = getCellValue(cell);
                    titleModel.setName(cellValue);
                    titleModel.setDataName(String.valueOf(j));
                    CellRangeAddress cellAddresses = getCellRangeAddress(rangeAddressList, row.getRowNum(), j);
                    if (cellAddresses != null) {//是合并的单元和
                        //判定单元格
                        //合并单元格类型（1-上下合并行，2左右合并列，3行列都合并）
                        int rangeType = getCellRangeAddressType(cellAddresses);
                        //2左右合并列,j直接等于合并单元格的最后列号,从合并列之后继续遍历
                        if (rangeType == 2 || rangeType == 3) {
                            Row row2 = sheet.getRow(rowNum + 1);
                            List<TitleModel> sunList = new ArrayList<>();
                            for (int k = cellAddresses.getFirstColumn(); k <= cellAddresses.getLastColumn(); k++) {
                                TitleModel sun = new TitleModel();
                                Cell cellSun = row2.getCell(k);
                                String cellValueSun = getCellValue(cellSun);
                                sun.setId(Long.valueOf(rangeType));//暂存合并类型
                                sun.setDataName(String.valueOf(j));
                                sun.setName(cellValueSun);
                                sunList.add(sun);
                                j++;
                            }
                            //子表头遍历完，多加了1，这里要减去
                            j = j - 1;
                            titleModel.setSonTitle(sunList);
                        } else {
                            titleModel.setId(Long.valueOf(rangeType));
                        }
                    }
                    modelList.add(titleModel);
                }


            }
        } catch (Exception e) {
            log.error("parse excel exception!", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("parse excel exception!", e);
                }
            }
        }
        return modelList;
    }

    /**
     * 判断是否合并单元格，返回合并单元格信息
     *
     * @param rangeAddressList
     * @param row
     * @param column
     * @return
     */
    public static CellRangeAddress getCellRangeAddress(List<CellRangeAddress> rangeAddressList, int row, int column) {
        CellRangeAddress res = null;
        for (CellRangeAddress cellAddresses : rangeAddressList) {
            int firstRow = cellAddresses.getFirstRow();
            int lastRow = cellAddresses.getLastRow();
            int firstColumn = cellAddresses.getFirstColumn();
            int lastColumn = cellAddresses.getLastColumn();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    res = cellAddresses;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 合并单元格类型（1-上下合并行，2左右合并列，3行列都合并）
     *
     * @return
     */
    public static int getCellRangeAddressType(CellRangeAddress cellAddresses) {
        int res = 3;
        int firstRow = cellAddresses.getFirstRow();
        int lastRow = cellAddresses.getLastRow();
        int firstColumn = cellAddresses.getFirstColumn();
        int lastColumn = cellAddresses.getLastColumn();
        if (firstRow == lastRow) {
            res = 2;
        } else if (firstColumn == lastColumn) {
            res = 1;
        }
        return res;
    }
}