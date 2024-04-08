package com.mz.common.util;


import cn.hutool.core.date.DateUtil;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatUtil {

    /**
     * 中文数字
     */
    private static final String[] CN_NUM = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    /**
     * 中文数字单位
     */
    private static final String[] CN_UNIT = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 特殊字符：点
     */
    public static String SPECIAL_STRING = "Φ";//特殊分隔符
    private static final String CN_POINT = "点";
    public static Integer PAGE_NO_DEFAULT = 1;//默认页码
    public static Integer PAGE_SIZE_DEFAULT = 10;//默认每页条数

    /**
     * 补全字符串
     *
     * @param s 字符串
     * @param a 补全至几位
     * @return 字符串
     */
    public static String stringCompl(String s, Integer a) {

        String newStr = "";
        for (int i = 0; i < a - s.length(); i++) {
            newStr += "0";
        }
        newStr = newStr + s;
        return newStr;
    }

    /**
     * 比较一遍找出最大值
     */
    public static double getMaxNum(double[] arr) {
        // 记录下标
        int index = 0;
        // 假设第一个为最大值
        double max = arr[0];
        for (int i = index; i < arr.length; i++) {
            if (i + 1 < arr.length && max < arr[i + 1]) {
                index = i + 1;
                max = arr[i + 1];
            }
        }
        return max;
    }

    /**
     * 比较一遍找出最小值
     */
    public static double getMinNum(double[] arr) {
        // 记录下标
        int index = 0;
        // 假设第一个为最小值
        double min = arr[0];
        for (int i = index; i < arr.length; i++) {
            if (i + 1 < arr.length && min > arr[i + 1]) {
                index = i + 1;
                min = arr[i + 1];
            }
        }
        return min;
    }

    public static String idHandle(String idCardNumber) {
        if (StringUtils.isEmpty(idCardNumber) || (idCardNumber.length() < 8)) {
            return idCardNumber;
        }
//        return idCardNumber.replaceAll("(?<=\\w{2})\\w(?=\\w{2})", "*");
        return idCardNumber.replaceAll("(?<=\\w{1})\\w(?=\\w{1})", "*");
    }
    public static String bankCardHandle(String idCardNumber) {
        if (StringUtils.isEmpty(idCardNumber) || (idCardNumber.length() < 8)) {
            return idCardNumber;
        }
        return idCardNumber.replaceAll("(?<=\\w{2})\\w(?=\\w{2})", "*");
    }

    public static String phoneHandle(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return phoneNumber;
        }
        return phoneNumber.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    /**
     * @description:
     * @return: 判断字符串中是否含中文
     * @author: Administrator
     * @date: 2019/10/4
     */
    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 中文数字转阿拉伯数字
     */
    public static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
        char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    /**
     * 获取字符串中的数字
     */
    public static int getStrInt(String chineseNumber) {
        int resultInt = 0;
        if (!StringUtils.isEmpty(chineseNumber)) {
            String result = "";
            char[] cnArr = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            for (int i = 0; i < chineseNumber.length(); i++) {
                char c = chineseNumber.charAt(i);
                for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                    if (c == cnArr[j]) {
                        result += cnArr[j];
//                    }else{
//                        if(!StringUtils.isEmpty(result)){
//                            break;
//                        }
                    }
                }
            }
            if (!StringUtils.isEmpty(result)) {
                resultInt = Integer.valueOf(result);
            }
        }
        return resultInt;
    }

    /**
     * 添加星号
     */
    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    /**
     * 姓名隐藏名
     */
    public static String desensitizedName(String fullName) {
        if (!StringUtils.isEmpty(fullName)) {
            String name = fullName.substring(0, 1);
            return name + createAsterisk(fullName.length() - 1);
        } else {
            return "*";
        }
    }
    /**
     * 隐藏公司名
     */
    public static String hiddenCompanyName(String fullName) {
        if (!StringUtils.isEmpty(fullName)) {
            String name = fullName.substring(0, 2);
            String company = fullName.substring(fullName.length() - 2);
            return name + createAsterisk(fullName.length() - 4)+company;
        } else {
            return null;
        }
    }

    //	将实体类转化成MAP
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    //	将数组转化城list
    public static List<String> arrToList(String[] arr) {
        List<String> list = new ArrayList<String>();
        for (String s : arr) {
            list.add(s.trim());
        }
        return list;
    }

    /**
     * 生成订单商品核销码
     * 生成核销码的方法，生成规格为：两位数年 + 当前是今年第几天 + 7位随机数
     *
     * @return
     */
    public static String genVerifyCode() {
        String year = new SimpleDateFormat("yy").format(new Date());
        String day = String.format("%tj", new Date());
        double random = Math.random() * 10000000;
        while (random < 1000000) {
            random = Math.random() * 10000000;
        }
        int intRandom = Double.valueOf(random).intValue();
        String verifyCode = year + day + intRandom;
        return verifyCode;
    }

    public static File asFile(InputStream inputStream) throws IOException {
        File tmp = File.createTempFile("lzq", ".tmp", new File("d:\\"));
        OutputStream os = new FileOutputStream(tmp);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        return tmp;
    }

    //Unicode转中文方法
    private static String unicodeToCn(String unicode) {
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        for (int i = 1; i < strs.length; i++) {
            if (strs[i].length() > 4) {
                returnStr += (char) Integer.valueOf(strs[i].substring(0, 4), 16).intValue();
                System.out.println(strs[i]);
                returnStr += strs[i].substring(4);
            } else {
                returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
            }

        }
        return returnStr;
    }

    /**
     * int 转 中文数字
     * 支持到int最大值
     */
    public static String int2chineseNum(int intNum) {
        StringBuffer sb = new StringBuffer();
        boolean isNegative = false;
        if (intNum < 0) {
            isNegative = true;
            intNum *= -1;
        }
        int count = 0;
        while (intNum > 0) {
            sb.insert(0, CN_NUM[intNum % 10] + CN_UNIT[count]);
            intNum = intNum / 10;
            count++;
        }

        if (isNegative)
            sb.insert(0, CN_NEGATIVE);


        return sb.toString().replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    /**
     * bigDecimal 转 中文数字
     * 整数部分只支持到int的最大值
     */
    public static String bigDecimal2chineseNum(BigDecimal bigDecimalNum) {
        if (bigDecimalNum == null)
            return CN_NUM[0];

        StringBuffer sb = new StringBuffer();

        //将小数点后面的零给去除
        String numStr = bigDecimalNum.abs().stripTrailingZeros().toPlainString();

        String[] split = numStr.split("\\.");
        String integerStr = int2chineseNum(Integer.parseInt(split[0]));

        sb.append(integerStr);

        //如果传入的数有小数，则进行切割，将整数与小数部分分离
        if (split.length == 2) {
            //有小数部分
            sb.append(CN_POINT);
            String decimalStr = split[1];
            char[] chars = decimalStr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int index = Integer.parseInt(String.valueOf(chars[i]));
                sb.append(CN_NUM[index]);
            }
        }

        //判断传入数字为正数还是负数
        int signum = bigDecimalNum.signum();
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }

        return sb.toString();
    }

    /**
     * 根据身份证号判断用户性别
     *
     * @param idNo
     * @return 性别1男2女
     */
    public static int getSex(String idNo) {
        String sexStr = "0";
        if (idNo.length() == 15) {
            sexStr = idNo.substring(14, 15);
        } else if (idNo.length() == 18) {
            sexStr = idNo.substring(16, 17);
        }
        int sexNo=1;
        if(sexStr.matches("[0-9]+")){
            sexNo = Integer.parseInt(sexStr);
        }
        return sexNo % 2 == 0 ? 2 : 1;
    }

    /**
     * 根据身份证号获取出生日期
     *
     * @param idNo
     * @return 出生日期
     */
    public static String getBirthDate(String idNo) {
        String birthDayStr = idNo.substring(6, 14);
        if (isRqFormat(birthDayStr)) {
            Date birthDate = DateUtil.parse(birthDayStr, "yyyyMMdd");
            return DateUtil.formatDate(birthDate);
        } else {
            return null;
        }
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int getAge(Date birthDate) {
        if (birthDate != null) {
            if(birthDate.before(DateUtil.parseDate(DateUtil.now()))){
                return DateUtil.age(birthDate, DateUtil.parseDate(DateUtil.now()));
            }else{
                return 0;
            }
        } else {
            return 0;
        }
    }

    /***
     * 判断字符串是否是yyyyMMdd格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isRqFormat(String mes) {
        String format = "([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
            matcher = pattern.matcher(mes);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m - 1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;

    }
    /***
     * 判断字符串是否是yyyy-MM-dd格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isDate(String mes) {
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(mes);
        boolean dateFlag = m.matches();
        return dateFlag;
    }

    /**
     * double转string  去除double数值过大时的科学计数方式
     * @param num  要转换的double数值
     * @param length   保留几位小数点
     * @return:
     */
    public static String doubleTransitionString(double num,int length){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);      //关闭科学计数法
        nf.setMaximumFractionDigits(length);   //定义保留几位小数
        return nf.format(num);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(isRqFormat("19878809"));
    }

}
